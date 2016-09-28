package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.activity.KeyRecordActivity;
import net.dunyun.framework.android.mainapp.activity.KeyUpdateNameActivity;
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
public class GateManageAuthActivity extends BaseActivity implements UpdateChainCallback,DelGrantCallback {

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
    private DeleteDialog deleteDialog;
    private LoadingDialog loadingDialog;
    private UserVo userVo;
    private KeyChainVo keyChainVo;
    private UpdateChainBiz updateChainBiz;
    private DelGrantBiz delGrantBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_manage_auth);
        ButterKnife.bind(this);
        setTitle("钥匙管理");
        phone = SharedUtil.getString(context, "phone");
        final String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            token = userVo.getToken();
        }
        updateChainBiz = new UpdateChainBiz(this);

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        intent = new Intent();
        Bundle intentBundle = new Bundle();
        intentBundle.putSerializable("keyChainVo", keyChainVo);
        intent.putExtras(intentBundle);
        tv_keyname.setText(keyChainVo.getChainName());

        tv_auth_start.setText(keyChainVo.getGrantBdt());
        tv_auth_end.setText(keyChainVo.getGrantEdt());
        tv_key_number.setText(keyChainVo.getGrantNum());

        delGrantBiz = new DelGrantBiz(this);

        deleteDialog = new DeleteDialog(this, "请确认是否删除", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ok:
                        loadingDialog = DialogUtil.showWaitDialog(GateManageAuthActivity.this);
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
        this.finish();
    }

    @OnClick(R.id.rl_update_key_name)
    void updateKeyName() {
        intent.setClass(this, GateUpdateNameActivity.class);
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
        intent.setClass(this, GateRecordActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDelSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除成功");
        GateManageAuthActivity.this.setResult(2, new Intent());
        GateManageAuthActivity.this.finish();
    }

    @Override
    public void onDelFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "删除失败");
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onFailed(String result) {

    }
}