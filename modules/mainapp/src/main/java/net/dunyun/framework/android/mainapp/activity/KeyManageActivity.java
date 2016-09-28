package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.UpdateKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateKeyCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.KeysUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 　钥匙管理界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyManageActivity extends BaseActivity implements UpdateKeyCallback,UpdateLockKeyCallback {

    private Context context = null;
    private LockVo lockVo;

    Intent intent;
    @Bind(R.id.tv_keyname)
    TextView tv_keyname;
    @Bind(R.id.cb_lock_notif)
    CheckBox cb_lock_notif;

    @Bind(R.id.cb_lock_share)
    CheckBox cb_lock_share;

    KeyVo keyVo;
    String phone;
    String token;
    //1:接收消息推送,0:不接收消息推送
    boolean checkMsg = false;
    boolean notif = true;

    UpdateKeyBiz updateKeyBiz;
    private LoadingDialog loadingDialog;
    private UpdateLockKeyBiz updateLockKeyBiz;
    private DeleteDialog deleteDialog;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_manage);
        ButterKnife.bind(this);
        setTitle("钥匙管理");
        updateKeyBiz = new UpdateKeyBiz(this);
        updateLockKeyBiz = new UpdateLockKeyBiz(this);
        phone = SharedUtil.getString(context, "phone");
        final String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            token = userVo.getToken();
        }

        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");

        intent = new Intent();
        Bundle intentBundle = new Bundle();
        intentBundle.putSerializable("lockVo", lockVo);
        intent.putExtras(intentBundle);

        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

        tv_keyname.setText(keyVo.getKeyName());

        if ("1".equals(keyVo.getPushFlg())) {
            cb_lock_notif.setChecked(true);
        } else {
            cb_lock_notif.setChecked(false);
        }
        if ("1".equals(keyVo.getIsShare())) {
            cb_lock_share.setChecked(true);
        } else {
            cb_lock_share.setChecked(false);
        }

        deleteDialog = new DeleteDialog(this, "请确认是否删除", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_ok:
                        deleteDialog.dismiss();
                        loadingDialog = DialogUtil.showWaitDialog(KeyManageActivity.this);
                        KeyVo keyVoTemp = new KeyVo();
                        keyVoTemp.setMacCode(keyVo.getMacCode());
                        keyVoTemp.setKeyIndex(keyVo.getKeyIndex());
                        keyVoTemp.setMobile(userVo.getMobile());
                        keyVoTemp.setState("0");
                        keyVoTemp.setKeyType(keyVo.getKeyType());
                        updateLockKeyBiz.updateLockKey(keyVoTemp, userVo);
                        break;
                    case R.id.btn_cancel:
                        deleteDialog.dismiss();
                        break;
                }
            }

            @Override
            public void onItemClick(int position) {

            }
        });
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        this.finish();
    }

    /**
     * 跳转到修改钥匙名称界面
     */
    @OnClick(R.id.rl_update_key_name)
    void updateKeyName() {
        intent.setClass(this, KeyUpdateNameActivity.class);
        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            if(data != null){
                tv_keyname.setText(data.getStringExtra("keyname"));
            }
        }else if(resultCode == 1){
            lockVo = LockDbUtil.query(lockVo.getMacCode(), userVo.getMobile());
            intent = new Intent();
            Bundle intentBundle = new Bundle();
            intentBundle.putSerializable("lockVo", lockVo);
            intent.putExtras(intentBundle);
            keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
        }
    }

    /**
     * 跳转到更改密码界面
     */
    @OnClick(R.id.rl_update_key_passwd)
    void updateKeyPwd() {
        intent.setClass(this, KeyUpdatePwdActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转到显示密码界面
     */
    @OnClick(R.id.rl_find_key_passwd)
    void findKeyPwd() {
        intent.setClass(this, KeyFindPwdActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转到用户管理界面
     */
    @OnClick(R.id.rl_user_manager)
    void userManage() {
        intent.setClass(this, KeyUserManageActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转到记录界面
     */
    @OnClick(R.id.rl_lock_record)
    void lockRecord() {
        intent.setClass(this, KeyRecordActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转到授权界面
     */
    @OnClick(R.id.rl_lock_auth)
    void lockAuth() {
        if("1".equals(lockVo.getIsGrant())){
            intent.setClass(this, KeyAuthActivity.class);
            this.startActivity(intent);
        }else{
            ToastUtil.showToast(context, "锁管理员已关闭远程授权功能");
        }
    }

    /**
     * 跳转到设置界面
     */
    @OnClick(R.id.rl_setting)
    void setting() {
        intent.setClass(this, KeySettingActivity.class);
        this.startActivityForResult(intent,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyManageActivity.this.setResult(0, new Intent());
    }

    /**
     * 消息按钮
     */
    @OnClick(R.id.cb_lock_notif)
    void lockNotif() {
        notif = true;
        loadingDialog = DialogUtil.showWaitDialog(this);
        String check = "0";
        if (cb_lock_notif.isChecked()) {
            check = "1";
        }
        keyVo.setKeyName("");
        keyVo.setState("");
        keyVo.setIsShare("");
        keyVo.setPushFlg(check);
        updateKeyBiz.updateLock(keyVo, token, phone);
    }

    /**
     * 共享门锁记录
     */
    @OnClick(R.id.cb_lock_share)
    void lockShare() {
        notif = false;
        loadingDialog = DialogUtil.showWaitDialog(this);
        String check = "0";
        if (cb_lock_share.isChecked()) {
            check = "1";
        }
        keyVo.setKeyName("");
        keyVo.setState("");
        keyVo.setPushFlg("");
        keyVo.setIsShare(check);
        updateKeyBiz.updateLock(keyVo, token, phone);
    }

    @OnClick(R.id.btn_del)
    void delKey(){
        if("0".equals(keyVo.getKeyIndex())){
            ToastUtil.showToast(context, "锁管理员不能删除，请在锁上执行清空操作");
        }else{
            deleteDialog.show();
        }
    }

    @Override
    public void onKeySuccess(String result) {
        ToastUtil.showToast(context, "成功");
        DialogUtil.cancelWaitDialog(loadingDialog);
    }

    @Override
    public void onKeyFailed(String result) {
        ToastUtil.showToast(context, "失败");
        DialogUtil.cancelWaitDialog(loadingDialog);
        if (notif) {
            if (cb_lock_notif.isChecked()) {
                cb_lock_notif.setChecked(false);
            } else {
                cb_lock_notif.setChecked(true);
            }
        } else {
            if (cb_lock_share.isChecked()) {
                cb_lock_share.setChecked(false);
            } else {
                cb_lock_share.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        lockVo = LockDbUtil.query(lockVo.getMacCode(), userVo.getMobile());
//        intent = new Intent();
//        Bundle intentBundle = new Bundle();
//        intentBundle.putSerializable("lockVo", lockVo);
//        intent.putExtras(intentBundle);
//        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

    }

    @Override
    public void onSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(KeyManageActivity.this, "删除成功");
        KeyManageActivity.this.finish();
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(KeyManageActivity.this, "删除失败,"+result);
    }
}
