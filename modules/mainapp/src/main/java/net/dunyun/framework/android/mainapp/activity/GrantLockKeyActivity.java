package net.dunyun.framework.android.mainapp.activity;

import android.os.Bundle;

import net.dunyun.framework.android.mainapp.biz.GrantLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;

//import net.dunyun.framework.lock.R;

/**
 *授权钥匙界面
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class GrantLockKeyActivity extends BaseActivity implements GrantLockKeyCallback {

    GrantLockKeyBiz grantLockKeyBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseSetContentView(R.layout.activity_grant_lock_key);

        ButterKnife.bind(this);

        grantLockKeyBiz = new GrantLockKeyBiz(this);
    }

    @Override
    public void onGrantLockKeySuccess(String result) {

    }

    @Override
    public void onGrantLockKeyFailed(String result) {

    }
}
