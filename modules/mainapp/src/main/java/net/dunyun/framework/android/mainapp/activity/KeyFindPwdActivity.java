package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.DunyunDialog;
import net.dunyun.framework.android.mainapp.widget.WhiteDialog;
import net.dunyun.framework.lock.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 显示开门密码界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyFindPwdActivity extends BaseActivity {

    private Context context = null;
    private LockVo lockVo;
    @Bind(R.id.cb_passwd)
    CheckBox cb_passwd;
    @Bind(R.id.et_keypasswd)
    EditText et_keypasswd;
    private DunyunSDK dunyunSDK;
    private Handler mHandler;
    private String newPassword;
    private String phone;
    private KeyVo keyVo;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_find_pwd);
        ButterKnife.bind(this);
        setTitle("查看开门密码");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");

        keyVo =  BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

        if (mHandler == null) {
            mHandler = new Handler(this.getMainLooper());
        }
    }

    @OnCheckedChanged(R.id.cb_passwd) void check_oldPassWd(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked) {
            //如果选中，显示密码
            et_keypasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            et_keypasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }


    private WhiteDialog whiteDialog;
    private void showDialog(final String title, final String content) {
        KeyFindPwdActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (whiteDialog != null && whiteDialog.isShowing()) {
                    whiteDialog.dismiss();
                    whiteDialog = null;
                }
                whiteDialog = new WhiteDialog(KeyFindPwdActivity.this, title, content, new DialogListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_ok:
                                whiteDialog.dismiss();
                                break;
                        }
                    }

                    @Override
                    public void onItemClick(int position) {

                    }
                });
                whiteDialog.show();
            }
        });
    }

    private void runOnUiDialog(final String content) {
        KeyFindPwdActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(KeyFindPwdActivity.this, content);
            }
        });
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        KeyFindPwdActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submitOnclick() {
        newPassword = et_keypasswd.getText().toString();
        if (newPassword.length() > 0) {
            String passwd = AesUtil.getInstance().decrypt(userVo.getPassWord());
            if(newPassword.equals(passwd)){
                KeyPasswd keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), userVo.getMobile());
                if(keyPasswd == null){
                    ToastUtil.showToast(this, "未保存开门密码");
                }else{
                    showDialog("查询结果", "开门密码为:"+keyPasswd.password);
                }
            }else{
                ToastUtil.showToast(this, "登陆密码输入错误，请重试");
            }
        } else {
            ToastUtil.showToast(this, "请输入用户登陆密码");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
