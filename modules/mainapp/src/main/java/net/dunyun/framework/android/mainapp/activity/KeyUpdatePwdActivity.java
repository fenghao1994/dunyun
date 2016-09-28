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
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
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
 * 修改开门密码
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyUpdatePwdActivity extends BaseActivity {

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
        baseSetContentView(R.layout.activity_key_update_pwd);
        ButterKnife.bind(this);
        setTitle("修改开门密码");
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

    DunyunDialog dunyunDialog;

    private void showDialog() {
        dunyunDialog = new DunyunDialog(this, "进行中...", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_exit:
                        dunyunSDK.stopAll();
                        dunyunSDK.destroy();
                        dunyunDialog.dismiss();
                        break;
                }
            }

            @Override
            public void onItemClick(int position) {

            }
        });
        dunyunDialog.show();
    }

    private WhiteDialog whiteDialog;
    private void showDialog(final String title, final String content) {
        KeyUpdatePwdActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (whiteDialog != null && whiteDialog.isShowing()) {
                    whiteDialog.dismiss();
                    whiteDialog = null;
                }
                whiteDialog = new WhiteDialog(KeyUpdatePwdActivity.this, title, content, new DialogListener() {
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

    public void search() {
        if (BluetoothUtil.bluetoothIsOpen(this)) {
            showDialog();
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(this);
            }
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
            }
            dunyunSDK.setAddKey(true);
            dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                @Override
                public void onSuccess(List<DYLockDevice> data) {
                    DYLockDevice dYLockDevice = BluetoothUtil.findBestDevice(data, keyVo.getMacCode());
                    if (dYLockDevice != null) {
                        LogUtil.d(dYLockDevice.getName());
                        dYLockDevice.setName(lockVo.getMacName());
                        dunyunSDK.stopSearchDevices();
                        connect(dYLockDevice, keyVo.getMacCode());
                    }
                }

                @Override
                public void onFailed(String error) {
                    dunyunDialog.dismiss();
                    runOnUiDialog("锁搜索失败");
                }
            });
        }
    }

    private void connect(DYLockDevice dYLockDevice, final String macCode) {
        dunyunSDK.connect(dYLockDevice, new ConnectCallback() {
            @Override
            public void onSuccess(DYLockDevice dyLockDevice) {
                LogUtil.d("---连接成功---");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String userId = UserUtil.parseAddTime(keyVo.getAddTm())+keyVo.getMobile().substring(1);
                        LockUser lockUser = new LockUser();
                        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
                        lockUser.setUserId(userId);
                        lockUser.setOpenLockPwd(newPassword);
                        addUser(lockUser, macCode);
                    }
                }, 120);
            }

            @Override
            public void onFailed(DYLockDevice dyLockDevice, String reason) {
                LogUtil.d("---连接失败---");
                runOnUiDialog("锁连接失败,"+reason);
                dunyunDialog.dismiss();
            }

            @Override
            public void onDescoverServiceFailed(DYLockDevice dyLockDevice) {

            }

            @Override
            public void onDataReceive(byte[] bytes) {

            }

            @Override
            public void onDisconnected(DYLockDevice dyLockDevice) {
                LogUtil.d("---锁已断开---");
                runOnUiDialog("连接已断开");
                dunyunDialog.dismiss();
            }
        });
    }

    private void addUser(final LockUser addLockUser, final String macCode) {
        dunyunSDK.updateOpenLockPwd(addLockUser, newPassword, new Callback<LockUser>() {
            @Override
            public void onSuccess(LockUser data) {
                showDialog("修改成功", "钥匙密码修改成功，开锁密码为:" + newPassword);
                KeyPasswdDbUtil.insert(macCode, addLockUser.getOpenLockPwd(), phone);
                dunyunSDK.destroy();
                dunyunDialog.dismiss();
            }

            @Override
            public void onFailed(String error) {
                showDialog("修改失败", error);
                dunyunDialog.dismiss();
            }
        });
    }

    private void runOnUiDialog(final String content) {
        KeyUpdatePwdActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(KeyUpdatePwdActivity.this, content);
            }
        });
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        KeyUpdatePwdActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submitOnclick() {
        newPassword = et_keypasswd.getText().toString();
        if (newPassword.length() == 6) {
            search();
        } else {
            ToastUtil.showToast(this, "请输入6位数字密码");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dunyunSDK != null){
            dunyunSDK.stopAll();
            dunyunSDK.destroy();
        }
    }
}
