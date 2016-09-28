package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.psoft.framework.android.base.ui.view.ToastUtil;

import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多设置界面
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class MeSettingActivity extends BaseActivity {

    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_me_setting);
        ButterKnife.bind(this);
        setTitle("更多设置");
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        MeSettingActivity.this.finish();
    }

    @OnClick(R.id.rl_me_setting_near) void rl_me_setting_near(){
        this.startActivity(new Intent(this, InductorActivity.class));
    }

    @OnClick(R.id.rl_me_setting_alert) void rl_me_setting_alert(){
        this.startActivity(new Intent(this, AlertActivity.class));
    }

    @OnClick(R.id.rl_me_setting_shake) void rl_me_setting_shake(){
        this.startActivity(new Intent(this, ShakeActivity.class));
    }

    @OnClick(R.id.rl_me_setting_auth) void rl_me_setting_auth(){
        this.startActivity(new Intent(this, KeyAuthAllActivity.class));
    }

}
