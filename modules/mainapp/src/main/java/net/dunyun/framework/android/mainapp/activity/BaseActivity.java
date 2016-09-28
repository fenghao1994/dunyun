package net.dunyun.framework.android.mainapp.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.LogUtil;
import net.dunyun.framework.android.mainapp.application.MainApplication;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 基础界面
 * @author chenzp
 * @ClassName: BaseActivity
 * @Description: Activity基类
 * @date 2015-7-31
 */
public class BaseActivity extends Activity {

    /**Application全局*/
    protected MainApplication mainApplication = null;

    protected LoadingDialog mLoadingDialog = null;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    protected ImageButton title_left_btn;
    protected Button right_one_btn;
    protected Button right_two_btn;
    protected RelativeLayout right_two_btn_lay;
    protected RelativeLayout topLayout;
    protected TextView title_center_txt;

    protected Handler mCommonHandler = new Handler() {

        public void handleMessage(Message msg) {

        }
    };
    private boolean showScrollView = true;
    private TextView title_left_txt;
//    public void abstract

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int number = savedInstanceState.getInt("showScrollView", -1);
            if(number == 1){
                showScrollView = false;
            }
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(showScrollView){
            setContentView(R.layout.activity_base);
        }else{
            setContentView(R.layout.activity_base1);
        }

        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        right_two_btn = (Button)findViewById(R.id.right_two_btn);
        title_left_txt = (TextView)findViewById(R.id.title_left_txt);
        right_one_btn = (Button)findViewById(R.id.right_one_btn);
        right_two_btn_lay = (RelativeLayout)findViewById(R.id.right_two_btn_lay);
        topLayout = (RelativeLayout)findViewById(R.id.topLayout);
        title_center_txt = (TextView)findViewById(R.id.title_center_txt);

        mainApplication = MainApplication.getInstance();
        right_two_btn_lay.setVisibility(View.INVISIBLE);
        right_one_btn.setVisibility(View.INVISIBLE);
    }

    protected void setTitleInVisibility(){
        topLayout.setVisibility(View.GONE);
    }

    /***
     * 设置右侧按钮图像及事件
     * @param bgId
     * @param onClickListener
     */
    protected void setRightTwoButton(int bgId, View.OnClickListener onClickListener){
        right_two_btn.setBackgroundResource(bgId);
        right_two_btn.setOnClickListener(onClickListener);
        right_two_btn.setVisibility(View.VISIBLE);
        right_two_btn_lay.setVisibility(View.VISIBLE);
    }

    /***
     * 置右侧按钮图像及事件
     * @param bgId
     * @param onClickListener
     */
    protected void setRightOneButton(int bgId, View.OnClickListener onClickListener){
        right_one_btn.setBackgroundResource(bgId);
        right_one_btn.setOnClickListener(onClickListener);
        right_one_btn.setVisibility(View.VISIBLE);
    }

    /***
     * 设置标题
     * @param title
     */
    protected void setTitle(String title){
        title_center_txt.setText(title);
    }

    public void baseSetContentView(int layoutResId) {
        LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        llContent.addView(v);
    }

    protected boolean bluetoothIsOpen(){
        if (mBluetoothManager == null) {
            mBluetoothManager = (android.bluetooth.BluetoothManager) this
                    .getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                // LogUtil.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            //LogUtil.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            // 请求打开 Bluetooth
            Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 请求开启 Bluetooth
            this.startActivityForResult(requestBluetoothOn, 1);

            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(this.getClass(), "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(this.getClass(), "---------onResume ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(this.getClass(), "---------onStop ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(this.getClass(), "---------onPause ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(this.getClass(), "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(this.getClass(), "---------onDestroy ");
    }
}
