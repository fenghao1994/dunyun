package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.AboutSuggestionBlz;
import net.dunyun.framework.android.mainapp.biz.AboutSuggestionCallback;
import net.dunyun.framework.android.mainapp.biz.ChangeBiz;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 名称：AboutSuggestionActivity.java
 * 描述：意见反馈界面.
 *
 * @author wuhx
 * @version v1.0
 * @date：2016/04/13 下午11:52:13
 * @Copyright:
 */
public class AboutSuggestionActivity extends BaseActivity implements AboutSuggestionCallback{

    @Bind(R.id.suggestions_edit) EditText suggestionsEdit;
    private Context context = null;
    private ProgressDialog progressDialog = null;
    private AboutSuggestionBlz mAboutSuggestionBlz=null;
    private UserVo userVo=null;
    @Override
    public void onResume() {
        super.onResume();
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }
        if(userVo == null){
            showToast("请登录");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_about_suggestions);
        ButterKnife.bind(this);

        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.about_suggestions),new LeftOnClickListener(), null, null);

        mAboutSuggestionBlz=new AboutSuggestionBlz(this);
    }
    /* 跳转到blz，发送意见*/
    @OnClick(R.id.send_btn) void sendBtn()
    {
        String suggestionstext = suggestionsEdit.getText().toString().trim();
        if (StrUtil.isEmpty(suggestionstext)) {
            ToastUtil.showToast(context, getResources().getString(R.string.about_suggestions_tip));
            return;
        }
        if (suggestionstext.length()>140) {
            ToastUtil.showToast(context, getResources().getString(R.string.about_suggestions_limit));
            return;
        }
        progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.about_suggestions_dialog),
                getResources().getString(R.string.about_suggestions_sending));

        PushManager.getInstance().initialize(this.getApplicationContext());
        /***
         * 跳转到blz
         * @param
         */
        mAboutSuggestionBlz.aboutSuggestionBlz(userVo, suggestionstext);

    }
    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        AboutSuggestionActivity.this.runOnUiThread(new Runnable() {
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
    /***
     * 提示信息
     * callback返回调用
     */
    @Override
    public void onSuccess(UserVo userVo) {
        DialogUtil.cancelProgressDialog(progressDialog);
        showToast(getResources().getString(R.string.about_suggestions_sendsuccess));
    }

    @Override
    public void onFailed(String result) {
        DialogUtil.cancelProgressDialog(progressDialog);
        showToast(getResources().getString(R.string.about_suggestions_sendfail) + "," + result);
    }
}

