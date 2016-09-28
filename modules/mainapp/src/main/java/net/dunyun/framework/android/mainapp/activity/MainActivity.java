package net.dunyun.framework.android.mainapp.activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.igexin.sdk.PushManager;
import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockInfo;
import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.callback.ListCallback;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.AppUtil;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.application.MainApplication;
import net.dunyun.framework.android.mainapp.biz.AddKeyBiz;
import net.dunyun.framework.android.mainapp.biz.AddKeyCallback;
import net.dunyun.framework.android.mainapp.biz.DownloadCallback;
import net.dunyun.framework.android.mainapp.biz.GetVersionNewBiz;
import net.dunyun.framework.android.mainapp.biz.GetVersionNewCallback;
import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyCacheDb;
import net.dunyun.framework.android.mainapp.db.KeyCacheDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.db.YaoKeyDb;
import net.dunyun.framework.android.mainapp.db.YaoKeyDbUtil;
import net.dunyun.framework.android.mainapp.fragment.AreaFragment;
import net.dunyun.framework.android.mainapp.fragment.BaseFragment;
import net.dunyun.framework.android.mainapp.fragment.HomeFragment;
import net.dunyun.framework.android.mainapp.fragment.KeysFragment;
import net.dunyun.framework.android.mainapp.fragment.SystemFragment;
import net.dunyun.framework.android.mainapp.fund.GestureVerifyActivity;
import net.dunyun.framework.android.mainapp.fund.common.Constants;
import net.dunyun.framework.android.mainapp.service.BleService;
import net.dunyun.framework.android.mainapp.util.AddLockLogUtil;
import net.dunyun.framework.android.mainapp.util.AesEncryptionUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.Base64Decoder;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.BuglyUtil;
import net.dunyun.framework.android.mainapp.util.FileToCRCUtil;
import net.dunyun.framework.android.mainapp.util.IntegerUtil;
import net.dunyun.framework.android.mainapp.util.LockUserUtil;
import net.dunyun.framework.android.mainapp.util.LockUtil;
import net.dunyun.framework.android.mainapp.util.MediaPlayerUtil;
import net.dunyun.framework.android.mainapp.util.RecordsUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.util.VersionUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.VersionVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.DunyunDialog;
import net.dunyun.framework.android.mainapp.widget.WhiteDialog;
import net.dunyun.framework.lock.R;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity implements UpdateLockKeyCallback, SensorEventListener, LoginCallback {

    // 定义Fragment页面
    private HomeFragment fragmentShop;
    private AreaFragment fragmentCommunity;
    private KeysFragment fragmentKeys;
    private SystemFragment fragmentMe;

    // 定义布局对象
    @Bind(R.id.layout_shop)
    FrameLayout shop;
    @Bind(R.id.layout_community)
    FrameLayout community;
    @Bind(R.id.layout_keys)
    FrameLayout keys;
    @Bind(R.id.layout_me)
    FrameLayout me;
    @Bind(R.id.frame_welcome)
    FrameLayout welcome;

    @Bind(R.id.image_shop)
    ImageView shopIv;
    @Bind(R.id.image_community)
    ImageView communityIv;
    @Bind(R.id.image_keys)
    ImageView keysIv;
    @Bind(R.id.image_me)
    ImageView meIv;
    @Bind(R.id.image_open)
    ImageView image_open;
    @Bind(R.id.open_txt)
    TextView open_txt;
    private DunyunSDK dunyunSDK;
    List<LockVo> lockVoList;
    KeyVo keyVo;
    String phone;
    private Handler mHandler;

    private boolean isClicked = false;
    private LockUser lockUser;

    private DunyunDialog dunyunDialog;
    private WhiteDialog whiteDialog;
    private List<KeyVo> delIndexs;
    private UpdateLockKeyBiz updateLockKeyBiz;

    private LockVo lockVo;
    private Context context;
    private LoadingDialog mLoadingDialog;
    private SensorManager sensorManager;
    UserVo userVo;
    private Vibrator mVibrator;

    private KeyPasswd keyPasswd = null;
    private DYLockDevice mdYLockDevice = null;

    private boolean auth = true;
    private Map<KeyVo, KeyPasswd> keyMaps = null;
    private LoginBiz loginBiz;
    private String pushId;
    private String passwd;
    private MainApplication mainApplication;
    private AMapLocationClient mlocationClient;

    HashSet<String> uniqueKeySet;
    private String address;

    private long time;
    private static final String PICASSO_CACHE = "picasso-cache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost1);

        context = MainActivity.this;
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }
        loginBiz = new LoginBiz(this);
        pushId = PushManager.getInstance().getClientid(context);
        ButterKnife.bind(this);

//        stopService(new Intent(context, BleService.class));

        mainApplication = MainApplication.getInstance();
        clickAtShop();

        login();

        updateLockKeyBiz = new UpdateLockKeyBiz(this);

        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            open_txt.setTextColor(getResources().getColor(R.color.light_grey));
                            image_open.setImageResource(R.drawable.tab_icon_open);
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showDialog("温馨提示", "您的手机不支持手机开锁");
        } else {
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(this);
            }

            isFinding = false;
            if (userVo != null) {
                lockVoList = LockDbUtil.query(phone);
                lockVoList = BluetoothUtil.query(lockVoList, phone);
                if (lockVoList.size() > 0) {
                    keyMaps = BluetoothUtil.getKeyVos(phone, lockVoList);
                    uniqueKeySet = new HashSet<String>();
                    if (keyMaps != null && !keyMaps.isEmpty()) {
                        for (Map.Entry<KeyVo, KeyPasswd> entry : keyMaps.entrySet()) {
                            KeyVo keyVoT = (KeyVo) entry.getKey();
                            KeyPasswd keyPasswdT = (KeyPasswd) entry.getValue();
                            if (keyPasswdT != null) {
                                uniqueKeySet.add(keyVoT.getMacCode());
                            }
                        }
                    }

                    if (uniqueKeySet != null && uniqueKeySet.size() == 1) {
                        String mac = uniqueKeySet.iterator().next();
                        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(Base64Decoder.decodeToHex(mac));
                        DYLockDevice dyLockDevice = new DYLockDevice();
                        dyLockDevice.setMac(mac);
                        dyLockDevice.setBluetoothDevice(bluetoothDevice);
                        findDevice(dyLockDevice, false);
                    } else if (uniqueKeySet != null && uniqueKeySet.size() > 1) {
                        search(false);
                    }
                }
            }
        }

        registerNetworkReceiver();
        updateVersion();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);

        if (SharedUtil.getString(this, "latitude") != null) {
            latitude = SharedUtil.getString(this, "latitude");
        }
        if (SharedUtil.getString(this, "longitude") != null) {
            longitude = SharedUtil.getString(this, "longitude");
        }
        location();

        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        lastTime = System.currentTimeMillis();

        String PASSWD_OPEN = SharedUtil.getString(this, Constants.PASSWD_OPEN);
        if("yes".equals(PASSWD_OPEN)){
            this.startActivityForResult(new Intent(this, GestureVerifyActivity.class),0);
        }
        PushManager.getInstance().initialize(this.getApplicationContext());
        File cache = new File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE);
        cache.delete();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mainApplication.setIsBle(false);
        }else {
            mainApplication.setIsBle(true);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                welcome.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private long lastTime = 0;
    private boolean yaoyao = false;

    /***
     * 搜索附近钥匙
     * @param yao 是否为摇一摇开锁
     */
    private void search(final boolean yao) {
        yaoyao = yao;
        if (BluetoothUtil.bluetoothIsOpen(this)) {

            if (Build.VERSION.SDK_INT >= 23) {
                if (isGpsEnable(this)) {

                } else {
                    Toast.makeText(this, "安卓6.0以上系统，必须打开GPS才可以使用蓝牙", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(this);
            }
            if (dunyunSDK.isConnected()) {
                Toast.makeText(MainActivity.this, "锁数据读取中...请稍后再试", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
            }
            dunyunSDK.setAddKey(false);

            //用户有单个钥匙
            if (uniqueKeySet != null && uniqueKeySet.size() == 1) {
                String mac = uniqueKeySet.iterator().next();
                BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(Base64Decoder.decodeToHex(mac));
                DYLockDevice dyLockDevice = new DYLockDevice();
                dyLockDevice.setMac(mac);
                dyLockDevice.setBluetoothDevice(bluetoothDevice);
                findDevice(dyLockDevice, false);
            } else if (uniqueKeySet != null && uniqueKeySet.size() > 1) {
                dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {
                    @Override
                    public void onSuccess(List<DYLockDevice> data) {
                        LogUtil.d("-------search...---------");
                        DYLockDevice dYLockDevice = BluetoothUtil.findBestDevice(data, uniqueKeySet);

                        if (dYLockDevice != null) {
//                            if (yao) {//摇一摇开锁
//                                if (keyDbs != null && keyDbs.size() > 0) {
//                                    boolean find = false;
//                                    for (int i = 0; i < keyDbs.size(); i++) {
//                                        if (dYLockDevice.getName().contains(keyDbs.get(i).macCode)) {
//                                            find = true;
//                                            break;
//                                        }
//                                    }
//
//                                    if (find) {
//                                        findDevice(dYLockDevice, yao);
//                                    }
//                                }
//                            } else {
                            //一键开锁
                            findDevice(dYLockDevice, yao);
//                            }
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        ToastUtil.showToast(MainActivity.this, "未搜索到锁");
                        reason = "未搜索到锁";
                        AddLockLogUtil.addLockLog(userVo, mdYLockDevice, AddLockLogUtil.operType_Open, reason, address);
                        if (dunyunDialog != null)
                            dunyunDialog.dismiss();
                        isClicked = false;
                    }
                });
            }

        }
    }

    String reason = "成功";
    private boolean isFinding = false;

    /***
     * 找到锁的本地密码
     * @param dYLockDevice
     * @param yao
     */
    private synchronized void findDevice(DYLockDevice dYLockDevice, boolean yao) {
        LogUtil.d("find----" + dYLockDevice.getName());
        if (dYLockDevice != null && !isFinding) {
            isFinding = true;
            if (keyMaps != null && !keyMaps.isEmpty()) {
                for (Map.Entry<KeyVo, KeyPasswd> entry : keyMaps.entrySet()) {
                    KeyVo keyVoT = (KeyVo) entry.getKey();
                    KeyPasswd keyPasswdT = (KeyPasswd) entry.getValue();
                    if (keyVoT.getMacCode().contains(dYLockDevice.getMac())) {
                        keyVo = keyVoT;
                        keyPasswd = keyPasswdT;

                        lockVo = LockDbUtil.query(keyVoT.getMacCode(), phone);

                        break;
                    }
                }

                if (keyVo != null && keyPasswd != null) {
                    dunyunSDK.stopSearchDevices();
                    mdYLockDevice = dYLockDevice;
                    connectDevice(dYLockDevice, keyVo, keyPasswd, true, yao);
                }
            }
            isFinding = false;
        }
    }

    /***
     * 连接锁设备，连接成功后，握手；连接失败后，提示用户失败
     * @param dyLockDevice 锁设备信息
     * @param keyVo 钥匙信息
     * @param keyPasswd 本地开锁密码
     * @param flag 直接连接
     * @param yao 是否摇一摇开锁
     */
    private void connectDevice(final DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, boolean flag, final boolean yao) {
        lastTime = System.currentTimeMillis();
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
                                auth(dyLockDevice, keyVo, keyPasswd, yao);
                            }
                        }, 120);
                    }

                    @Override
                    public void onFailed(DYLockDevice dyLockDevice, String reason) {
                        reason = reason;
                        AddLockLogUtil.addLockLog(userVo, mdYLockDevice, AddLockLogUtil.operType_Open, reason, address);

                        ToastUtil.showToast(MainActivity.this, "锁连接失败," + reason);
                        if (dunyunDialog != null)
                            dunyunDialog.dismiss();
                        isClicked = false;

                        BuglyUtil.postInfo("锁连接失败");
                        com.psoft.bluetooth.utils.LogUtil.d("---连接失败---");
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

                        if (dunyunDialog != null)
                            dunyunDialog.dismiss();
                        isClicked = false;
                        if (dunyunSDK != null) {
                            dunyunSDK.destroy();
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                });
            }
        });
    }

    /***
     * 锁握手，目前取消，改为直接发送开锁指令
     * @param dyLockDevice 锁设备信息
     * @param keyVo 钥匙信息
     * @param keyPasswd 本地密码
     * @param yao 是否摇一摇开锁
     */
    public void auth(DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean yao) {
        String userId = UserUtil.parseAddTime(keyVo.getAddTm()) + keyVo.getMobile().substring(1);
        lockUser = new LockUser();
        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
        lockUser.setUserId(userId);
        lockUser.setOpenLockPwd(keyPasswd.password);

        if (yao) {
            open(lockUser, keyVo, false);
        } else {
            if (isClicked) {
                open(lockUser, keyVo, false);
            }
        }
        ToastUtil.showToast(context, keyVo.getKeyName() + "已连接到锁");

        auth = true;
        //握手成功
        image_open.setImageResource(R.drawable.tab_icon_open2);
        open_txt.setTextColor(getResources().getColor(R.color.open_color));
    }

    /***
     * 发送开锁指令，成功后获取开锁记录；失败后，提示开锁失败
     * @param lockUser 用户信息
     * @param keyVo 钥匙信息
     * @param flag 是否需要保存开锁密码
     */
    private void open(final LockUser lockUser, final KeyVo keyVo, final boolean flag) {
        dunyunSDK.openLock(lockUser, new Callback<LockInfo>() {
            @Override
            public void onSuccess(LockInfo data) {
                time = System.currentTimeMillis() - lastTime;
                MainApplication.lastClickTime = System.currentTimeMillis();
                if (dunyunDialog != null)
                    dunyunDialog.dismiss();

                isClicked = false;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRecords(lockUser, keyVo);
                    }
                }, 300);

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

                MediaPlayerUtil.getInstance().start(context, R.raw.open_success);
                reason = "";
            }

            @Override
            public void onFailed(String error) {
                runOnUiDialog("开锁失败，密码错误或者" + error);
                dunyunSDK.destroy();

                if (dunyunDialog != null)
                    dunyunDialog.dismiss();

                isClicked = false;

                reason = "密码错误或者" + error;
                AddLockLogUtil.addLockLog(userVo, mdYLockDevice, AddLockLogUtil.operType_Open, reason, address);
            }
        });
    }

    /***
     * 获取锁内用户信息，两种情况
     * 1、first为true时，找到需要删除的用户
     * 2、first为false时，找到需要同步的用户，并同步到后台
     * @param keyVo 钥匙信息
     * @param lockUser 用户信息
     * @param first
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
                    LockUserUtil.addLockUser(keyVo, userVo.getToken(), data, phone);
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

    /***
     *GPS是否打开，android6.0以上需要
     * @param context
     * @return
     */
    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /***
     * 根据锁内返回的用户信息比对后台用户信息，如果有需要删除的用户，则执行删除操作，
     * 删除完成后，再次读取锁内用户同步到后台，保证数据一致性
     * @param keyVo 钥匙信息
     * @param lockUser 用户信息
     * @param data 锁内用户列表
     */
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

    /***
     * 删除锁内用户
     * @param keyVo 钥匙信息
     * @param lockUser 用户信息
     * @param delIndex 删除的锁内用户索引号
     */
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

    /***
     * g更新锁内系统时间，成功后获取电量
     * @param keyVo 钥匙信息
     * @param lockUser 用户信息
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
     * 获取电量，成功后，同步到后台
     * @param keyVo
     * @param lockUser
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
        ToastUtil.showToast(MainActivity.this, content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("MainActivity-----onResume" + userVo);
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }
        if (userVo != null) {
            keyDbs = YaoKeyDbUtil.list(userVo.getMobile());
            if (userVo != null) {
                lockVoList = LockDbUtil.query(phone);
                lockVoList = BluetoothUtil.query(lockVoList, phone);
                if (lockVoList.size() > 0) {
                    keyMaps = BluetoothUtil.getKeyVos(phone, lockVoList);
                }
            }
        }
        if (fragmentShop != null) {
            fragmentShop.updateWebView();
            fragmentShop.refresh();
        }
        if (fragmentKeys != null) {
            fragmentKeys.setMessageCount();
        }
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
                    String operType = "一键开锁";
                    address = SharedUtil.getString(context, "locationAddress");
                    RecordsUtil.addLockRecord(keyVo, "3", "1", time + "", userVo.getToken(), data, userVo.getMobile(), operType, address);
                }
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
            }
        });
    }

    private void showDialog(final String title, final String content) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (whiteDialog != null && whiteDialog.isShowing()) {
                    whiteDialog.dismiss();
                    whiteDialog = null;
                }
                whiteDialog = new WhiteDialog(MainActivity.this, title, content, new DialogListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){//成功

        }else if(resultCode == 2){//失败
            MainActivity.this.finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {

            LogUtil.d("---------Main exit------------");
            MainApplication.getInstance().setUserVo(null);
            HomeFragment.refresh = true;
            fragmentShop.refresh();
            if (fragmentCommunity != null) {
                fragmentCommunity.refresh();
            }
//            if(fragmentKeys != null){
//                fragmentKeys.reFresh();
//            }
//            if(fragmentMe != null){
//                fragmentMe.reFresh();
//            }
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    //点击首页
    @OnClick(R.id.layout_shop)
    void clickAtShop() {
        check(0);
        if (fragmentShop == null)
            fragmentShop = new HomeFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragmentShop);
        fragmentTransaction.commit();
    }

    //点击社区
    @OnClick(R.id.layout_community)
    void clickAtCommunity() {
        check(1);
        if (fragmentCommunity == null)
            fragmentCommunity = new AreaFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragmentCommunity);
        fragmentTransaction.commit();
    }

    //点击钥匙
    @OnClick(R.id.layout_keys)
    void clickAtKeys() {
        if (userVo != null) {
            check(2);
            if (fragmentKeys == null)
                fragmentKeys = new KeysFragment();
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragmentKeys);
            fragmentTransaction.commit();
        } else {
            MainActivity.this.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    //我的
    @OnClick(R.id.layout_me)
    void clickAtMe() {
        check(3);
        if (fragmentMe == null)
            fragmentMe = new SystemFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragmentMe);
        fragmentTransaction.commit();
    }

    /***
     * 一键开锁
     */
    @OnClick(R.id.layout_open)
    void clickAtOpen() {
        if (System.currentTimeMillis() - MainApplication.lastClickTime < 10 * 1000) {
            Toast.makeText(MainActivity.this, "锁加密中，请稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }

        isFinding = false;
        yaoyao = false;
        if (userVo != null) {
            isClicked = true;
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(this);
            }
            lockVoList = LockDbUtil.query(phone);
            lockVoList = BluetoothUtil.query(lockVoList, phone);
            if (lockVoList.size() > 0) {
                if (auth) {
                    if (dunyunSDK.isConnected()) {
                        open(lockUser, keyVo, false);
                    } else {
                        showDialog();
                        search(false);
                    }
                }
            }
        } else {
            MainActivity.this.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    private void showDialog() {
        if (dunyunDialog == null) {
            dunyunDialog = new DunyunDialog(this, "正在开锁...", new DialogListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
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
        }
        try {
            dunyunDialog.setCanceledOnTouchOutside(false);
            dunyunDialog.show();
            dunyunDialog.startAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check(int index) {
        shopIv.setImageResource(R.drawable.tab_icon_home);
        communityIv.setImageResource(R.drawable.tab_icon_community);
        keysIv.setImageResource(R.drawable.tab_icon_key);
        meIv.setImageResource(R.drawable.tab_icon_me);
        switch (index) {
            case 0:
                shopIv.setImageResource(R.drawable.tab_icon_home_checked);
                break;
            case 1:
                communityIv.setImageResource(R.drawable.tab_icon_community_checked);
                break;
            case 2:
                keysIv.setImageResource(R.drawable.tab_icon_key_ckecked);
                break;
            case 3:
                meIv.setImageResource(R.drawable.tab_icon_me_checked);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dunyunSDK != null)
            dunyunSDK.destroy();

        unRegisterNetworkReceiver();
        sensorManager.unregisterListener(this);
//        startService(new Intent(context, BleService.class));
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onSuccess(UserVo userVo) {
        userVo.setPassWord(passwd);
        mainApplication.setUserVo(userVo);
        SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
        SharedUtil.putString(context, "phone", userVo.getMobile());
    }

    @Override
    public void onFailed(String result) {
        if (result != null && result.equals("密码错误")) {
            SharedUtil.clear(context);
        } else {
            String userVoStr = SharedUtil.getString(context, UserVo.class + "");
            if (userVoStr != null) {
                UserVo userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
                mainApplication.setUserVo(userVo);
                SharedUtil.putString(context, "phone", userVo.getMobile());
            }
        }
    }

    /***
     * 检测APP版本，如有更新则提示用户下载版本
     */
    private void updateVersion() {
        GetVersionNewBiz getVersionNewBiz = new GetVersionNewBiz(new GetVersionNewCallback() {
            @Override
            public void onGetVersionSuccess(final VersionVo versionVo) {
                String versionCode = SharedUtil.getString(context, "version" + versionVo.getCode());
                if (versionCode == null) {
                    try {
                        int code = Integer.parseInt(versionVo.getCode());
                        int locationVersionCode = Integer.parseInt(AppUtil.getPackageInfo(context).versionCode + "");

                        if (code > locationVersionCode) {
                            DialogUtil.showDialog(context, getResources().getString(R.string.update_title) + "\n" + versionVo.getDescription(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    download(versionVo);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    SharedUtil.putString(context, "version" + versionVo.getCode(), "1");
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onGetVersionFailed(String result) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
            }
        });
        getVersionNewBiz.getVersion();
    }

    /***
     * 下载APP更新文件
     *
     * @param versionVo
     */
    private void download(VersionVo versionVo) {
        mLoadingDialog = DialogUtil.showWaitDialog(context, getResources().getString(R.string.update_download_title));

        VersionUtil.download(versionVo, FileUtil.getFileDownloadDir(context), new DownloadCallback() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFinish(File file) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                install(file);
            }

            @Override
            public void onError(int code, String error) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                ToastUtil.showToast(context, "下载失败");
            }
        });
    }

    /***
     * 安装APK
     *
     * @param file 安装文件
     */
    private void install(File file) {
        AppUtil.installApk(context, file);
    }

    List<KeyCacheDb> list;

    private BroadcastReceiver networkBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isConnected = false;
            if (connectivityManager != null) {
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (int i = 0; i < networkInfos.length; i++) {
                    NetworkInfo.State state = networkInfos[i].getState();
                    if (NetworkInfo.State.CONNECTED == state) {
                        System.out.println("------------> Network is ok");
                        isConnected = true;
                        break;
                    }
                }
            }

            if (isConnected) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list = KeyCacheDbUtil.list(phone);
                        deleteKey();
                    }
                });
            }
        }
    };

    AddKeyBiz addKeyBiz;

    private void deleteKey() {
        if (list != null && list.size() > 0) {
            if (addKeyBiz == null) {
                addKeyBiz = new AddKeyBiz(new AddKeyCallback() {
                    @Override
                    public void onAddKeyToWebSuccess(String result) {
                        if (list != null && list.size() > 0) {
                            KeyCacheDbUtil.delete(list.get(0).id);
                            list.remove(list.get(0));
                        }
                        if (list != null && list.size() > 0) {
                            deleteKey();
                        }
                    }

                    @Override
                    public void onAddKeyToWebFailed(String result) {

                    }
                });
            }

            KeyCacheDb key = list.get(0);
            if (userVo != null) {
                addKeyBiz.addKeyToWebCache(key.macCode, key.keyIndex, key.lockName, key.keyName, key.mobile, key.addTm,
                        key.costTime, key.macName, userVo.getToken());
            }
        }
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkBroadcast, filter);
    }

    private void unRegisterNetworkReceiver() {
        this.unregisterReceiver(networkBroadcast);
    }

    private long lastYaoTimeMillis = 0l;
    private List<YaoKeyDb> keyDbs;

    @Override
    public void onSensorChanged(SensorEvent event) {
//        int sensorType = event.sensor.getType();

//        float[] values = event.values;
//        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
//            int value = 13;
//            if ((Math.abs(values[0]) > value || Math.abs(values[1]) > value || Math.abs(values[2]) > value)) {
//
//                long currentTimeMillis = System.currentTimeMillis();
//                if (currentTimeMillis - lastYaoTimeMillis >= 1000) {
//                    lastYaoTimeMillis = System.currentTimeMillis();
//                    return;
//                } else {
//                    lastYaoTimeMillis = System.currentTimeMillis();
//                }
//                LogUtil.d("摇一摇.....");
//                //摇一摇开锁，
//                if (keyDbs != null && keyDbs.size() > 0) {
//                    LogUtil.d("---------" + keyDbs.size());
//                    mVibrator.vibrate(200);
//                    if (dunyunSDK != null && dunyunSDK.isConnected()) {
//                        boolean find = false;
//                        for (int i = 0; i < keyDbs.size(); i++) {
//                            if (dunyunSDK.getDYLockDevice().getName().contains(keyDbs.get(i).macCode)) {
//                                find = true;
//                                break;
//                            }
//                        }
//                        if (find) {
//                            open(lockUser, keyVo, false);
//                        } else {
//                            showDialog();
//                            if (!isRunning) {
//                                search(true);
//                            }
//                        }
//                    } else {
//                        showDialog();
//                        if (!isRunning) {
//                            search(true);
//                        }
//                    }
//                }
//
//            }
//
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 登陆
     */
    private void login() {
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            UserVo userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            passwd = userVo.getPassWord();
            if (pushId != null && pushId.length() > 6) {
                userVo.setPushId(pushId);
            }
            loginBiz.login(userVo);
            com.psoft.framework.android.base.utils.LogUtil.d(userVo.toString());
        }
    }

    public static String latitude;
    public static String longitude;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    /***
     * 获取定位信息，缓存到Shared
     */
    private void location() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        latitude = amapLocation.getLatitude() + "";//获取纬度
                        longitude = amapLocation.getLongitude() + "";//获取经度

                        SharedUtil.putString(MainActivity.this, "locationAddress", amapLocation.getAddress() + "");
                        SharedUtil.putString(MainActivity.this, "latitude", latitude + "");
                        SharedUtil.putString(MainActivity.this, "longitude", longitude + "");
                        amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间

                        LogUtil.d("--------------" + latitude + "----------" + longitude);

                        mlocationClient.stopLocation();
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

}
