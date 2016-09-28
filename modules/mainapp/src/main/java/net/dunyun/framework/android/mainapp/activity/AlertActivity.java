package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;

import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 远程报警
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class AlertActivity extends BaseActivity {

    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        setTitle("远程报警");
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        AlertActivity.this.finish();
    }

}
