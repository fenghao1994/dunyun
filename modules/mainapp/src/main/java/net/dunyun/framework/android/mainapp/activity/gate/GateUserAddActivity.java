package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.GateInfoAdapter;
import net.dunyun.framework.android.mainapp.biz.gate.GateAuthBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GateAuthCallback;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.DoorsVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateInfoVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class GateUserAddActivity extends BaseActivity implements GateAuthCallback {

    private Context context = null;
    private UserVo userVo;
    private String phone;
    private KeyChainVo keyChainVo;
    @Bind(R.id.et_area)
    EditText et_area;

    @Bind(R.id.et_phone)
    EditText et_phone;
    private LoadingDialog loadingDialog;
    private GateAuthBiz gateAuthBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        baseSetContentView(R.layout.activity_gate_user_add);
        ButterKnife.bind(this);
        setTitle("添加家庭成员");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");
        et_area.setText(phone);

        gateAuthBiz = new GateAuthBiz(this);
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        GateUserAddActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submit() {

        String phone = et_phone.getText().toString().trim();
        if (StrUtil.isEmpty(phone)) {
            ToastUtil.showToast(context, "请输入手机号");
            return;
        }
        loadingDialog = DialogUtil.showWaitDialog(this);
        gateAuthBiz.auth(userVo, keyChainVo.id, phone, "1","", "", "");
    }

    @Override
    public void onSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "添加成功");
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "添加失败,"+result);
    }
}
