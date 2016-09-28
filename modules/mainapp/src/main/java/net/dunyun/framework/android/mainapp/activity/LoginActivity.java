package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.fragment.HomeFragment;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.Aes2;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;
//import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 名称：LoginActivity.java
 * 描述：登录界面.
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class LoginActivity extends BaseActivity implements LoginCallback {
    @Bind(R.id.et_userName) EditText et_userName;
    @Bind(R.id.et_pass) EditText et_password;
    @Bind(R.id.bt_login) Button bt_login;
    @Bind(R.id.register_tv) TextView register_tv;
    @Bind(R.id.find_passwd_tv) TextView find_passwd_tv;
    @Bind(R.id.title_left_btn) ImageButton title_left_btn;

    private Context context = null;
    private static LoadingDialog loadingDialog = null;

    String password;

    LoginBiz loginBiz;
    private boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);
        context = this;

        setTitleInVisibility();
        baseSetContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        loginBiz = new LoginBiz(this);
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            String phone = intent.getStringExtra("phone");
            if(phone != null){
                et_userName.setText(phone);
            }
        }
    }


    @OnClick(R.id.title_exit_btn) void exit(){
        this.finish();
    }
    /***
     * 登录sslol
     */
    @OnClick(R.id.bt_login) void login() {
        click = true;
        loginTrue();
    }

    private void loginTrue(){
        //获取用户名和密码数据
        final String username = et_userName.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (StrUtil.isEmpty(username)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_username_input_tips));
            return;
        }
        if (StrUtil.isEmpty(password)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        loadingDialog = DialogUtil.showWaitDialog(context, getResources().getString(R.string.login_progressDialog_content));
        password = AesUtil.getInstance().encrypt(password.getBytes());
        String clientId = PushManager.getInstance().getClientid(this.getApplicationContext());
        UserVo userVo = new UserVo();
        userVo.setMobile(username);
        userVo.setPassWord(password);
        userVo.setPushId(clientId);
        loginBiz.login(userVo);
    }

    @OnClick(R.id.register_tv) void register(){
        click = true;
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivityForResult(intent, 0);
    }

    @OnClick(R.id.find_passwd_tv) void forget(){
        click = true;
        Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
        LoginActivity.this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0 && data != null){
            Bundle bundle = data.getExtras();
            String phoneno = bundle.getString("phone");
            String passwd = bundle.getString("passwd");
            et_userName.setText(phoneno);
            et_password.setText(passwd);

            loginTrue();

        }
    }

    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }

    @Override
    public void onSuccess(UserVo userVo) {

        DialogUtil.cancelWaitDialog(loadingDialog);

        userVo.setPassWord(password);
        mainApplication.setUserVo(userVo);
        SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
        SharedUtil.putString(context, "phone" , userVo.getMobile());

        HomeFragment.refresh = true;

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if(click){
            LoginActivity.this.startActivity(intent);
        }else{
            LoginActivity.this.setResult(0, intent);
        }
        LoginActivity.this.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);

        showToast(getResources().getString(R.string.login_failed) + "," + result);
    }
}
