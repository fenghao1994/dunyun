package net.dunyun.framework.android.mainapp.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.dunyun.framework.android.mainapp.activity.AddKeyActivity;
import net.dunyun.framework.android.mainapp.activity.KeyManageActivity;
import net.dunyun.framework.android.mainapp.activity.KeyManageAuthActivity;
import net.dunyun.framework.android.mainapp.activity.LoginActivity;
import net.dunyun.framework.android.mainapp.activity.MsgActivity;
import net.dunyun.framework.android.mainapp.activity.WebviewActivity;
import net.dunyun.framework.android.mainapp.activity.gate.GateAddActivity;
import net.dunyun.framework.android.mainapp.activity.gate.GateAllActivity;
import net.dunyun.framework.android.mainapp.activity.gate.GateManageActivity;
import net.dunyun.framework.android.mainapp.activity.gate.GateManageAuthActivity;
import net.dunyun.framework.android.mainapp.adapter.KeysAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysGateAdapter;
import net.dunyun.framework.android.mainapp.application.MainApplication;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.GetAdvertisesBiz;
import net.dunyun.framework.android.mainapp.biz.GetAdvertisesCallback;
import net.dunyun.framework.android.mainapp.biz.GetKeysBiz;
import net.dunyun.framework.android.mainapp.biz.GetKeysCallback;
import net.dunyun.framework.android.mainapp.biz.MessageBiz;
import net.dunyun.framework.android.mainapp.biz.MessageCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GetGateKeysBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GetGateKeysCallback;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.gate.KeyChainUtil;
import net.dunyun.framework.android.mainapp.gate.GateCode;
import net.dunyun.framework.android.mainapp.gate.GateConnect;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.util.AddLockLogUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.AesUtilGate;
import net.dunyun.framework.android.mainapp.util.Base64Decoder;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.BuglyUtil;
import net.dunyun.framework.android.mainapp.util.GateUtil;
import net.dunyun.framework.android.mainapp.util.IntegerUtil;
import net.dunyun.framework.android.mainapp.util.LockUserUtil;
import net.dunyun.framework.android.mainapp.util.LockUtil;
import net.dunyun.framework.android.mainapp.util.MediaPlayerUtil;
import net.dunyun.framework.android.mainapp.util.RecordsUtil;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.util.WifiM;
import net.dunyun.framework.android.mainapp.vo.AdvertiseVo;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockInfo;
import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.callback.ListCallback;
import com.psoft.bluetooth.utils.HexUtil;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.ui.view.playview.SlidingPlayView;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.WifiUtil;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.DoorsVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.DunyunDialog;
import net.dunyun.framework.android.mainapp.widget.PasswdDialog;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 钥匙
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class KeysFragment extends BaseFragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener
        , GetKeysCallback, GetAdvertisesCallback, UpdateLockKeyCallback
        , GetGateKeysCallback {

    @Bind(R.id.gridview)
    GridView keysGridview;
    @Bind(R.id.gridview_gate)
    GridView keysGridviewGate;
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
    @Bind(R.id.lock_key_lay)
    FrameLayout lock_key_lay;
    @Bind(R.id.gate_key_lay)
    FrameLayout gate_key_lay;
    @Bind(R.id.slidingPlayView)
    SlidingPlayView slidingPlayView;
    private KeysAdapter keysAdapter;
    private List<LockVo> keysList;

    LayoutInflater mInflater;
    private View view;
    @Bind(R.id.mPullRefreshView)
    PullToRefreshView mPullRefreshView;
    @Bind(R.id.mPullRefreshView_gate)
    PullToRefreshView mPullRefreshView_gate;

    private GetKeysBiz getKeysBiz;
    private GetGateKeysBiz getGateKeysBiz;
    private GetAdvertisesBiz getAdvertisesBiz;

    private boolean isInitAdvertises = false;
    private DunyunSDK dunyunSDK;
    private List<DYLockDevice> dyLockDevices;
    private Handler mHandler;
    private AddLockRecordBiz addLockRecordBiz;
    private UpdateLockKeyBiz updateLockKeyBiz;

    private List<KeyVo> delIndexs = null;
    private UserVo userVo;
    private ActivityTitleUtil activityTitleUtil;
    //失败次数
    private int failedNum = 0;
    private KeyVo keyVo;
    private KeyPasswd keyPasswd;

    public boolean mFlag;
    private DYLockDevice dYLockDevice;
    private List<KeyChainVo> keysGateList;
    private KeysGateAdapter keysGateAdapter;
    private List<GatesVo> gatesVoList;
    private long time;
    private String address;

    private boolean isFirst = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            mInflater = inflater;
            view = inflater.inflate(R.layout.fragment_keys, null, false);
            ButterKnife.bind(this, view);
            initTitle(view);
            keysList = new ArrayList<LockVo>();
            initViews(view);
            initRadioButton();
            initPlayView();

            getKeysBiz = new GetKeysBiz(this);
            getGateKeysBiz = new GetGateKeysBiz(this);
            getAdvertisesBiz = new GetAdvertisesBiz(this);
            updateLockKeyBiz = new UpdateLockKeyBiz(this);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
        if (userVoStr != null) {
            LogUtil.d("---------onActivityCreated--------"+isFirst);
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);

            if(!isFirst){
//                getKeysBiz.getKeys(userVo);
                getGateKeysBiz.getKeys(userVo);
                mPullRefreshView.headerRefreshing();
            }

            getUnReadMessage(userVo);

            List<KeyChainVo> keyChainVos = KeyChainUtil.query(userVo.getMobile());
            KeyChainVo keyChainVo = new KeyChainVo();
            List<GatesVo> gatesVos = new ArrayList<GatesVo>();
            keyChainVo.setGates(gatesVos);
            keysGateList = keyChainVos;
            keysGateList.add(keyChainVo);

            if (!isInitAdvertises) {
                getAdvertisesBiz.getAdvertises(userVo);
            }

            address = SharedUtil.getString(getActivity(),"locationAddress");

        }

        if (mHandler == null) {
            mHandler = new Handler(getActivity().getMainLooper());
        }
    }

    public void reFresh(){
        String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
        if (userVoStr != null) {
            LogUtil.d("---------onActivityCreated--------");
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            getKeysBiz.getKeys(userVo);
            getGateKeysBiz.getKeys(userVo);
            getUnReadMessage(userVo);

            List<KeyChainVo> keyChainVos = KeyChainUtil.query(userVo.getMobile());
            KeyChainVo keyChainVo = new KeyChainVo();
            List<GatesVo> gatesVos = new ArrayList<GatesVo>();
            keyChainVo.setGates(gatesVos);
            keysGateList = keyChainVos;
            keysGateList.add(keyChainVo);

            if (!isInitAdvertises) {
                getAdvertisesBiz.getAdvertises(userVo);
            }

            address = SharedUtil.getString(getActivity(),"locationAddress");
        }

        if (mHandler == null) {
            mHandler = new Handler(getActivity().getMainLooper());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }
    }

    private void initTitle(View view) {
        activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(view, view.getResources().getString(R.string.main_tab_3), null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userVo != null) {
                    getActivity().startActivity(new Intent(getActivity(), MsgActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        activityTitleUtil.setRightBtnBg(R.drawable.title_icon_msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            getKeysBiz.getKeys(userVo);
        } else if (resultCode == 2) {
            getGateKeysBiz.getKeys(userVo);
        }
    }

    private LockVo lockVo;
    private GateConnect gateConnect;
    private boolean firstOpenLock = true;
    private GatesVo gatesVo = null;
    private LoadingDialog openGateDialog;
    KeyChainVo keyChainVoTemp;

    // Gps是否可用
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

    private void initViews(View view) {
        // 设置监听器
        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView_gate.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);
        mPullRefreshView_gate.setOnFooterLoadListener(this);
        keysGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dunyunSDK = DunyunSDK.getInstance(getActivity());

                if (System.currentTimeMillis() - MainApplication.lastClickTime < 10 * 1000) {
                    Toast.makeText(getActivity(), "锁加密中，请稍后再试...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (position == keysList.size() - 1) {
                    LogUtil.d("-----(Build.VERSION.SDK_INT-----"+Build.VERSION.SDK_INT);
                    if(Build.VERSION.SDK_INT >= 23) {
                        if(isGpsEnable(getActivity())){

                        }else {
                            Toast.makeText(getActivity(),"安卓6.0以上系统，必须打开GPS才可以使用蓝牙",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (dunyunSDK != null) {
                        dunyunSDK.destroy();
                    }
                    KeysFragment.this.startActivityForResult(new Intent(KeysFragment.this.getActivity(), AddKeyActivity.class), 0);
                } else {
                    failedNum = 0;

                    lockVo = keysList.get(position);
                    keyVo = BluetoothUtil.getKeyVo(keysList.get(position).getLockKeys(), userVo.getMobile());
                    if ("2".equals(keyVo.getKeyType())) {
                        keyPasswd = new KeyPasswd();
                        keyPasswd.password = AesUtil.getInstance().decrypt(keyVo.getGrantPwd());
                        if (keyVo.getGrantNum().equals("0")) {
                            ToastUtil.showToast(getActivity(), "授权次数已用完");
                            return;
                        }
                        if (DateUtil.getCurrentDate().compareTo(keyVo.getGrantEdt()) > 0) {
                            ToastUtil.showToast(getActivity(), "授权结束时间已到");
                            return;
                        }
                        if (DateUtil.getCurrentDate().compareTo(keyVo.getGrantBdt()) < 0) {
                            ToastUtil.showToast(getActivity(), "授权开始时间未到");
                            return;
                        }
                    } else {
                        keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), userVo.getMobile());
                    }

                    if (keyPasswd == null) {
                        showPasswdDialog(keyVo);
                    } else {
                        firstOpenLock = true;
                        LogUtil.d(keyPasswd.password);
                        openLock(keyVo, keyPasswd, false);
                    }
                }
            }
        });

        keysGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != keysList.size() - 1) {
                    Intent intent = new Intent();
                    KeyVo keyVo = BluetoothUtil.getKeyVo(keysList.get(position).getLockKeys(), userVo.getMobile());
                    if ("2".equals(keyVo.getKeyType())) {
                        intent.setClass(KeysFragment.this.getActivity(), KeyManageAuthActivity.class);
                    } else {
                        intent.setClass(KeysFragment.this.getActivity(), KeyManageActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lockVo", keysList.get(position));
                    intent.putExtras(bundle);
                    KeysFragment.this.startActivityForResult(intent, 0);
                }
                return true;
            }
        });

        keysAdapter = new KeysAdapter(getActivity());
        keysGridview.setAdapter(keysAdapter);
        keysAdapter.clear();
        keysAdapter.refreshAdapter(keysList);
        //t通道闸
        keysGateList = new ArrayList<KeyChainVo>();

        KeyChainVo keyChainVo = new KeyChainVo();
        List<GatesVo> gatesVos = new ArrayList<GatesVo>();
        keyChainVo.setGates(gatesVos);
        keysGateList.add(keyChainVo);

        keysGateAdapter = new KeysGateAdapter(getActivity());
        keysGridviewGate.setAdapter(keysGateAdapter);
        keysGateAdapter.clear();
        keysGateAdapter.refreshAdapter(keysGateList);

        keysGridviewGate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == (keysGateList.size() - 1)) {
                    KeysFragment.this.startActivityForResult(new Intent(KeysFragment.this.getActivity(), GateAddActivity.class), 0);
                } else {
                    gatesVo = null;
                    keyChainVoTemp = keysGateList.get(position);
                    gatesVoList = keyChainVoTemp.getGates();
                    if (WifiM.isNetworkConnected(getActivity())) {
                        WifiInfo wifiInfo = WifiUtil.getConnectionInfo(getActivity());

                        String ssid = wifiInfo.getSSID().replace("\"", "");
                        LogUtil.d("连接的wifi=" + ssid);

                        if (gatesVoList != null && gatesVoList.size() > 0) {
                            for (GatesVo v : gatesVoList) {
                                if (ssid != null && ssid.equals(v.getWifiMac())) {
                                    gatesVo = v;
                                    break;
                                }
                            }
                        }

                        if (gatesVo != null) {//WIFI连接OK,直接开锁
                            final List<DoorsVo> doorsVos = gatesVo.getDoors();
                            if (doorsVos != null && doorsVos.size() == 0) {
                                openGate(gatesVo, "0");
                            } else {
                                if (BluetoothUtil.bluetoothIsOpen(getActivity())) {
                                    openGateDialog = DialogUtil.showWaitDialog(getActivity(), "正在连接...");
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUtil.cancelWaitDialog(openGateDialog);
                                        }
                                    }, 2000);

                                    doorsMap = new HashMap<DoorsVo, Integer>();

                                    dunyunSDK = DunyunSDK.getInstance(getActivity());
                                    dunyunSDK.setAddKey(true);
                                    if (dunyunSDK.isConnected()) {
                                        dunyunSDK.destroy();
                                    }
                                    dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {

                                        @Override
                                        public void onSuccess(List<DYLockDevice> data) {

                                            for (int i = 0; i < data.size(); i++) {
                                                String deviceName = data.get(i).getName();
                                                for (int j = 0; j < doorsVos.size(); j++) {
                                                    LogUtil.d("------d------" + deviceName + "------m------" + doorsVos.get(j).getDoorMac());
                                                    if (deviceName.equalsIgnoreCase(doorsVos.get(j).getDoorMac())) {
                                                        doorsMap.put(doorsVos.get(j), data.get(i).getRssi());
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            if (dunyunSDK != null)
                                                dunyunSDK.destroy();
                                        }
                                    }, true);

                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (dunyunSDK != null)
                                                dunyunSDK.destroy();

                                            if (doorsMap.isEmpty()) {
                                                ToastUtil.showToast(getActivity(), "请靠近闸门再试");
                                            } else {
                                                DoorsVo doorsVo = null;
                                                int rssi = -100;
                                                Iterator iter = doorsMap.entrySet().iterator();
                                                while (iter.hasNext()) {
                                                    Map.Entry entry = (Map.Entry) iter.next();
                                                    DoorsVo key = (DoorsVo) entry.getKey();
                                                    Integer val = (Integer) entry.getValue();
                                                    if (val >= rssi) {
                                                        doorsVo = key;
                                                        rssi = val;
                                                    }
                                                }

                                                if (doorsVo == null) {
                                                    ToastUtil.showToast(getActivity(), "请靠近闸门再试，未检测到闸门");
                                                } else if (rssi < -80) {
                                                    ToastUtil.showToast(getActivity(), "请靠近闸门再试,闸门信号弱");
                                                } else {
//                                                    ToastUtil.showToast(getActivity(),"door index "+doorsVo.getDoorIndex());
                                                    openGate(gatesVo, doorsVo.getDoorIndex());
                                                }
                                            }
                                        }
                                    }, 2000);

                                } else {
                                    ToastUtil.showToast(getActivity(), "请打开蓝牙");
                                }
                            }

                        } else {//远程开锁
                            //gate01,0,0,1
                            KeyChainVo keyChainVo1 = keysGateList.get(position);
                            if (gatesVoList != null && gatesVoList.size() > 1) {
                                Intent intent = new Intent();
                                intent.setClass(KeysFragment.this.getActivity(), GateAllActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("keyChainVo", keyChainVo1);
                                intent.putExtras(bundle);
                                KeysFragment.this.startActivity(intent);
                            } else {
                                if (gatesVoList == null || gatesVoList.size() == 0) {
                                    return;
                                }
                                gatesVo = gatesVoList.get(0);
                                final String gateName = gatesVo.getUsername();
                                final String keyIndex = gatesVo.getKeyIndex();
                                final String gatePort = "0";
                                final String inOut = "1";

                                final GateConnect gateConnect = new GateConnect();

                                Handler myHandler = new Handler() {
                                    public void handleMessage(Message msg) {
                                        switch (msg.what) {
                                            case 0:
                                                gateConnect.send(GateCode.getOpenGateData(gateName, keyIndex, gatePort, inOut));
                                                break;
                                            case 1:
                                                LogUtil.d("连接失败");
                                                ToastUtil.showToast(getActivity(), "连接失败");

                                                break;
                                            case 2://接收数据
                                                Bundle bundle = msg.getData();
                                                byte[] bytes = bundle.getByteArray("data");
                                                LogUtil.d(HexUtil.byteToString(bytes));
                                                if (gatesVo != null && keyChainVoTemp != null && "2".equals(keyChainVoTemp.getChainType())) {
                                                    try {
                                                        String grantNum = keyChainVoTemp.getGrantNum();
                                                        int grantNumInt = Integer.parseInt(grantNum);
                                                        grantNumInt--;
                                                        GateUtil.update(keyChainVoTemp.getId(), userVo.getToken(), grantNumInt + "");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                break;
                                            case 3:

                                                break;
                                        }
                                        super.handleMessage(msg);
                                    }
                                };

                                int port = 9999;
                                try {
                                    port = Integer.parseInt(gatesVo.getNettyPort());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                gateConnect.setHost(gatesVo.getNettyHost());
                                gateConnect.setPort(port);
                                gateConnect.setHandler(myHandler);
                                gateConnect.start();
                            }

                        }
                    } else {
                        ToastUtil.showToast(getActivity(), "打开失败，请检查网络连接");
                    }
                }
            }
        });

        keysGridviewGate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != keysGateList.size() - 1) {
                    Intent intent = new Intent();
                    if ("2".equals(keysGateList.get(position).getChainType())) {
                        intent.setClass(KeysFragment.this.getActivity(), GateManageAuthActivity.class);
                    } else {
                        intent.setClass(KeysFragment.this.getActivity(), GateManageActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("keyChainVo", keysGateList.get(position));
                    intent.putExtras(bundle);
                    KeysFragment.this.startActivityForResult(intent, 2);
                }
                return true;
            }
        });
    }

    private HashMap<DoorsVo, Integer> doorsMap;

    private void openGate(GatesVo gatesVo, final String door) {
        final GatesVo finalGatesVo = gatesVo;

        if (gateConnect != null) {
            gateConnect.destory();
        }
        gateConnect = new GateConnect();

        Handler myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        gateConnect.send(GateCode.getData(finalGatesVo.getPassword(), finalGatesVo.getAes128key(), finalGatesVo.getKeyIndex(), door));
                        break;
                    case 1:
                        LogUtil.d("连接失败");
                        ToastUtil.showToast(getActivity(), "连接失败,请检查网络");
                        break;
                    case 2://接收数据
                        Bundle bundle = msg.getData();
                        byte[] bytes = bundle.getByteArray("data");
                        LogUtil.d(HexUtil.byteToString(bytes));
                        int result = GateCode.isSuccess(finalGatesVo.getKeyIndex(), finalGatesVo.getAes128key(), bytes);
                        if (result == 1) {
                            if (finalGatesVo != null && keyChainVoTemp != null && "2".equals(keyChainVoTemp.getChainType())) {
                                try {
                                    String grantNum = keyChainVoTemp.getGrantNum();
                                    int grantNumInt = Integer.parseInt(grantNum);
                                    grantNumInt--;
                                    GateUtil.update(keyChainVoTemp.getId(), userVo.getToken(), grantNumInt + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (gateConnect != null) {
                                gateConnect.destory();
                            }
                        }
                        break;
                    case 3:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        gateConnect.setHost(finalGatesVo.getLocalHost());
        int port = 11045;
        try {
            port = Integer.parseInt(finalGatesVo.getLocalPort());
        } catch (Exception e) {
            port = 11045;
        }
        gateConnect.setPort(port);
        gateConnect.setHandler(myHandler);
        gateConnect.start();
    }

    public void getUnReadMessage(UserVo userVo) {
        MessageBiz messageBiz = new MessageBiz(new MessageCallback() {
            @Override
            public void onMessageSuccess(List<MessageVo> messageVoList, PageVo pageVo) {

            }

            @Override
            public void onMessageFailed(String result) {

            }

            @Override
            public void onMessageCount(String number) {
                //ToastUtil.showToast(getActivity(), number);
                activityTitleUtil.showMsg(number);
            }

            @Override
            public void onMessageClearSuccess(String result) {

            }

            @Override
            public void onMessageClearFailed(String result) {

            }
        });

        messageBiz.getUnReadCount(userVo);
    }

    private long lastTime = 0l;

    private void openLock(final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        if (BluetoothUtil.bluetoothIsOpen(getActivity())) {

            showDialog();

            lastTime = System.currentTimeMillis();
            if (dunyunSDK == null) {
                dunyunSDK = DunyunSDK.getInstance(getActivity());
            }
            dunyunSDK.setAddKey(false);
            if (dunyunSDK.isConnected()) {
                dunyunSDK.destroy();
            }

            if (firstOpenLock) {
                //直接连接
                dYLockDevice = new DYLockDevice();
                BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(Base64Decoder.decodeToHex(keyVo.getMacCode()));
                dYLockDevice.setBluetoothDevice(bluetoothDevice);
                dYLockDevice.setName(lockVo.getMacName());

                LogUtil.d(dYLockDevice.getName());
                dunyunSDK.stopSearchDevices();
                mFlag = flag;
                connectDevice(dYLockDevice, keyVo, keyPasswd, flag);

                firstOpenLock = false;
            } else {
                //扫描
                dunyunSDK.startSearchDevices(new Callback<List<DYLockDevice>>() {

                    @Override
                    public void onSuccess(List<DYLockDevice> data) {
                        dyLockDevices = data;
                        dYLockDevice = BluetoothUtil.findBestDevice(data, keyVo.getMacCode());
                        if (dYLockDevice != null) {
                            dYLockDevice.setName(lockVo.getMacName());
                            LogUtil.d(dYLockDevice.getName());
                            dunyunSDK.stopSearchDevices();
                            mFlag = flag;
                            connectDevice(dYLockDevice, keyVo, keyPasswd, flag);
                        }
                    }

                    @Override
                    public void onFailed(String error) {
                        dunyunDialog.dismiss();
                        runOnUiDialog("锁搜索失败");
                        BuglyUtil.postInfo("未找到设备");
                    }
                });
            }
        }
    }

    private DunyunDialog dunyunDialog;

    private void showDialog() {
        if (dunyunDialog == null) {
            dunyunDialog = new DunyunDialog(getActivity(), "正在开锁...", new DialogListener() {
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

    private PasswdDialog passwdDialog;

    private void showPasswdDialog(final KeyVo keyVo) {
        passwdDialog = new PasswdDialog(getActivity(), new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ok:
                        if (passwdDialog.getPasswd().length() == 6) {
                            passwdDialog.dismiss();
                            KeyPasswd keyPasswd = new KeyPasswd();
                            keyPasswd.password = passwdDialog.getPasswd();
                            openLock(keyVo, keyPasswd, true);
                        }
                        break;
                }
            }

            @Override
            public void onItemClick(int position) {

            }
        });
        passwdDialog.show();
    }

    public void setMessageCount() {
        if (userVo != null) {
            getUnReadMessage(userVo);
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
                        }, 20);

                        failedNum = 2;
                    }

                    @Override
                    public void onFailed(DYLockDevice dyLockDevice,String reason) {
                        LogUtil.d("---连接失败---");
                        runOnUiDialog("锁连接失败,"+reason);
                        dunyunDialog.dismiss();
                        BuglyUtil.postInfo("锁连接失败");

                        AddLockLogUtil.addLockLog(userVo, dyLockDevice, AddLockLogUtil.operType_Open, reason, address+"");
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
                        try {
                            runOnUiDialog("锁已断开");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dunyunDialog.dismiss();
                    }
                });
            }
        });
    }

    public void auth(DYLockDevice dyLockDevice, final KeyVo keyVo, final KeyPasswd keyPasswd, final boolean flag) {
        String phone = "";
        if ("2".equals(keyVo.getKeyType())) {
            phone = keyVo.getGrantMbl();
        } else {
            phone = keyVo.getMobile();
        }
        String userId = UserUtil.parseAddTime(keyVo.getAddTm()) + phone.substring(1);
        final LockUser lockUser = new LockUser();
        lockUser.setUserIndex(Integer.parseInt(keyVo.getKeyIndex()));
        lockUser.setUserId(userId);
        lockUser.setOpenLockPwd(keyPasswd.password);

        open(lockUser, keyVo, flag);
    }

    private void open(final LockUser lockUser, final KeyVo keyVo, final boolean flag) {
        dunyunSDK.openLock(lockUser, new Callback<LockInfo>() {
            @Override
            public void onSuccess(LockInfo data) {
                time = (System.currentTimeMillis() - lastTime);
                MainApplication.lastClickTime = System.currentTimeMillis();

                dunyunDialog.dismiss();
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

                MediaPlayerUtil.getInstance().start(getActivity(), R.raw.open_success);
            }

            @Override
            public void onFailed(String error) {
                runOnUiDialog("开锁失败，密码错误或者" + error);
                dunyunDialog.dismiss();
                dunyunSDK.destroy();

                AddLockLogUtil.addLockLog(userVo, dYLockDevice, AddLockLogUtil.operType_Open, "锁端未响应指令，密码错误或者" + error, address);
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
                LogUtil.d("同步电量成功");
                dunyunSDK.destroy();

                LockVo lockVo = new LockVo();
                lockVo.setMacCode(keyVo.getMacCode());
                lockVo.setTdState(data.lockStatus);
                lockVo.setPower(data.batteryPower);
                LockUtil.updateLock(lockVo, userVo.getToken(), userVo.getMobile());
            }

            @Override
            public void onFailed(String error) {
                LogUtil.d("同步电量失败");
                dunyunSDK.destroy();
            }
        });
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
                    String address = SharedUtil.getString(getActivity(), "locationAddress");
                    RecordsUtil.addLockRecord(keyVo, "3", "1", time+"", userVo.getToken(), data, userVo.getMobile(), operType, address+"");
                }
            }

            @Override
            public void onFailed(String error) {
                dunyunSDK.destroy();
            }
        });
    }

    private void runOnUiDialog(final String content) {
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(getActivity(), content);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initPlayView() {
        final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
        ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
        TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
        mPlayText.setText("");
        mPlayImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mPlayImage.setImageResource(R.drawable.logo_default);

        slidingPlayView.addView(mPlayView);
        slidingPlayView.setNavPageResources(R.drawable.play_display, R.drawable.play_hide);
        slidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
        slidingPlayView.startPlay();
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
                    lock_key_lay.setVisibility(View.VISIBLE);
                    gate_key_lay.setVisibility(View.GONE);
                } else {
                    radio_button1.setBackgroundResource(R.color.light_bg_grey);
                    radio_button1.setTextColor(getResources().getColor(R.color.white));
                    line_01.setBackgroundResource(R.color.light_bg_grey);

                    radio_button0.setBackgroundResource(R.color.white);
                    radio_button0.setTextColor(getResources().getColor(R.color.light_grey));
                    line_00.setBackgroundResource(R.color.light_bg_no_grey);
                    lock_key_lay.setVisibility(View.GONE);
                    gate_key_lay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onKeysSuccess(List<LockVo> keyVoList, int flag) {
        LogUtil.d("-------onKeysSuccess----------"+flag);
        isFirst = true;
        LockVo lockVo = new LockVo();
        List<KeyVo> keyVos = new ArrayList<KeyVo>();
        KeyVo keyVo = new KeyVo();
        keyVos.add(keyVo);
        lockVo.setLockKeys(keyVos);
        keyVoList = BluetoothUtil.query(keyVoList, userVo.getMobile());

        keyVoList.add(lockVo);
        keysList = keyVoList;
        keysAdapter.clear();
        keysAdapter.refreshAdapter(keysList);
        if (flag == 1) {
            mPullRefreshView.onHeaderRefreshFinish();
        }
    }

    @Override
    public void onKeysFailed(String result) {
        mPullRefreshView.onHeaderRefreshFinish();
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        if (view.getId() == mPullRefreshView.getId()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPullRefreshView.onFooterLoadFinish();
                }
            });
        } else {
            mPullRefreshView_gate.onFooterLoadFinish();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        if (userVo != null) {
            if (view.getId() == mPullRefreshView.getId()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getKeysBiz.getKeys(userVo);
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getGateKeysBiz.getKeys(userVo);
//                        mPullRefreshView_gate.onHeaderRefreshFinish();
                    }
                });
            }
        } else {
            ToastUtil.showToast(getActivity(), "请先登录");
            mPullRefreshView_gate.onHeaderRefreshFinish();
            mPullRefreshView.onHeaderRefreshFinish();
        }
    }

    @Override
    public void onGetAdvertisesSuccess(final List<AdvertiseVo> advertiseVoList) {
        isInitAdvertises = true;
        if (advertiseVoList != null && advertiseVoList.size() > 0) {

            slidingPlayView.stopPlay();
            slidingPlayView.removeAllViews();
            for (int i = 0; i < advertiseVoList.size(); i++) {
                final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
                ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
                TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
                mPlayText.setText("");
                mPlayImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final int number = i;
                try {
                    Picasso.with(getActivity()).load(advertiseVoList.get(i).getPath())
                            .placeholder(R.drawable.logo_default)
                            .error(R.drawable.logo_default)
                            .into(mPlayImage);

                    mPlayImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), WebviewActivity.class);
                            intent.putExtra("url", advertiseVoList.get(number).getUrl());
                            intent.putExtra("title", advertiseVoList.get(number).getDescription());
                            getActivity().startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    mPlayImage.setImageResource(R.drawable.logo_default);
                }

                slidingPlayView.addView(mPlayView);
            }
        }
        slidingPlayView.setNavPageResources(R.drawable.play_display, R.drawable.play_hide);
        slidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
        slidingPlayView.startPlay();
    }

    @Override
    public void onGetAdvertisesFailed(String result) {

    }

    @Override
    public void onSuccess(String result) {
        if (userVo != null) {
            getKeysBiz.getKeys(userVo);
        }
    }

    @Override
    public void onFailed(String result) {
        if (userVo != null) {
            getKeysBiz.getKeys(userVo);
        }
    }

    @Override
    public void onGatesSuccess(List<KeyChainVo> keyChainVos, int flag) {
        KeyChainVo keyChainVo = new KeyChainVo();
        List<GatesVo> gatesVos = new ArrayList<GatesVo>();
        keyChainVo.setGates(gatesVos);
        keysGateList = keyChainVos;

        keysGateList.add(keyChainVo);
        keysGateAdapter.clear();
        keysGateAdapter.refreshAdapter(keysGateList);

        if (flag == 1) {
            mPullRefreshView_gate.onHeaderRefreshFinish();
        }
    }

    @Override
    public void onGatesFailed(String result) {

    }
}
