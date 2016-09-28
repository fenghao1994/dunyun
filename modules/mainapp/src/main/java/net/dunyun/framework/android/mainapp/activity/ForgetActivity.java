package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.ForgetBiz;
import net.dunyun.framework.android.mainapp.biz.ForgetCallback;
import net.dunyun.framework.android.mainapp.biz.GetAuthCodeAgainBiz;
import net.dunyun.framework.android.mainapp.biz.GetAuthCodeAgainCallback;
import net.dunyun.framework.android.mainapp.biz.GetAuthCodeBiz;
import net.dunyun.framework.android.mainapp.biz.GetAuthCodeCallback;
import net.dunyun.framework.android.mainapp.biz.RegisterBiz;
import net.dunyun.framework.android.mainapp.biz.RegisterCallback;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class ForgetActivity extends BaseActivity implements ForgetCallback, GetAuthCodeAgainCallback {
    private Context context = null;
    private LoadingDialog loadingDialog = null;

    @Bind(R.id.bt_forget) Button bt_forget;
    @Bind(R.id.bt_getAuthCode) Button bt_getAuthCode;
    @Bind(R.id.et_userName) EditText et_userName;
    @Bind(R.id.et_code) EditText et_code;
    @Bind(R.id.et_passwd) EditText et_passwd;

    ForgetBiz forgetBiz;
    GetAuthCodeAgainBiz getAuthCodeAgainBiz;
    private String username;
    private String newPasswd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        setTitleInVisibility();
        baseSetContentView(R.layout.activity_forget);
        ButterKnife.bind(this);

        forgetBiz = new ForgetBiz(this);
        getAuthCodeAgainBiz = new GetAuthCodeAgainBiz(this);
    }

    @OnClick(R.id.title_exit_btn) void exit(){
        this.finish();
    }

    @OnClick(R.id.bt_forget) void forget(){
        username = et_userName.getText().toString().trim();
        String password = et_passwd.getText().toString().trim();
        newPasswd = password;
        final String code = et_code.getText().toString().trim();

        if (StrUtil.isEmpty(username) || username.length() != 11) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_username_input_tips));
            return;
        }
        if (StrUtil.isEmpty(password)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        if (StrUtil.isEmpty(code)) {
            ToastUtil.showToast(context, getResources().getString(R.string.code_input_tips));
            return;
        }

        password = AesUtil.getInstance().encrypt(password.getBytes());
        UserVo userVo = new UserVo();
        userVo.setMobile(username);
        userVo.setPassWord(password);
        forgetBiz.forget(userVo, code);
    }

    @OnClick(R.id.bt_getAuthCode) void getAuthCode(){
        final String username = et_userName.getText().toString().trim();
        if (StrUtil.isEmpty(username)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_username_input_tips));
            return;
        }

        startTimer();
        getAuthCodeAgainBiz.getAuthCode(username);
    }

    @Override
    public void onAuthSuccess(String result) {
        ToastUtil.showToast(context, "获取验证码成功");
    }

    @Override
    public void onAuthFailed(String result) {
        ToastUtil.showToast(context, "获取验证码失败,"+result);

        stopTimer();
    }

    @Override
    public void onForgetSuccess(String result) {
        ToastUtil.showToast(context, "重设密码成功");

        Bundle bundle = new Bundle();
        bundle.putString("phone", username);
        bundle.putString("passwd", newPasswd);
        ForgetActivity.this.setResult(0, ForgetActivity.this.getIntent().putExtras(bundle));
        ForgetActivity.this.finish();
    }

    @Override
    public void onForgetFailed(String result) {
        ToastUtil.showToast(context, "重设密码失败,"+result);
    }

    private int recLen = 60;
    private Timer timer;
    private TimerTask task;

    private void startTimer() {
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() { // UI thread

                    @Override
                    public void run() {
                        recLen--;
                        bt_getAuthCode.setText("" + recLen);
                        if (recLen <= 0) {
                            if (timer != null)
                                timer.cancel();
                            if (task != null)
                                task.cancel();
                            bt_getAuthCode.setEnabled(true);
                            bt_getAuthCode.setText("获取验证码");
                            recLen = 30;
                        } else {
                            bt_getAuthCode.setEnabled(false);
                            bt_getAuthCode.setText("等待" + recLen + "S");
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                bt_getAuthCode.setEnabled(true);
                bt_getAuthCode.setText("获取验证码");
            }
        });
    }
}
