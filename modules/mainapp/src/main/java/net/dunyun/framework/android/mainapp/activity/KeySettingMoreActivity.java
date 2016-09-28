package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockParameter;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.DunyunDialog;
import net.dunyun.framework.lock.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能设置
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class KeySettingMoreActivity extends BaseActivity {

    private Context context = null;
    private String phone;
    private LockVo lockVo;
    private KeyVo keyVo;
    @Bind(R.id.cb_key_setting_add)
    CheckBox cb_key_setting_add;
    @Bind(R.id.cb_key_setting_delete)
    CheckBox cb_key_setting_delete;
    @Bind(R.id.cb_key_setting_passwd)
    CheckBox cb_key_setting_passwd;
    @Bind(R.id.cb_key_setting_more_clear)
    CheckBox cb_key_setting_more_clear;
    @Bind(R.id.cb_key_setting_more_power)
    CheckBox cb_key_setting_more_power;
    private DunyunSDK dunyunSDK;
    private Handler mHandler;

    private LockUser lockUser;
    private KeyPasswd keyPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_setting_more);
        ButterKnife.bind(this);
        setTitle("功能使能");

        setRightTwoButton(R.drawable.title_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dunyunSDK.isConnected()) {
                    dunyunSDK.destroy();
                }
                openLock(keyVo, keyPasswd, false);
            }
        });

        phone = SharedUtil.getString(context, "phone");
        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

        if (mHandler == null) {
            mHandler = new Handler(context.getMainLooper());
        }

        cb_key_setting_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dunyunSDK.isConnected()){
                    LockParameter lockParameter = new LockParameter();
                    lockParameter.setADD_KEY(LockParameter.OPEN);
                    if(cb_key_setting_add.isChecked()){
                        openStatus(lockUser, lockParameter, true);
                    }else{
                        openStatus(lockUser, lockParameter, false);
                    }
                }else{
                    ToastUtil.showToast(context, "锁未连接");
                }
            }
        });

        cb_key_setting_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dunyunSDK.isConnected()){
                    LockParameter lockParameter = new LockParameter();
                    lockParameter.setDEL_KEY(LockParameter.OPEN);
                    if(cb_key_setting_delete.isChecked()){
                        openStatus(lockUser, lockParameter, true);
                    }else{
                        openStatus(lockUser, lockParameter, false);
                    }
                }else{
                    ToastUtil.showToast(context, "锁未连接");
                }

            }
        });

        cb_key_setting_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dunyunSDK.isConnected()){
                    LockParameter lockParameter = new LockParameter();
                    lockParameter.setKEYBOARD(LockParameter.OPEN);
                    if(cb_key_setting_passwd.isChecked()){
                        openStatus(lockUser, lockParameter, true);
                    }else{
                        openStatus(lockUser, lockParameter, false);
                    }
                }else{
                    ToastUtil.showToast(context, "锁未连接");
                }

            }
        });

        cb_key_setting_more_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dunyunSDK.isConnected()){
                    LockParameter lockParameter = new LockParameter();
                    lockParameter.setCLEAR_KEY(LockParameter.OPEN);
                    if(cb_key_setting_more_clear.isChecked()){
                        openStatus(lockUser, lockParameter, true);
                    }else{
                        openStatus(lockUser, lockParameter, false);
                    }
                }else{
                    ToastUtil.showToast(context, "锁未连接");
                }


            }
        });
        cb_key_setting_more_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dunyunSDK.isConnected()){
                    LockParameter lockParameter = new LockParameter();
                    lockParameter.setPOWER(LockParameter.OPEN);
                    if(cb_key_setting_more_power.isChecked()){
                        openStatus(lockUser, lockParameter, true);
                    }else{
                        openStatus(lockUser, lockParameter, false);
                    }
                }else{
                    ToastUtil.showToast(context, "锁未连接");
                }
            }
        });

        keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), phone);
        if(keyPasswd == null){
            ToastUtil.showToast(context, "本地未缓存开锁密码");
        }else{
            openLock(keyVo, keyPasswd, false);
        }
    }

    private void openLock(final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        if(keyPasswd == null){
            ToastUtil.showToast(context, "本地未缓存开锁密码");
            return;
        }
        if (BluetoothUtil.bluetoothIsOpen(context)) {
            showDialog();
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(context);
            }
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
            }
            dunyunSDK.setAddKey(false);
            dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                @Override
                public void onSuccess(List<DYLockDevice> data) {
                    DYLockDevice dYLockDevice = BluetoothUtil.findBestDevice(data, keyVo.getMacCode());
                    if (dYLockDevice != null) {
                        LogUtil.d(dYLockDevice.getName());
                        dunyunSDK.stopSearchDevices();
                        dYLockDevice.setName(lockVo.getMacName());
                        connectDevice(dYLockDevice, keyVo, keyPasswd, flag);
                    }
                }

                @Override
                public void onFailed(String error) {
                    dunyunDialog.dismiss();
                    ToastUtil.showToast(context, "未搜索到锁");
                }
            });
        }
    }

    private void connectDevice(final DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                dunyunSDK.connect(dyLockDevice, new ConnectCallback() {
                    @Override
                    public void onSuccess(final DYLockDevice dyLockDevice) {
                        LogUtil.d("---连接成功---");
                        dunyunDialog.dismiss();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                auth(dyLockDevice, keyVo, keyPasswd, flag);
                            }
                        }, 120);
                    }

                    @Override
                    public void onFailed(DYLockDevice dyLockDevice, String reason) {
                        LogUtil.d("---连接失败---");
                        ToastUtil.showToast(context, "锁连接失败,"+reason);
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
                        dunyunDialog.dismiss();
                        ToastUtil.showToast(context, "锁已断开连接");
                    }
                });
            }
        });
    }

    public void auth(DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        String userId = UserUtil.parseAddTime(keyVo.getAddTm()) + keyVo.getMobile().substring(1);
        final LockUser lockUser = new LockUser();
        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
        lockUser.setUserId(userId);
        lockUser.setOpenLockPwd(keyPasswd.password);
        dunyunSDK.openLockAuth(lockUser, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                readStatus(lockUser);
                ToastUtil.showToast(context, "连接成功，可以开始设置");
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(context, "握手失败");
                dunyunSDK.destroy();
                dunyunDialog.dismiss();
            }
        });
    }

    public void readStatus(LockUser lockUser){
        this.lockUser = lockUser;
        dunyunSDK.readStatus(lockUser, new Callback<LockParameter>() {
            @Override
            public void onSuccess(LockParameter data) {
                displayCheckbox(data);
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(context, "读取状态失败");
            }
        });
    }

    private void openStatus(final LockUser lockUser, LockParameter lockParameter, boolean open){

        dunyunSDK.setStatus(lockUser, lockParameter, new Callback<LockParameter>() {
            @Override
            public void onSuccess(LockParameter data) {
                ToastUtil.showToast(context, "设置成功");
                readStatus(lockUser);
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(context, "设置失败");
            }
        }, open);
    }

    private void displayCheckbox(LockParameter lockParameter){
        if(lockParameter.getADD_KEY() == LockParameter.OPEN){
            cb_key_setting_add.setChecked(true);
        }else{
            cb_key_setting_add.setChecked(false);
        }
        if(lockParameter.getDEL_KEY() == LockParameter.OPEN){
            cb_key_setting_delete.setChecked(true);
        }else{
            cb_key_setting_delete.setChecked(false);
        }
        if(lockParameter.getCLEAR_KEY() == LockParameter.OPEN){
            cb_key_setting_more_clear.setChecked(true);
        }else{
            cb_key_setting_more_clear.setChecked(false);
        }
        if(lockParameter.getKEYBOARD() == LockParameter.OPEN){
            cb_key_setting_passwd.setChecked(true);
        }else{
            cb_key_setting_passwd.setChecked(false);
        }
        if(lockParameter.getPOWER() == LockParameter.OPEN){
            cb_key_setting_more_power.setChecked(true);
        }else{
            cb_key_setting_more_power.setChecked(false);
        }
    }

    DunyunDialog dunyunDialog;

    private void showDialog() {
        dunyunDialog = new DunyunDialog(context, "正在连接锁...", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_exit:
                        if (dunyunSDK != null) {
                            dunyunSDK.stopAll();
                            dunyunSDK.destroy();
                        }
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

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dunyunSDK != null){
            dunyunSDK.stopAll();
        }
        if (dunyunSDK != null && dunyunSDK.isConnected()) {
            dunyunSDK.destroy();
        }
    }
}
