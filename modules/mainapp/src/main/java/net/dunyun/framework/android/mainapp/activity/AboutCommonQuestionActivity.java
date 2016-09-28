package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.psoft.framework.android.base.ui.view.ToastUtil;

import net.dunyun.framework.android.mainapp.biz.ChangeBiz;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;

/**
 * 名称：AboutCommonQuestionActivity.java
 * 描述：常见问题界面
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-12
 * @Copyright:
 *
 */

public class AboutCommonQuestionActivity extends BaseActivity{

    private Context context = null;
    ChangeBiz mChangeBiz=null;
    private WebView webview;
    private String url=null,title=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        baseSetContentView(R.layout.activity_about_question);
        ButterKnife.bind(this);

        url = this.getIntent().getExtras().getString("url");
        title = this.getIntent().getExtras().getString("title");
        System.out.println(title+"+"+url);

        webview = (WebView) findViewById(R.id.webview);
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        // 加载需要显示的网页
        webview.loadUrl(url);
        // 设置Web视图
        webview.setWebViewClient(new HelloWebViewClient());

        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), title,new LeftOnClickListener(), null, null);
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
    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        AboutCommonQuestionActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }

    /**
     *  返回上一界面
     */
    class  LeftOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }

}
