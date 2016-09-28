package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.psoft.framework.android.base.ui.view.ToastUtil;

import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 名称：AboutAbstractsActivity.java
 * 描述：盾云简介界面.
 *
 * @author wuhx
 * @version v1.0
 * @date：2016/04/13 下午11:52:13
 * @Copyright:
 */
public class AboutAbstractsActivity extends BaseActivity{


        private Context context = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = this;
            baseSetContentView(R.layout.activity_about_abstracts);
            ButterKnife.bind(this);
            ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
            activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.about_abstracts),new LeftOnClickListener(), null, null);
        }
        /***
         * 提示信息
         * @param content 提示内容
         */
        private void showToast(final String content) {
            AboutAbstractsActivity.this.runOnUiThread(new Runnable() {
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
