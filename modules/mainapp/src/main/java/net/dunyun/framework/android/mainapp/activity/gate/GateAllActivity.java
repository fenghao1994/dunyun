package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.utils.HexUtil;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.WifiUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.GateInfoAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysGateAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysGateListAdapter;
import net.dunyun.framework.android.mainapp.gate.GateCode;
import net.dunyun.framework.android.mainapp.gate.GateConnect;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.util.GateUtil;
import net.dunyun.framework.android.mainapp.util.WifiM;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.DoorsVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateInfoVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class GateAllActivity extends BaseActivity {

    private Context context = null;
    private UserVo userVo;
    private String phone;
    @Bind(R.id.gridview)
    GridView gridview;

    private KeyChainVo keyChainVo;
    private KeysGateListAdapter keysGateAdapter;
    private ArrayList<KeyChainVo> keysGateList;
    private List<GatesVo> gates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        baseSetContentView(R.layout.activity_gate_all);
        ButterKnife.bind(this);

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        setTitle(keyChainVo.getChainName());

        keysGateList = new ArrayList<KeyChainVo>();
        gates = keyChainVo.getGates();
        for(GatesVo gatesVo : gates){
            KeyChainVo keyChainVo = new KeyChainVo();
            keyChainVo.setChainName(gatesVo.getUsername());
            keysGateList.add(keyChainVo);
        }
        keysGateAdapter = new KeysGateListAdapter(GateAllActivity.this);
        gridview.setAdapter(keysGateAdapter);
        keysGateAdapter.clear();
        keysGateAdapter.refreshAdapter(keysGateList);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                open(gates.get(position));
            }
        });
    }

    private void open(GatesVo gatesVo) {
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
                        ToastUtil.showToast(GateAllActivity.this, "连接失败");

                        break;
                    case 2://接收数据
                        Bundle bundle = msg.getData();
                        byte[] bytes = bundle.getByteArray("data");
                        LogUtil.d(HexUtil.byteToString(bytes));
                        if( keyChainVo != null &&"2".equals(keyChainVo.getChainType())){
                            try{
                                String grantNum = keyChainVo.getGrantNum();
                                int grantNumInt = Integer.parseInt(grantNum);
                                grantNumInt--;
                                GateUtil.update(keyChainVo.getId(),userVo.getToken(),grantNumInt+"");
                            }catch (Exception e){
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

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        GateAllActivity.this.finish();
    }

}
