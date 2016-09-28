package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.fund.GestureVerifyActivity;
import net.dunyun.framework.android.mainapp.fund.common.Constants;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * WebView界面
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class WebviewActivity extends BaseActivity {

    private Context context = null;
    private LoadingDialog loadingDialog = null;
    private String url;
    private String title;
    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        baseSetContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        url = this.getIntent().getStringExtra("url");

        title = this.getIntent().getStringExtra("title");
        webview = (WebView) findViewById(R.id.webview);
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.loadUrl(url);
        // 设置Web视图
        webview.setWebViewClient(new HelloWebViewClient());

        setTitle(title);
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            WebviewActivity.this.finish();
            return true;
        }
        return true;
    }

    // Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }
    }
}
