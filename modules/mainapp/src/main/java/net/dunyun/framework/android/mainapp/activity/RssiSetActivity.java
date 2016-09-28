package net.dunyun.framework.android.mainapp.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.GrantLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.NearKeyDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.time.JudgeDate;
import net.dunyun.framework.android.mainapp.widget.time.ScreenInfo;
import net.dunyun.framework.android.mainapp.widget.time.WheelMain;
import net.dunyun.framework.lock.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 感应距离设置界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class RssiSetActivity extends BaseActivity {

    private Context context = null;
    private DunyunSDK dunyunSDK;
    private long lastTime;
    String code;

    @Bind(R.id.tv_rssi) TextView tv_rssi;
    @Bind(R.id.btn_get1)
    Button btn_get1;
    @Bind(R.id.btn_get2)
    Button btn_get2;
    @Bind(R.id.btn_get3)
    Button btn_get3;
    private int lrssi = 0;

    protected Handler mCommonHandler = new Handler() {

        public void handleMessage(Message msg) {
            lrssi = msg.arg1;
            tv_rssi.setText(lrssi+"");
        }
    };
    private UserVo userVo;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_rssi_set);
        ButterKnife.bind(this);
        setTitle("感应距离设置");

        code = getIntent().getStringExtra("macCode");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        search();
    }

    private void search() {
        if (BluetoothUtil.bluetoothIsOpen(this)) {
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
                    for (int i=0; i<data.size(); i++){
                        if(data.get(i).getName().contains(code)){
                            Message msg = new Message();
                            msg.what = 0;
                            msg.arg1 = data.get(i).getRssi();
                            mCommonHandler.sendMessage(msg);
                            break;
                        }
                    }

                }

                @Override
                public void onFailed(String error) {
                    ToastUtil.showToast(RssiSetActivity.this, "搜索结束");

                }
            });
        }
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        RssiSetActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        if(rssi1 == 0){
            ToastUtil.showToast(RssiSetActivity.this, "请先设置位置1的距离");
            return;
        }
        if(rssi2 == 0){
            ToastUtil.showToast(RssiSetActivity.this, "请先设置位置2的距离");
            return;
        }
        if(rssi3 == 0){
            ToastUtil.showToast(RssiSetActivity.this, "请先设置位置3的距离");
            return;
        }
        int temp = (rssi1+rssi2+rssi3)/3;
        NearKeyDbUtil.insert(code, userVo.getMobile(), temp, userVo.getMobile());

        setResult(0);
        finish();
    }

    private int rssi1;
    private int rssi2;
    private int rssi3;

    @OnClick(R.id.btn_get1)
    void btn_get1() {
        rssi1 = lrssi;
        btn_get1.setText(lrssi+"");
    }

    @OnClick(R.id.btn_get2)
    void btn_get2() {
        rssi2 = lrssi;
        btn_get2.setText(lrssi+"");
    }

    @OnClick(R.id.btn_get3)
    void btn_get3() {
        rssi3 = lrssi;
        btn_get3.setText(lrssi+"");
    }

}
