package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.network.http.TextHttpResponseHandler;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.ChangeBiz;
import net.dunyun.framework.android.mainapp.biz.ChangeCallback;
import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 名称：ChangeActivity.java
 * 描述：修改密码界面
 *
 * @author wuhx
 * @version v1.0
 * @date2016-04-11
 * @Copyright:
 *
 */
public class ChangeActivity extends BaseActivity implements ChangeCallback {
    private EditText et_old_password = null;
    private EditText et_new_password = null;
    private EditText et_new_password_check = null;
    private Context context = null;
    private ProgressDialog progressDialog = null;
    private ChangeBiz mChangeBiz=null;
    @Bind(R.id.cb_passwd_old) CheckBox cb_passwd_old;
    @Bind(R.id.cb_passwd_new) CheckBox cb_passwd_new;
    private String newPassWord=null;
    private UserVo userVo;
    private String oldPassword;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //baseSetContentView(R.layout.activity_update_password);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_check = (EditText) findViewById(R.id.et_new_password_check);
        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.update_password_title), new LeftOnClickListener(), null, null);
    }

    /***
     * 修改密码
     * @param
     */
    public void btupdate() {

        oldPassword = et_old_password.getText().toString().trim();
        newPassword = et_new_password.getText().toString().trim();
        //final String newPasswordCheck = et_new_password_check.getText().toString().trim();
        if (StrUtil.isEmpty(oldPassword)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        if (StrUtil.isEmpty(newPassword)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.login_progressDialog_title),
                getResources().getString(R.string.update_progressDialog_content));

        PushManager.getInstance().initialize(this.getApplicationContext());
        /***
         * 跳转到blz
         * @param
         */
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }

        oldPassword = AesUtil.getInstance().encrypt(oldPassword.getBytes());
        newPassword = AesUtil.getInstance().encrypt(newPassword.getBytes());

        mChangeBiz=new ChangeBiz(this);
        newPassWord=newPassword;
        mChangeBiz.changeBiz(userVo,newPassword,oldPassword);

    }

    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        ChangeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }

    /**
     *  返回上一界面
     */
    class  LeftOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @OnClick(R.id.bt_update) void update(){
            btupdate();
    }
    @OnCheckedChanged(R.id.cb_passwd_old) void check_oldPassWd(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked){
            //如果选中，显示密码
            et_old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            et_old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
    @OnCheckedChanged(R.id.cb_passwd_new) void check_newPassWd(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked){
            //如果选中，显示密码
            et_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            //否则隐藏密码
            et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
    /***
     * callback回调
     * @param提示内容
     */
    @Override
    public void onSuccess(UserVo userVo) {
        DialogUtil.cancelProgressDialog(progressDialog);

        userVo.setPassWord(newPassWord);
        SharedUtil.remove(context, UserVo.class + "");
        SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
        showToast(getResources().getString(R.string.update_password_success));
        mainApplication.setUserVo(userVo);
        /**查看修改后的本地信息**/
        String m=SharedUtil.getString(context, UserVo.class + "");
        UserVo xx=JsonUtil.parseObject(m, UserVo.class);
        LogUtil.i("修改后密码后", "password+" + xx.getPassWord() + "token+" + xx.getToken() + " " + xx.getMobile() + " ");

    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelProgressDialog(progressDialog);
        showToast("修改失败，" + "," + result);
    }
}
