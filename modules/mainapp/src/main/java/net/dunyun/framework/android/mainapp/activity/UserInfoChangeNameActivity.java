package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.UserInfoChangeBlz;
import net.dunyun.framework.android.mainapp.biz.UserInfoChangeCallback;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 名称：UserInfoChangeNameActivity.java
 * 描述：修改昵称界面
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-20
 * @Copyright:
 *
 */
public class UserInfoChangeNameActivity extends BaseActivity implements UserInfoChangeCallback {

    private ProgressDialog progressDialog = null;
    private UserInfoChangeBlz mUserInfoChangeNameBlz=null;
    private Context context = null;
    private String newNmae=null;
    @Bind(R.id.et_new_name) EditText et_new_name;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        //baseSetContentView(R.layout.activity_update_password);
        setContentView(R.layout.activity_user_info_changename);
        ButterKnife.bind(this);

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            et_new_name.setText(userVo.getNickName());
        }

        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), "修改昵称", new LeftOnClickListener(), null, null);
    }
    @OnClick(R.id.bt_change_name) void changeName(){
        newNmae = et_new_name.getText().toString().trim();
        //final String newPasswordCheck = et_new_password_check.getText().toString().trim();
        if (StrUtil.isEmpty(newNmae)) {
            ToastUtil.showToast(context, getResources().getString(R.string.et_new_name_tip));
            return;
        }
        progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.login_progressDialog_title),
                getResources().getString(R.string.update_progressDialog_content));

        PushManager.getInstance().initialize(this.getApplicationContext());
        /***
         * 跳转到blz
         * @param
         */

        mUserInfoChangeNameBlz=new UserInfoChangeBlz(this);
        net.dunyun.framework.android.mainapp.vo.UserVo newUserVo = new UserVo();
        newUserVo.setNickName(newNmae);
        mUserInfoChangeNameBlz.userInfoChange(userVo, newUserVo);
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
    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        UserInfoChangeNameActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }
    /***
     * callback回调
     * @param提示内容
     */
    @Override
    public void onSuccess(UserVo userVo) {
        DialogUtil.cancelProgressDialog(progressDialog);

        userVo.setUserName(newNmae);
        SharedUtil.remove(context, UserVo.class + "");
        SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
        mainApplication.setUserVo(userVo);
        showToast(getResources().getString(R.string.update_password_success));

        /**查看修改后的本地信息**/
        String m=SharedUtil.getString(context, UserVo.class + "");
        UserVo xx=JsonUtil.parseObject(m, UserVo.class);
        LogUtil.i("修改后本地", "nickname+" + xx.getUserName());

    }
    @Override
    public void onFailed(String result) {
        DialogUtil.cancelProgressDialog(progressDialog);
        showToast(getResources().getString(R.string.login_failed) + "," + result);
    }
}
