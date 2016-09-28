package net.dunyun.framework.android.mainapp.activity.gate;

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

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.activity.KeyAuthActivity;
import net.dunyun.framework.android.mainapp.activity.KeyFindPwdActivity;
import net.dunyun.framework.android.mainapp.activity.KeyRecordActivity;
import net.dunyun.framework.android.mainapp.activity.KeySettingActivity;
import net.dunyun.framework.android.mainapp.activity.KeyUpdateNameActivity;
import net.dunyun.framework.android.mainapp.activity.KeyUpdatePwdActivity;
import net.dunyun.framework.android.mainapp.activity.KeyUserManageActivity;
import net.dunyun.framework.android.mainapp.biz.UpdateKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateKeyCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.DelGrantBiz;
import net.dunyun.framework.android.mainapp.biz.gate.DelGrantCallback;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainBiz;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainCallback;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 　钥匙管理
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class GateManageActivity extends BaseActivity implements UpdateChainCallback,DelGrantCallback {

    private Context context = null;
    private KeyChainVo keyChainVo;

    Intent intent;
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

    private LoadingDialog loadingDialog;
    private DeleteDialog deleteDialog;
    private UserVo userVo;
    private UpdateChainBiz updateChainBiz;
    private DelGrantBiz delGrantBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_gate_manage);
        ButterKnife.bind(this);
        setTitle("钥匙管理");
        updateChainBiz = new UpdateChainBiz(this);
        phone = SharedUtil.getString(context, "phone");
        final String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            token = userVo.getToken();
        }

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        intent = new Intent();
        Bundle intentBundle = new Bundle();
        intentBundle.putSerializable("keyChainVo", keyChainVo);
        intent.putExtras(intentBundle);

        delGrantBiz = new DelGrantBiz(this);

        deleteDialog = new DeleteDialog(this, "请确认是否删除", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_ok:
                        deleteDialog.dismiss();
                        loadingDialog = DialogUtil.showWaitDialog(GateManageActivity.this);
                        delGrantBiz.delete(userVo, keyChainVo.id);
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
        GateManageActivity.this.setResult(2, new Intent());
        GateManageActivity.this.finish();
    }

    //修改门禁钥匙名
    @OnClick(R.id.rl_update_gate_name)
    void updateName() {
        intent.setClass(this, GateUpdateNameActivity.class);
        this.startActivityForResult(intent, 0);
    }

    //钥匙信息
    @OnClick(R.id.rl_gate_info)
    void gateInfo() {
        intent.setClass(this, GateInfoActivity.class);
        this.startActivityForResult(intent, 0);
    }

    //添加用户
    @OnClick(R.id.rl_user_add)
    void userAdd() {
        intent.setClass(this, GateUserAddActivity.class);
        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
        }
    }

    //用户管理
    @OnClick(R.id.rl_user_manager)
    void userManage() {
        intent.setClass(this, GateUserManageActivity.class);
        this.startActivity(intent);
    }

    //记录
    @OnClick(R.id.rl_lock_record)
    void lockRecord() {
        intent.setClass(this, GateRecordActivity.class);
        this.startActivity(intent);
    }

    @OnClick(R.id.rl_lock_auth)
    void lockAuth() {
        intent.setClass(this, GateAuthActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.cb_lock_notif)
    void lockNotif() {
        notif = true;
        loadingDialog = DialogUtil.showWaitDialog(this);
        String check = "0";
        if (cb_lock_notif.isChecked()) {
            check = "1";
        }
        updateChainBiz.updateChain(keyChainVo.id,"",userVo.getToken(),"", check,"");
    }

    @OnClick(R.id.cb_lock_share)
    void lockShare() {
        notif = false;
        loadingDialog = DialogUtil.showWaitDialog(this);
        String check = "0";
        if (cb_lock_share.isChecked()) {
            check = "1";
        }
        updateChainBiz.updateChain(keyChainVo.id,"",userVo.getToken(),check,"","" );
    }

    @OnClick(R.id.btn_del)
    void delKey(){
        deleteDialog.show();
    }

    @Override
    public void onSuccess(String result) {

        ToastUtil.showToast(context, "成功");
        DialogUtil.cancelWaitDialog(loadingDialog);
    }

    @Override
    public void onFailed(String result) {
        ToastUtil.showToast(GateManageActivity.this, "操作失败,"+result);

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
    public void onDelSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除成功");
        GateManageActivity.this.setResult(2, new Intent());
        GateManageActivity.this.finish();
    }

    @Override
    public void onDelFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除失败");
    }
}
