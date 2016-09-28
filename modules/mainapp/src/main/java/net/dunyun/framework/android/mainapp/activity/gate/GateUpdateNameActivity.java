package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.biz.UpdateKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainBiz;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainCallback;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *修改钥匙名称
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class GateUpdateNameActivity extends BaseActivity implements UpdateChainCallback{

    private Context context = null;
    private LockVo lockVo;
    @Bind(R.id.et_keyname) EditText et_keyname;

    UpdateKeyBiz updateKeyBiz;
    private LoadingDialog loadingDialog;
    String keyName;
    private UpdateLockKeyBiz updateLockKeyBiz;
    private UserVo userVo;
    private String phone;
    private UpdateChainBiz updateChainBiz;
    private KeyChainVo keyChainVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_update_name);
        ButterKnife.bind(this);
        setTitle("修改钥匙名称");

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        et_keyname.setText(keyChainVo.getChainName());

        updateChainBiz = new UpdateChainBiz(this);
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        GateUpdateNameActivity.this.finish();
    }

    @OnClick(R.id.btn_submit) void submit(){
        keyName = et_keyname.getText().toString().trim();
        if (StrUtil.isEmpty(keyName)) {
            ToastUtil.showToast(context, getResources().getString(R.string.key_name_input_tips));
            return;
        }
        loadingDialog = DialogUtil.showWaitDialog(context, "正在修改...");
        updateChainBiz.updateChain(keyChainVo.id,keyName,userVo.getToken(),"","","");
    }

    @Override
    public void onSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "修改成功");
        Intent intent = new Intent();
        intent.putExtra("keyname", keyName);
        this.setResult(0, intent);
        this.finish();
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, result);
    }
}
