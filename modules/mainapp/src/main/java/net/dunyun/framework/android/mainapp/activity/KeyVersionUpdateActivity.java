package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.utils.Base64Util;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.GetLockVersionBiz;
import net.dunyun.framework.android.mainapp.biz.GetLockVersionCallback;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.FileToCRCUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVersionVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.DunyunDialog;
import net.dunyun.framework.android.mainapp.widget.UpdateDialog;
import net.dunyun.framework.lock.R;

import java.util.List;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 固件刷新
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyVersionUpdateActivity extends BaseActivity implements GetLockVersionCallback {

    private Context context = null;
    private String phone;
    private LockVo lockVo;
    private KeyVo keyVo;
    GetLockVersionBiz getLockVersionBiz;
    private KeyPasswd keyPasswd;
    private DunyunSDK dunyunSDK;
    private DunyunDialog dunyunDialog;
    private Handler mHandler;
    @Bind(R.id.tv_current_version)
    TextView tv_current_version;
    @Bind(R.id.tv_update_version)
    TextView tv_update_version;

    @Bind(R.id.tv_progress)
    TextView tv_progress;

    @Bind(R.id.btn_update)
    Button btn_update;

    LockVersionVo version;

    private boolean getVersion = false;
    private LoadingDialog loadingDialog;

    String fileLocalPath;
    private UserVo userVo;

    UpdateDialog updateDialog;
    private int verserionInt = 210;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_version_update);
        ButterKnife.bind(this);
        setTitle("固件刷新");

        phone = SharedUtil.getString(context, "phone");
        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        getLockVersionBiz = new GetLockVersionBiz(this);

        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            DialogUtil.cancelWaitDialog(loadingDialog);
                            Bundle bundle = msg.getData();
                            fileLocalPath = bundle.getString("file");
                            com.psoft.bluetooth.utils.LogUtil.d("file=====" + fileLocalPath);
                            if (!"null".equals(fileLocalPath) && fileLocalPath.endsWith("bin")) {
                                ToastUtil.showToast(KeyVersionUpdateActivity.this, "下载成功，可以开始更新程序");
                                btn_update.setEnabled(true);
                            } else {
                                ToastUtil.showToast(KeyVersionUpdateActivity.this, "下载失败");
                                btn_update.setEnabled(false);
                            }
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }

        keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), phone);
        if (keyPasswd == null) {
            ToastUtil.showToast(context, "本地未缓存开锁密码");
        } else {
            openLock(keyVo, keyPasswd, false);
        }

        String version = "2";
        if (lockVo.getMacName() != null && lockVo.getMacName().length() == 16) {
            String versionStr3 = Base64Util.getPosition(lockVo.getMacName().charAt(lockVo.getMacName().length() - 5)) + "";
            String versionStr1 = Base64Util.getPosition(lockVo.getMacName().charAt(lockVo.getMacName().length() - 4)) + "";
            String versionStr2 = Base64Util.getPosition(lockVo.getMacName().charAt(lockVo.getMacName().length() - 3)) + "";
            int verserionInt3 = Integer.parseInt(versionStr3) * 100;
            int verserionInt1 = Integer.parseInt(versionStr1) * 10;
            int verserionInt2 = Integer.parseInt(versionStr2);
            verserionInt = verserionInt3 + verserionInt1 + verserionInt2;
            version = Integer.parseInt(versionStr3) + "";
        }
        com.psoft.bluetooth.utils.LogUtil.d("version----"+version+"---verserionInt-"+verserionInt);

        getLockVersionBiz.getLockVersion(version, userVo.getToken(), userVo.getMobile());

        btn_update.setEnabled(false);

        updateDialog = new UpdateDialog(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void showDialog() {
        dunyunDialog = new DunyunDialog(context, "正在连接锁...", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_exit:
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

    private void openLock(final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        if (keyPasswd == null) {
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
            dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                @Override
                public void onSuccess(List<DYLockDevice> data) {
                    DYLockDevice dYLockDevice = BluetoothUtil.findBestDevice(data, keyVo.getMacCode());
                    if (dYLockDevice != null) {
                        dYLockDevice.setName(lockVo.getMacName());
                        com.psoft.bluetooth.utils.LogUtil.d(dYLockDevice.getName());
                        dunyunSDK.stopSearchDevices();
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
                        com.psoft.bluetooth.utils.LogUtil.d("---连接成功---");
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
                        com.psoft.bluetooth.utils.LogUtil.d("---连接失败---");
                        ToastUtil.showToast(context, "锁连接失败," + reason);
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
                        com.psoft.bluetooth.utils.LogUtil.d("---锁已断开---");
                        dunyunDialog.dismiss();
                        ToastUtil.showToast(context, "锁已断开连接");
                    }
                });
            }
        });
    }

    LockUser lockUserTemp;

    public void auth(DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        String userId = UserUtil.parseAddTime(keyVo.getAddTm()) + keyVo.getMobile().substring(1);
        final LockUser lockUser = new LockUser();
        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
        lockUser.setUserId(userId);
        lockUser.setOpenLockPwd(keyPasswd.password);
        dunyunSDK.openLockAuth(lockUser, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                lockUserTemp = lockUser;
                if (!getVersion) {
                    ToastUtil.showToast(context, "连接成功，可以开始设置");
                    readVersion(lockUser);
                } else {
                    byte[] bytes = FileUtil.getByteArrayFromSD(fileLocalPath);
                    updateVersion(bytes, lockUserTemp);
                }
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(context, "握手失败");
                dunyunSDK.destroy();
                dunyunDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dunyunSDK != null ) {
            dunyunSDK.stopAll();
        }
        if (dunyunSDK != null && dunyunSDK.isConnected()) {
            dunyunSDK.destroy();
        }
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        this.finish();
    }

    @OnClick(R.id.btn_download)
    void btn_download() {
        if (version != null) {
            loadingDialog = DialogUtil.showWaitDialog(KeyVersionUpdateActivity.this);
            download(version.url);
        }
    }

    @OnClick(R.id.btn_update)
    void btn_update() {
        getVersion = true;
        openLock(keyVo, keyPasswd, false);
    }

    private void download(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = FileUtil.getFileDownloadDir(KeyVersionUpdateActivity.this);
                String file = FileUtil.downloadFile(fileName, filePath);
                Message msg = new Message();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("file", file);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    public void readVersion(LockUser lockUser) {
        dunyunSDK.readVersion(lockUser, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                data = data.charAt(0) + "." + data.charAt(1) + "." + data.charAt(2);
                tv_current_version.setText("当前版本号:" + data);
            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(KeyVersionUpdateActivity.this, "版本获取失败");
            }
        });
    }

    private String serverVersion = "";

    @Override
    public void onLockVersionSuccess(LockVersionVo version) {
        serverVersion = version.code;
        tv_update_version.setText("最新版本号:" + version.code);
        this.version = version;
        LogUtil.d("固件版本" + version.code);
    }

    @Override
    public void onLockVersionFailed(String result) {
        ToastUtil.showToast(KeyVersionUpdateActivity.this, "获取最新版本失败");
        tv_update_version.setText("最新版本号:" + "未获取到");
    }

    /**
     * 设备更新
     */
    public void updateVersion(byte[] bytes, LockUser lockUser) {
        LogUtil.d(bytes.length + "");
        byte[] crcBytes = FileToCRCUtil.getCRC32(bytes);
        if (updateDialog != null) {
            updateDialog.show();
        }
        dunyunSDK.updateVersion(lockUser, bytes, crcBytes, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
//                tv_progress.setText(data);
                if (updateDialog != null && !updateDialog.isShowing()) {
                    updateDialog.show();
                }

                if (data.contains("更新失败")) {
                    if (updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    ToastUtil.showToast(context, "更新失败");
                } else if (data.contains("更新完成")) {
                    if (updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    ToastUtil.showToast(context, "更新成功");
                } else {
                    updateDialog.update("更新中..." + data + "%");
                }

            }

            @Override
            public void onFailed(String error) {
                ToastUtil.showToast(KeyVersionUpdateActivity.this, "失败");
                if (updateDialog != null && updateDialog.isShowing()) {
                    updateDialog.dismiss();
                }
            }
        });
    }


}
