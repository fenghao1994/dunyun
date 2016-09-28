package net.dunyun.framework.android.mainapp.activity.gate;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GateAddBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GateAddCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.time.JudgeDate;
import net.dunyun.framework.android.mainapp.widget.time.ScreenInfo;
import net.dunyun.framework.android.mainapp.widget.time.WheelMain;
import net.dunyun.framework.lock.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class GateAddActivity extends BaseActivity implements GateAddCallback {

    private Context context = null;
    LoadingDialog loadingDialog;
    private UserVo userVo;
    private String phone;

    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.radio_button0)
    RadioButton radio_button0;
    @Bind(R.id.radio_button1)
    RadioButton radio_button1;
    @Bind(R.id.line_00)
    TextView line_00;
    @Bind(R.id.line_01)
    TextView line_01;
    @Bind(R.id.rl_area)
    LinearLayout rl_area;

    @Bind(R.id.et_area)
    EditText et_area;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_reason)
    EditText et_reason;
    @Bind(R.id.tv_phone)
    TextView tv_phone;

    private String id;
    private GateAddBiz gateAddBiz;

    private boolean fuzhu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);
        baseSetContentView(R.layout.activity_gate_add);
        ButterKnife.bind(this);
        setTitle("申请钥匙");
        context = this;

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        gateAddBiz = new GateAddBiz(this);

        initRadioButton();

        rl_area.setVisibility(View.GONE);

        setRightTwoButton(R.drawable.title_record, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GateAddActivity.this.startActivity(new Intent(GateAddActivity.this, GateApplyActivity.class));
            }
        });
    }

    private void initRadioButton() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radio_button0.getId()) {
                    radio_button0.setBackgroundResource(R.color.light_bg_grey);
                    radio_button0.setTextColor(getResources().getColor(R.color.white));
                    line_00.setBackgroundResource(R.color.light_bg_grey);

                    radio_button1.setBackgroundResource(R.color.white);
                    radio_button1.setTextColor(getResources().getColor(R.color.light_grey));
                    line_01.setBackgroundResource(R.color.light_bg_no_grey);

                    rl_area.setVisibility(View.GONE);

                    fuzhu = true;

                    tv_phone.setText("户主手机号");
                    et_phone.setText("");

                } else {
                    radio_button1.setBackgroundResource(R.color.light_bg_grey);
                    radio_button1.setTextColor(getResources().getColor(R.color.white));
                    line_01.setBackgroundResource(R.color.light_bg_grey);

                    radio_button0.setBackgroundResource(R.color.white);
                    radio_button0.setTextColor(getResources().getColor(R.color.light_grey));
                    line_00.setBackgroundResource(R.color.light_bg_no_grey);

                    rl_area.setVisibility(View.VISIBLE);
                    et_phone.setText(phone);
                    fuzhu = false;

                    tv_phone.setText("申请人手机号");
                }
            }
        });
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        GateAddActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        String reason = et_reason.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        if(!fuzhu){//向物管申请
            if (StrUtil.isEmpty(id)) {
                ToastUtil.showToast(context, "请选择小区");
                return;
            }

            loadingDialog = DialogUtil.showWaitDialog(this);
            gateAddBiz.add1(userVo, id, reason);
        }else{//想户主申请
            loadingDialog = DialogUtil.showWaitDialog(this);
            gateAddBiz.add(userVo, phone, reason);
        }

    }

    @OnClick(R.id.btn_contact)
    void contact() {
        startActivityForResult(new Intent(this, AreaSelectActivity.class), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                id = data.getStringExtra("id");
                et_area.setText(data.getStringExtra("name"));
                break;

            default:
                break;
        }
    }

    @Override
    public void onGateSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "申请成功");
    }

    @Override
    public void onGateFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "申请失败，" + result);
    }
}
