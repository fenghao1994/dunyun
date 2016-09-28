package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.dunyun.framework.android.mainapp.biz.DownloadCallback;
import net.dunyun.framework.android.mainapp.biz.GetVersionNewBiz;
import net.dunyun.framework.android.mainapp.biz.GetVersionNewCallback;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import com.psoft.framework.android.base.network.http.download.DownloadListener;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.AppUtil;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.VersionUpgraderCallback;
import com.psoft.framework.android.base.utils.VersionUpgraderUtil;
import com.psoft.framework.android.base.vo.VersionUpgraderVo;

import net.dunyun.framework.android.mainapp.util.VersionUtil;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.VersionVo;
import net.dunyun.framework.lock.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
* 名称：AboutActivity.java
        * 描述：盾云简介界面.
        *
        * @author wuhx
        * @version v1.0
        * @date：2016/04/13 下午11:52:13
        * @Copyright:
 */
public class AboutActivity extends BaseActivity {

    private Context context = null;
    @Bind(R.id.about_logo_text) TextView
            about_logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //baseSetContentView(R.layout.activity_about);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.about_title), new LeftOnClickListener(), null, null);

        about_logo_text.setText(AppUtil.getPackageInfo(context).versionName);
    }
    /*常见问题*/
    @OnClick(R.id.common_question) void commonQuestion()
    {
        Intent intent = new Intent(AboutActivity.this, AboutCommonQuestionActivity.class);
        intent.putExtra("url", WebUrl.ABUOUT_HELP);
        intent.putExtra("title", getResources().getString(R.string.about_common_question));
        AboutActivity.this.startActivity(intent);
    }
    /*使用帮助*/
    @OnClick(R.id.instructions) void instructions()
    {

        Intent intent = new Intent(AboutActivity.this, AboutCommonQuestionActivity.class);
        intent.putExtra("url", WebUrl.ABUOUT_HELP);
        intent.putExtra("title", getResources().getString(R.string.about_instructions));
        AboutActivity.this.startActivity(intent);
    }
    /*盾云简介*/
    @OnClick(R.id.abstracts) void abstracts()
    {
        AboutActivity.this.startActivity(new Intent(AboutActivity.this, AboutAbstractsActivity.class));
    }
    /*意见反馈*/
    @OnClick(R.id.suggestions) void suggestions()
    {
        AboutActivity.this.startActivity(new Intent(AboutActivity.this, AboutSuggestionActivity.class));
    }
    /*软件更新*/
    @OnClick(R.id.version_update) void versionUpdate()
    {
        update();
    }
    /***
     * 获取更新描述文件，异步返回VersionUpgraderVo
     */
    private void update(){
        mLoadingDialog = DialogUtil.showWaitDialog(context, getResources().getString(R.string.about_update));

        GetVersionNewBiz getVersionNewBiz = new GetVersionNewBiz(new GetVersionNewCallback() {
            @Override
            public void onGetVersionSuccess(final  VersionVo versionVo) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);

                try {
                    int code = Integer.parseInt(versionVo.getCode());
                    int locationVersionCode = Integer.parseInt(AppUtil.getPackageInfo(context).versionCode + "");

                    if (code > locationVersionCode) {
                        DialogUtil.showDialog(context, getResources().getString(R.string.update_title) + "\n" + versionVo.getDescription(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                download(versionVo);
                                dialog.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        ToastUtil.showToast(context, "已是最新版本");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetVersionFailed(String result) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                ToastUtil.showToast(context, "获取更新信息失败，"+result);
            }
        });
        getVersionNewBiz.getVersion();
    }

    /***
     * 下载更新文件
     * @param versionVo
     */
    private void download(VersionVo versionVo){
        mLoadingDialog = DialogUtil.showWaitDialog(context, getResources().getString(R.string.update_download_title));

        VersionUtil.download(versionVo, FileUtil.getFileDownloadDir(context), new DownloadCallback() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFinish(File file) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                install(file);
            }

            @Override
            public void onError(int code, String error) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                ToastUtil.showToast(context, "下载失败");
            }
        });
    }

    /***
     * 安装APK
     * @param file 安装文件
     */
    private void install(File file){
        AppUtil.installApk(context, file);
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
     * @param content 提示内容
     */
    private void showToast(final String content) {
        AboutActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }
}
