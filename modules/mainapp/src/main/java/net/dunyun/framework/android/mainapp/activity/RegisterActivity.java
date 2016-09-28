package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import net.dunyun.framework.android.mainapp.biz.GetAuthCodeCallback;
import net.dunyun.framework.android.mainapp.biz.RegisterBiz;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.GetAuthCodeBiz;
import net.dunyun.framework.android.mainapp.biz.RegisterCallback;
import net.dunyun.framework.lock.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册界面.
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class RegisterActivity extends BaseActivity implements RegisterCallback, GetAuthCodeCallback {
    private Context context = null;
    private LoadingDialog loadingDialog = null;

    @Bind(R.id.bt_register)
    Button bt_register;
    @Bind(R.id.bt_getAuthCode)
    Button bt_getAuthCode;
    @Bind(R.id.et_userName)
    EditText et_userName;
    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.et_passwd)
    EditText et_passwd;

    RegisterBiz registerBiz;
    GetAuthCodeBiz getAuthCodeBiz;
    private String username;
    private String password;
    private String newPasswd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);
        context = this;

        setTitleInVisibility();
        baseSetContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerBiz = new RegisterBiz(this);
        getAuthCodeBiz = new GetAuthCodeBiz(this);

        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    @OnClick(R.id.title_exit_btn) void exit(){
        this.finish();
    }

    @OnClick(R.id.bt_register)
    void register() {
        username = et_userName.getText().toString().trim();
        password = et_passwd.getText().toString().trim();
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
        loadingDialog = DialogUtil.showWaitDialog(context, "正在注册...");
        password = AesUtil.getInstance().encrypt(password.getBytes());
        String clientId = PushManager.getInstance().getClientid(this.getApplicationContext());
        LogUtil.d(clientId);
        UserVo userVo = new UserVo();
        userVo.setMobile(username);
        userVo.setPassWord(password);
        userVo.setDeviceType("1");
        userVo.setPushId(clientId);
        registerBiz.register(userVo, code);
    }

    @OnClick(R.id.bt_getAuthCode)
    void getAuthCode() {
        final String username = et_userName.getText().toString().trim();
        if (StrUtil.isEmpty(username)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_username_input_tips));
            return;
        }
        startTimer();
        getAuthCodeBiz.getAuthCode(username);
    }

    @Override
    public void onAuthSuccess(String result) {
        ToastUtil.showToast(context, "获取验证码成功");
    }

    @Override
    public void onAuthFailed(String result) {
        ToastUtil.showToast(context, "获取验证码失败," + result);
        stopTimer();
    }

    @Override
    public void onRegSuccess(UserVo userVo) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "注册成功");

        Bundle bundle = new Bundle();
        bundle.putString("phone", username);
        bundle.putString("passwd", newPasswd);
        RegisterActivity.this.setResult(0, RegisterActivity.this.getIntent().putExtras(bundle));
        RegisterActivity.this.finish();
    }

    @Override
    public void onRegFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "注册失败," + result);
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
