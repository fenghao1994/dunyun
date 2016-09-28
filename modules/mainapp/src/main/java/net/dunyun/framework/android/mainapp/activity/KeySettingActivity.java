package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CheckBox;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.UpdateLockBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockCallback;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.KeysUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 功能设置
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeySettingActivity extends BaseActivity implements UpdateLockCallback {
    private Context context = null;
    @Bind(R.id.cb_key_setting_auth)
    CheckBox cb_key_setting_auth;
    private LockVo lockVo;
    private String phone;
    private KeyVo keyVo;
    private Intent intent;
    UpdateLockBiz updateLockBiz;
    private LoadingDialog loadingDialog;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_setting);
        ButterKnife.bind(this);
        setTitle("功能设置");

        updateLockBiz = new UpdateLockBiz(this);
        phone = SharedUtil.getString(context, "phone");
        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        intent = new Intent();
        Bundle intentBundle = new Bundle();
        intentBundle.putSerializable("lockVo", lockVo);
        intent.putExtras(intentBundle);

        if ("1".equals(lockVo.getIsGrant())) {
            cb_key_setting_auth.setChecked(true);
        } else {
            cb_key_setting_auth.setChecked(false);
        }
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        Intent intent = new Intent();
        intent.putExtra("keyname", "---");
        this.setResult(1, intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("keyname", "---");
            this.setResult(1, intent);
            this.finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick(R.id.rl_key_setting_more)
    void rl_key_setting_more() {
        if ("0".equals(keyVo.getKeyIndex())) {
            intent.setClass(this, KeySettingMoreActivity.class);
            this.startActivity(intent);
        } else {
            ToastUtil.showToast(this, "锁管理员才能操作");
        }

    }

    @OnClick(R.id.rl_key_setting_update)
    void rl_key_setting_update() {
        if ("0".equals(keyVo.getKeyIndex())) {
            intent.setClass(this, KeyVersionUpdateActivity.class);
            this.startActivity(intent);
        } else {
            ToastUtil.showToast(this, "锁管理员才能操作");
        }
    }

    @OnClick(R.id.cb_key_setting_auth)
    void clickcb_key_setting_auth() {
        if ("0".equals(keyVo.getKeyIndex())) {
            LockVo lockVoTemp = new LockVo();
            lockVoTemp.setMacCode(lockVo.getMacCode());
            String code = "0";
            if (cb_key_setting_auth.isChecked()) {
                code = "1";
            }
            lockVoTemp.setIsGrant(code);
            loadingDialog = DialogUtil.showWaitDialog(context);
            updateLockBiz.updateLock(lockVoTemp, userVo.getToken(), userVo.getMobile());
        } else {
            ToastUtil.showToast(this, "锁管理员才能操作");
        }
    }

    @OnClick(R.id.rl_key_setting_set)
    void rl_key_setting_set() {
        intent.setClass(this, LongOpenActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onUpdateLockSuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);

        KeysUtil.getKeys(context);
    }

    @Override
    public void onUpdateLockFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        if (cb_key_setting_auth.isChecked()) {
            cb_key_setting_auth.setChecked(false);
        } else {
            cb_key_setting_auth.setChecked(true);
        }
    }
}
