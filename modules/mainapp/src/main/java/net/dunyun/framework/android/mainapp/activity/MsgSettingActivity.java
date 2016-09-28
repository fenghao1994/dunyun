package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;

import com.psoft.bluetooth.callback.Callback;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.UserInfoChangeBlz;
import net.dunyun.framework.android.mainapp.biz.UserInfoChangeCallback;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 　消息提醒界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class MsgSettingActivity extends BaseActivity implements UserInfoChangeCallback {

    private Context context = null;
    private UserVo userVo  = null;
    @Bind(R.id.cb_notif)
    CheckBox cb_notif;
    private LoadingDialog loadingDialog;
    UserInfoChangeBlz userInfoChangeBlz;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_msg_setting);
        ButterKnife.bind(this);
        setTitle("消息提醒");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }
        userInfoChangeBlz = new UserInfoChangeBlz(this);

        if("1".equals(userVo.getIsPush())){
            cb_notif.setChecked(true);
        }else{
            cb_notif.setChecked(false);
        }
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.cb_notif)
    void lockNotif() {
        loadingDialog = DialogUtil.showWaitDialog(this);
        String check = "0";
        if (cb_notif.isChecked()) {
            check = "1";
        }
        UserVo newUserVo = new UserVo();
        newUserVo.setIsPush(check);
        userInfoChangeBlz.userInfoChange(userVo, newUserVo);
    }

    @Override
    public void onSuccess(UserVo userVo1) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        UserUtil.login(context, mainApplication, userVo, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                String userVoStr = SharedUtil.getString(context, UserVo.class + "");
                if(userVoStr != null){
                    userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
                    phone = userVo.getMobile();
                }
            }

            @Override
            public void onFailed(String s) {

            }
        });
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "操作失败");
        if(cb_notif.isChecked()){
            cb_notif.setChecked(false);
        }else {
            cb_notif.setChecked(true);
        }
    }
}
