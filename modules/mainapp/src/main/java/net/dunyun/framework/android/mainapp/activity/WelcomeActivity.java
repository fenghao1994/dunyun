package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.psoft.bluetooth.DunyunSDK;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.fund.GestureVerifyActivity;
import net.dunyun.framework.android.mainapp.fund.common.Constants;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 欢迎界面
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class WelcomeActivity extends BaseActivity{

    private Context context = null;
    private static final String PICASSO_CACHE = "picasso-cache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        setTitleInVisibility();

        boolean fromGesture = getIntent().getBooleanExtra("fromGesture", false);

        baseSetContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        String PASSWD_OPEN = SharedUtil.getString(this, Constants.PASSWD_OPEN);
        if(!fromGesture && "yes".equals(PASSWD_OPEN)){
            this.startActivity(new Intent(this, GestureVerifyActivity.class));
            this.finish();
            return;
        }
        PushManager.getInstance().initialize(this.getApplicationContext());
        File cache = new File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE);
        cache.delete();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mainApplication.setIsBle(false);
        }else {
            mainApplication.setIsBle(true);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                goActivity();
            }
        }).start();
    }

    private void goActivity(){
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
    }

}
