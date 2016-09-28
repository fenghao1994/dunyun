package net.dunyun.framework.android.mainapp.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockInfo;
import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.callback.ListCallback;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.application.MainApplication;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.db.NearKeyDb;
import net.dunyun.framework.android.mainapp.db.NearKeyDbUtil;
import net.dunyun.framework.android.mainapp.db.YaoKeyDbUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.IntegerUtil;
import net.dunyun.framework.android.mainapp.util.LockUserUtil;
import net.dunyun.framework.android.mainapp.util.LockUtil;
import net.dunyun.framework.android.mainapp.util.RecordsUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chenzp
 * @date 2016/5/26
 * @Copyright:重庆平软科技有限公司
 */
public class BleService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private DunyunSDK dunyunSDK;
    private boolean isRunning;
    private Handler mHandler;
    private String phone;
    private List<LockVo> lockVoList;
    private long lastTime;

    List<NearKeyDb> keyDbs;
    private LockVo lockVo;
    private KeyVo keyVo;
    private LockUser lockUser;
    private UpdateLockKeyBiz updateLockKeyBiz;
    private List<KeyVo> delIndexs;

    private HashMap<String, Integer> deviceHashMap = new HashMap<String, Integer>();
    private UserVo userVo;

    public class LocalBinder extends Binder {
        BleService getService() {
            return BleService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT < 18) {
//            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18
//        } else {
//            Intent innerIntent = new Intent(this, GrayInnerService.class);
//            startService(innerIntent);
//            startForeground(GRAY_SERVICE_ID, new Notification());
//        }

        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            String userVoStr = SharedUtil.getString(this, UserVo.class + "");
            if (userVoStr != null) {
                userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
                phone = userVo.getMobile();
            }
            if (userVo != null) {
                keyDbs = NearKeyDbUtil.list(phone);
                lockVoList = LockDbUtil.query(phone);
                lockVoList = BluetoothUtil.query(lockVoList, phone);
                if (lockVoList.size() > 0 && keyDbs.size() > 0) {
                    search();
                }
            }
            deviceHashMap = new HashMap<String, Integer>();
        }
        updateLockKeyBiz = new UpdateLockKeyBiz(new UpdateLockKeyCallback() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailed(String result) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void search() {
        if (BluetoothUtil.bluetoothIsOpen(this)) {
            isRunning = true;
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(this);
            }
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
            }
            dunyunSDK.setAddKey(false);
            lastTime = System.currentTimeMillis();
            dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                @Override
                public void onSuccess(List<DYLockDevice> data) {

                    if (data != null && data.size() > 0) {
                        if (keyDbs != null && keyDbs.size() > 0) {
                            for (int j = 0; j < data.size(); j++) {
                                for (int i = 0; i < keyDbs.size(); i++) {
                                    if (data.get(j).getName().contains(keyDbs.get(i).macCode)) {
                                        LogUtil.d("" + data.get(j).getName() + ",,," + data.get(j).getRssi());
                                        //TODO
                                        if ((keyDbs.get(i).rssi - 2) < data.get(j).getRssi()) {
                                            if (deviceHashMap.containsKey(data.get(j).getName())) {
                                                int number = deviceHashMap.get(data.get(j).getName());
                                                number++;
                                                deviceHashMap.put(data.get(j).getName(), number);
                                            } else {
                                                deviceHashMap.put(data.get(j).getName(), 1);
                                            }
                                        }
                                    }
                                }
                            }
                            //
                            Iterator iter = deviceHashMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                String key = (String) entry.getKey();
                                Integer val = (Integer) entry.getValue();
                                if (val >= 3) {
                                    findDevice(getDevice(data, key));
                                    deviceHashMap.clear();
                                    break;
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailed(String error) {
                    LogUtil.d("未搜索到锁");
                    isRunning = false;
                    deviceHashMap.clear();

                    mHandler.postDelayed(runnable, 1000);
                }
            });
        }
    }

    private DYLockDevice getDevice(List<DYLockDevice> data, String name) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (name.equals(data.get(i).getName())) {
                    return data.get(i);
                }
            }
        }
        return null;
    }

    private void findDevice(DYLockDevice dYLockDevice) {
        LogUtil.d("---find----" + dYLockDevice.getName());
        if (dYLockDevice != null) {
            dunyunSDK.stopSearchDevices();
            if (lockVoList != null && lockVoList.size() > 0) {
                for (LockVo lock : lockVoList) {
                    if (lock.getMacCode().equals(dYLockDevice.getMac())) {
                        lockVo = lock;
                        break;
                    }
                }
            }
            if (lockVo != null) {
                keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
            }
            if (keyVo != null) {
                KeyPasswd keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), phone);
                if (keyPasswd != null) {
                    dYLockDevice.setName(lockVo.getMacName());
                    connectDevice(dYLockDevice, keyVo, keyPasswd, true);
                } else {
                    LogUtil.d(keyVo.getKeyName() + "未保存开锁密码");
                    isRunning = false;
                }
            } else {
                LogUtil.d(keyVo.getKeyName() + "未保存开锁密码");
                isRunning = false;
            }
        }
    }

    private void connectDevice(final DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, boolean flag) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                dunyunSDK.connect(dyLockDevice, new ConnectCallback() {
                    @Override
                    public void onSuccess(final DYLockDevice dyLockDevice) {
                        com.psoft.bluetooth.utils.LogUtil.d("---连接成功---");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                auth(dyLockDevice, keyVo, keyPasswd);
                            }
                        }, 120);
                    }

                    @Override
                    public void onFailed(DYLockDevice dyLockDevice, String reason) {
                        com.psoft.bluetooth.utils.LogUtil.d("---连接失败---");
                        isRunning = false;

                        mHandler.postDelayed(runnable, 1000);
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
                        isRunning = false;

                    }
                });
            }
        });
    }

    public void auth(DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd) {
        String userId = UserUtil.parseAddTime(keyVo.getAddTm()) + keyVo.getMobile().substring(1);
        lockUser = new LockUser();
        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
        lockUser.setUserId(userId);
        lockUser.setOpenLockPwd(keyPasswd.password);
        dunyunSDK.openLockAuth(lockUser, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                isRunning = false;
                open(lockUser, keyVo, false);
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
                isRunning = false;
                com.psoft.bluetooth.utils.LogUtil.d("---握手失败---");
                mHandler.postDelayed(runnable, 1000);
            }
        });
    }

    private void open(final LockUser lockUser, final KeyVo keyVo, final boolean flag) {
        dunyunSDK.openLock(lockUser, new Callback<LockInfo>() {
            @Override
            public void onSuccess(LockInfo data) {

                mHandler.postDelayed(runnable, 20 * 1000);

                long time = (System.currentTimeMillis() - lastTime);
                LogUtil.d("开锁耗时：" + time);
                isRunning = false;
                getRecords(lockUser, keyVo);
                if (flag) {
                    KeyPasswdDbUtil.insert(keyVo.getMacCode(), lockUser.getOpenLockPwd(), userVo.getMobile());
                }
                if ("2".equals(keyVo.getKeyType())) {
                    KeyVo keyVoTemp = new KeyVo();
                    try {
                        int grantNum = Integer.parseInt(keyVo.getGrantNum());
                        grantNum = grantNum - 1;
                        keyVoTemp.setGrantNum(grantNum + "");

                        KeyDbUtil.update(grantNum + "", keyVo.getMobile(), keyVo.getMacCode(), userVo.getMobile());

                        keyVoTemp.setMacCode(keyVo.getMacCode());
                        keyVoTemp.setKeyIndex(keyVo.getKeyIndex());
                        keyVoTemp.setKeyType(keyVo.getKeyType());
                        keyVoTemp.setMobile(keyVo.getMobile());
                        updateLockKeyBiz.updateLockKey(keyVoTemp, userVo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(String error) {
                runOnUiDialog("开锁失败，密码错误或者" + error);
                dunyunSDK.destroy();

                isRunning = false;

                mHandler.postDelayed(runnable, 2000);
            }
        });
    }

    /**
     * 获取锁内用户
     */
    public void getUsers(final KeyVo keyVo, final LockUser lockUser, final boolean first) {
        dunyunSDK.getLockUsers(lockUser, new Callback<List<LockUser>>() {
            @Override
            public void onSuccess(List<LockUser> data) {
                if (first) {
                    delIndexs = new ArrayList<KeyVo>();
                    findDelIndexs(keyVo, lockUser, data);
                } else {//同步锁用户
                    updateTime(keyVo, lockUser);
                    LockUserUtil.addLockUser(keyVo, userVo.getToken(), data, userVo.getMobile());
                }
                //获取用户信息成功
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < data.size(); i++) {
                    sb.append(data.get(i).toString());
                }
                LogUtil.d(sb.toString());
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
            }
        });
    }

    private void findDelIndexs(final KeyVo keyVo, LockUser lockUser, List<LockUser> data) {
        delIndexs = BluetoothUtil.findKeyVosDelIndexs(lockVo);
        if (delIndexs.size() > 0 && data.size() > 0) {
            for (int i = 0; i < delIndexs.size(); i++) {
                boolean isfind = false;
                for (LockUser user : data) {
                    String addTm = IntegerUtil.toHexString(delIndexs.get(i).getAddTm());
                    if (delIndexs.get(i).getKeyIndex().equals(user.getUserIndex() + "")
                            && addTm.equals(UserUtil.getTime(user.getUserId()))) {
                        isfind = true;
                        break;
                    }
                }
                if (!isfind) {
                    delIndexs.remove(i);
                }
            }
        }
        if (delIndexs.size() > 0) {
            int index = Integer.parseInt(delIndexs.get(0).getKeyIndex());
            delUser(keyVo, lockUser, index);
        } else {
            getUsers(keyVo, lockUser, false);
        }
    }

    public void delUser(final KeyVo keyVo, final LockUser lockUser, int delIndex) {
        dunyunSDK.delLockUser(lockUser, delIndex, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                LogUtil.d("删除" + data);
                if (delIndexs.size() > 0) {
                    delIndexs.remove(0);
                }
                if (delIndexs.size() > 0) {
                    int index = Integer.parseInt(delIndexs.get(0).getKeyIndex());
                    delUser(keyVo, lockUser, index);
                } else {
                    //读取所内用户
                    getUsers(keyVo, lockUser, false);
                }
            }

            @Override
            public void onFailed(String error) {
                com.psoft.framework.android.base.utils.LogUtil.d("删除失败");
                dunyunSDK.destroy();
            }
        });
    }

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

    private void runOnUiDialog(final String content) {
        LogUtil.d(content);
    }

    /***
     * 开关门记录
     */
    public void getRecords(final LockUser lockUser, final KeyVo keyVo) {
        dunyunSDK.readRecords(lockUser, new ListCallback<LockRecord>() {
            @Override
            public void onSuccess(List<LockRecord> data) {

                getUsers(keyVo, lockUser, true);

                String str = "";
                if (data != null && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        str += data.get(i);
                    }
                    LogUtil.d(str);
                    String operType = "点击开锁";
                    String address = SharedUtil.getString(BleService.this, "locationAddress");

                    RecordsUtil.addLockRecord(keyVo, "3", "1", "0", userVo.getToken(), data, userVo.getMobile(), operType, address);
                }
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
            }
        });
    }

    @Override
    public void onDestroy() {
        LogUtil.d(BleService.class, "BleService.class ------onDestroy");
        mHandler.removeCallbacks(runnable);
        if (dunyunSDK != null)
            dunyunSDK.destroy();
        super.onDestroy();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            search();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private final static int GRAY_SERVICE_ID = 1001;

//    public static class GrayInnerService extends Service {
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            startForeground(GRAY_SERVICE_ID, new Notification());
//            stopForeground(true);
//            stopSelf();
//            return super.onStartCommand(intent, flags, startId);
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//
//    }
}


