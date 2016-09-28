package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockInfo;
import com.psoft.bluetooth.beans.LockParameter;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.AddKeyBiz;
import net.dunyun.framework.android.mainapp.biz.AddKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.util.AddLockLogUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.LockUserUtil;
import net.dunyun.framework.android.mainapp.util.LockUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.WhiteDialog;
import net.dunyun.framework.lock.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加钥匙界面
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class AddKeyActivity extends BaseActivity implements AddKeyCallback {
    @Bind(R.id.iv_img1)
    ImageView iv_img1;
    @Bind(R.id.iv_img2)
    ImageView iv_img2;
    @Bind(R.id.iv_img3)
    ImageView iv_img3;
    @Bind(R.id.iv_img4)
    ImageView iv_img4;
    @Bind(R.id.iv_img5)
    ImageView iv_img5;

    private Context context = null;
    private LoadingDialog loadingDialog = null;

    private AddKeyBiz addKeyBiz;
    private DunyunSDK dunyunSDK;
    private Runnable workRunnable = null;
    private List<DYLockDevice> dyLockDevices;
    private List<DYLockDevice> resultDyLockDevices;
    private DYLockDevice dyLockDevice;
    private String phone;
    private LockUser addLockUser = null;
    private long lastTime = 0l;
    private String costTime = "";
    private boolean isSuccess = false;
    private Handler mHandler;
    UserVo userVo;
    private String address;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }
        if (mHandler == null) {
            mHandler = new Handler(this.getMainLooper());
        }
        context = this;
        baseSetContentView(R.layout.activity_addkey);
        ButterKnife.bind(this);
        setTitle("添加钥匙");
        setRightTwoButton(R.drawable.title_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("----isRunning---"+isRunning);
                if(!isRunning){
                    if (dunyunSDK != null && dunyunSDK.isConnected()) {
                        dunyunSDK.destroy();
                    }
                    addKeyToDevice();
                    isRunning = true;
                }

            }
        });
        addKeyToDevice();
        addKeyBiz = new AddKeyBiz(this);

        address = SharedUtil.getString(context, "locationAddress");

        if(dunyunSDK.bluetoothIsOpen()){
            isRunning = true;
        }
    }

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        com.psoft.framework.android.base.utils.LogUtil.d("active=" + active);
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        com.psoft.framework.android.base.utils.LogUtil.d("active=" + active);
    }

    private WhiteDialog whiteDialog;

    /***
     * 消息提示框
     * @param title 标题
     * @param content 内容
     */
    private void showDialog(final String title, final String content) {
        if (active) {
            AddKeyActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (whiteDialog != null && whiteDialog.isShowing()) {
                        whiteDialog.dismiss();
                        whiteDialog = null;
                    }
                    whiteDialog = new WhiteDialog(AddKeyActivity.this, title, content, new DialogListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_ok:
                                    whiteDialog.dismiss();
                                    if ("添加成功".equals(title)) {
                                        AddKeyActivity.this.finish();
                                    }
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
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            display(number);
            if (number > 5) {
                number = 0;
            } else {
                number++;
            }
            handler.postDelayed(this, 200);
        }
    };
    private int number = 0;

    private void startLoop() {
        number = 0;
        handler.postDelayed(runnable, 200);
    }

    private void stopLoop() {
        handler.removeCallbacks(runnable);
    }

    private void display(int number) {
        switch (number) {
            case 0:
                iv_img1.setVisibility(View.VISIBLE);
                break;
            case 1:
                iv_img2.setVisibility(View.VISIBLE);
                break;
            case 2:
                iv_img3.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv_img4.setVisibility(View.VISIBLE);
                break;
            case 4:
                iv_img5.setVisibility(View.VISIBLE);
                break;
            case 5:
                iv_img1.setVisibility(View.INVISIBLE);
                iv_img2.setVisibility(View.INVISIBLE);
                iv_img3.setVisibility(View.INVISIBLE);
                iv_img4.setVisibility(View.INVISIBLE);
                iv_img5.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void addKeyToDevice() {
        if (BluetoothUtil.bluetoothIsOpen(this)) {
            startLoop();

            dunyunSDK = DunyunSDK.getInstance(this);
            dunyunSDK.setAddKey(true);
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            timer = new Timer();
            task = new TimerTask() {

                @Override
                public void run() {
                    if(dyLockDevices != null && dyLockDevices.size()>0){
                        List<DYLockDevice> resultDyLockDevicesTemp = BluetoothUtil.delNotSetDevice(dyLockDevices);
                        if(resultDyLockDevicesTemp != null && resultDyLockDevicesTemp.size() > 0){
                            resultDyLockDevices = resultDyLockDevicesTemp;
                        }
                        if (resultDyLockDevices != null && resultDyLockDevices.size() > 0) {
                            if (workRunnable == null) {
                                initWorkRunnable(resultDyLockDevices);
                                mCommonHandler.postDelayed(workRunnable, 1000);
                            }
                        }
                    }
                }
            };
            timer.schedule(task, 500, 1000);

            dyLockDevices = null;
            resultDyLockDevices = null;
            dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                @Override
                public void onSuccess(List<DYLockDevice> data) {
                    LogUtil.d(context, "---------data----------");
                    dyLockDevices = data;
                }

                @Override
                public void onFailed(String error) {
                    isRunning = false;
                    showDialog("锁未找到", "未找到设置状态的锁");
                    AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Add, "未检测到设置状态的锁", address);
                    stopLoop();
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (task != null) {
                        task.cancel();
                    }
                    if (dunyunSDK != null)
                        dunyunSDK.destroy();
                }
            });

        }
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        AddKeyActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSuccess) {
            AddKeyActivity.this.setResult(0, new Intent());
        }

        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }

        if (dunyunSDK != null) {
            dunyunSDK.setAddKey(false);
            dunyunSDK.stopAll();
            dunyunSDK.destroy();
        }
    }

    private void initWorkRunnable(final List<DYLockDevice> devices) {
        workRunnable = new Runnable() {
            @Override
            public void run() {
                dunyunSDK.stopSearchDevices();
                if(timer != null)
                timer.cancel();
                if(task != null){
                    task.cancel();
                }

                workRunnable = null;
                if (resultDyLockDevices == null || resultDyLockDevices.size() == 0) {
                    stopLoop();
                    showDialog("添加失败", "未检测到设置状态的锁");
                    AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Add, "未检测到设置状态的锁", address);
                    isRunning = false;
                } else {
                    dyLockDevice = BluetoothUtil.findBestDevice(resultDyLockDevices);
                    lastTime = System.currentTimeMillis();

                    dunyunSDK.connect(dyLockDevice, new ConnectCallback() {
                        @Override
                        public void onSuccess(DYLockDevice dyLockDevice) {
                            LogUtil.d("---连接成功---");
                            //循环发送，直到有反馈回来
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getUsers(true);
                                }
                            }, 120);
                        }

                        @Override
                        public void onFailed(DYLockDevice dyLockDevice, String reason) {
                            LogUtil.d("---连接失败---");
                            showDialog("添加失败", reason);
                            stopLoop();
                            dunyunSDK.destroy();

                            AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Add, reason, address);
                            isRunning = false;
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
                            stopLoop();
                            dunyunSDK.destroy();
                            isRunning = false;
                        }
                    });
                }
            }
        };
    }

    private void runOnUiDialog(final String content) {
        AddKeyActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(AddKeyActivity.this, content);
            }
        });
    }

    /**
     * 获取锁内用户
     */
    public void getUsers(final boolean first) {
        LockUser lockUser = new LockUser();
        lockUser.setUserIndex(0);
        lockUser.setUserId("1234567812345678");
        lockUser.setOpenLockPwd("123456");

        dunyunSDK.getLockUsers(lockUser, new Callback<List<LockUser>>() {
            @Override
            public void onSuccess(final List<LockUser> data) {
                //获取用户信息成功
                addLockUser = BluetoothUtil.getAddLockUser(data, phone);
                if (first) {
                    addUser();
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyVo keyVo = new KeyVo();
                            keyVo.setMacCode(dyLockDevice.getMac());
                            LockUserUtil.addLockUser(keyVo, userVo.getToken(), data, userVo.getMobile());

//                            updateTime(keyVo, addLockUser);
                            getLockInfo(keyVo, addLockUser);
                        }
                    }, 100);
                }
            }

            @Override
            public void onFailed(String error) {
                showDialog("添加失败", "获取锁信息失败" + error);
                stopLoop();
                dunyunSDK.destroy();
                if(first){
                    AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Add, "获取锁信息失败", address);
                }
                isRunning = false;
            }
        });
    }

    /**
     * 更新锁内系统时间
     * @param keyVo
     * @param lockUser
     */
    public void updateTime(final KeyVo keyVo, final LockUser lockUser) {
        String date = DateUtil.getCurrentDate().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        dunyunSDK.updateTime(lockUser, date, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d("同步时间成功");
                getLockInfo(keyVo, lockUser);
            }

            @Override
            public void onFailed(String error) {
                LogUtil.d("同步时间失败");
                dunyunSDK.destroy();
            }
        });
    }

    /***
     * 获取电量
     */
    public void getLockInfo(final KeyVo keyVo, LockUser lockUser) {
        dunyunSDK.getBatteryPower(lockUser, new Callback<LockInfo>() {
            @Override
            public void onSuccess(LockInfo data) {
                dunyunSDK.destroy();

                LockVo lockVo = new LockVo();
                lockVo.setMacCode(keyVo.getMacCode());
                lockVo.setTdState(data.lockStatus);
                lockVo.setPower(data.batteryPower);
                LockUtil.updateLock(lockVo, userVo.getToken(), userVo.getMobile());
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
            }
        });
    }

    /***
     * 添加用户
     */
    private void addUser() {
        isRunning = false;
        if (addLockUser.getUserIndex() == -1) {
            showDialog("添加失败", "锁内用户已满");
            stopLoop();
        } else {
            dunyunSDK.addLockUser(addLockUser, new Callback<LockUser>() {
                @Override
                public void onSuccess(LockUser data) {
                    isSuccess = true;
                    costTime = String.valueOf((System.currentTimeMillis() - lastTime) / 1000);
                    showDialog("添加成功", "钥匙添加成功，开锁密码为:" + addLockUser.getOpenLockPwd());
                    String macCode = dyLockDevice.getMac();
                    KeyPasswdDbUtil.insert(macCode, addLockUser.getOpenLockPwd(), phone);

                    addKeyToWeb();
                    stopLoop();
                }

                @Override
                public void onFailed(String error) {
                    showDialog("添加失败", error);
                    AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Add, error, address);
                    stopLoop();
                    dunyunSDK.destroy();
                }
            });
        }
    }

    /***
     * 添加要后台
     */
    private void addKeyToWeb() {
        LockVo lockVo = new LockVo();
        lockVo.setMacCode(dyLockDevice.getMac());
        lockVo.setTdState("1");
        lockVo.setPower("100");
        lockVo.setIsGrant("1");
        LockDbUtil.insert(lockVo, phone);

        String addTm = UserUtil.getTimeStr(addLockUser.getUserId());

        KeyVo key = new KeyVo();
        key.setKeyName("钥匙" + dyLockDevice.getMac());
        key.setKeyIndex(addLockUser.getUserIndex() + "");
        key.setMacCode(dyLockDevice.getMac());
        key.setKeyType("1");
        key.setState("1");
        key.setMobile(phone);
        key.setAddTm(addTm);
        KeyDbUtil.insert(key, phone);

        KeyVo keyVo = new KeyVo();
        keyVo.setKeyName("钥匙" + dyLockDevice.getMac());
        keyVo.setKeyIndex(addLockUser.getUserIndex() + "");
        keyVo.setMacCode(dyLockDevice.getMac());
        keyVo.setAddTm(addTm);
        keyVo.setMobile(phone);

        String address = SharedUtil.getString(AddKeyActivity.this, "locationAddress");

        addKeyBiz.addKeyToWeb(keyVo, dyLockDevice.getName(), dyLockDevice.getName(), costTime, address, userVo.getToken());
    }

    @Override
    public void onAddKeyToWebSuccess(String result) {
        LogUtil.d("上传钥匙成功...");

        getUsers(false);
    }

    @Override
    public void onAddKeyToWebFailed(String result) {
        //添加失败
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            List<DYLockDevice> resultDyLockDevicesTemp = BluetoothUtil.delNotSetDevice(dyLockDevices);
            if(resultDyLockDevicesTemp != null && resultDyLockDevicesTemp.size() > 0){
                resultDyLockDevices = resultDyLockDevicesTemp;
            }
            if (resultDyLockDevices != null && resultDyLockDevices.size() > 0) {
                if (workRunnable == null) {
                    initWorkRunnable(resultDyLockDevices);
                    mCommonHandler.postDelayed(workRunnable, 100);
                }
            }
        }
    };
}
