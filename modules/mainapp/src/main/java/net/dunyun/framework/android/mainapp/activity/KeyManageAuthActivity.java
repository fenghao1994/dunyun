package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
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
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;

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
public class KeyManageAuthActivity extends BaseActivity implements UpdateLockKeyCallback {

    private Context context = null;
    private LockVo lockVo;
    private KeyVo keyVo;
    private String phone;
    private String token;

    Intent intent;
    @Bind(R.id.tv_keyname)
    TextView tv_keyname;
    @Bind(R.id.tv_auth_start)
    TextView tv_auth_start;
    @Bind(R.id.tv_auth_end)
    TextView tv_auth_end;
    @Bind(R.id.tv_key_number)
    TextView tv_key_number;
    @Bind(R.id.rl_lock_record)
    RelativeLayout rl_lock_record;
    @Bind(R.id.btn_del)
    Button btn_del;
    private UpdateLockKeyBiz updateLockKeyBiz;
    private DeleteDialog deleteDialog;
    private LoadingDialog loadingDialog;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_manage_auth);
        ButterKnife.bind(this);
        setTitle("钥匙管理");
        phone = SharedUtil.getString(context, "phone");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            token = userVo.getToken();
        }
        updateLockKeyBiz = new UpdateLockKeyBiz(this);

        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");

        intent = new Intent();
        Bundle intentBundle = new Bundle();
        intentBundle.putSerializable("lockVo", lockVo);
        intent.putExtras(intentBundle);
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
        tv_keyname.setText(keyVo.getKeyName());

        tv_auth_start.setText(keyVo.getGrantBdt());
        tv_auth_end.setText(keyVo.getGrantEdt());
        tv_key_number.setText(keyVo.getGrantNum());

        deleteDialog = new DeleteDialog(this, "请确认是否删除", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ok:
                        loadingDialog = DialogUtil.showWaitDialog(KeyManageAuthActivity.this);
                        KeyVo keyVoTemp = new KeyVo();
                        keyVoTemp.setMacCode(keyVo.getMacCode());
                        keyVoTemp.setKeyIndex(keyVo.getKeyIndex());
                        keyVoTemp.setKeyType(keyVo.getKeyType());
                        keyVoTemp.setMobile(keyVo.getMobile());
                        keyVoTemp.setState("0");
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

    @OnClick(R.id.rl_update_key_name)
    void updateKeyName() {
        intent.setClass(this, KeyUpdateNameActivity.class);
        this.startActivityForResult(intent, 0);
    }

    @OnClick(R.id.btn_del)
    void del() {
        deleteDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data != null) {
                tv_keyname.setText(data.getStringExtra("keyname"));
            }
        }
    }

    @OnClick(R.id.rl_lock_record)
    void lockRecord() {
        intent.setClass(this, KeyRecordActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyManageAuthActivity.this.setResult(0, new Intent());
    }

    @Override
    public void onSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除成功");
        KeyManageAuthActivity.this.finish();
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除失败");

    }
}