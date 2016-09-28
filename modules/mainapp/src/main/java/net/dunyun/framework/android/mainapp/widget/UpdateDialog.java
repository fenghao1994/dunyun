package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import net.dunyun.framework.lock.R;

/**
 * @author chenzp
 * @date 2016/6/3
 * @Copyright:重庆平软科技有限公司
 */
public class UpdateDialog extends Dialog {
    private TextView tv = null;
    private String title = null;
    private RotateAnimation animation;
    private ImageView iv_dialog_icon;

    public UpdateDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    public UpdateDialog(Context context, String title) {
        super(context, R.style.loadingDialogStyle);
        this.title = title;
    }

    private UpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_dialog);
        tv = (TextView)this.findViewById(R.id.dialog_progressText);
        iv_dialog_icon = (ImageView)this.findViewById(R.id.iv_dialog_icon);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(-1);//重复次数
        animation.setFillAfter(true);//停在最后
        animation.setDuration(3000);
        setCanceledOnTouchOutside(false);

        iv_dialog_icon.startAnimation(animation);
        tv.setText((title == null) ? "请等待..." : title);
    }

    public void update(String content){
        tv.setText(content);
    }
}
