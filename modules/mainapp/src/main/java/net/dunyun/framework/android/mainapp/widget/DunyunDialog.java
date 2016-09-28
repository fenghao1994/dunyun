package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.dunyun.framework.lock.R;

/**
 * @author chenzp
 * @date 2016/5/5
 * @Copyright:重庆平软科技有限公司
 */
public class DunyunDialog extends Dialog implements android.view.View.OnClickListener{
    private TextView tv = null;
    private ImageView iv = null;
    private String title = null;
    private RotateAnimation animation;
    private Button btn_exit;
    DialogListener dialogListener;

    public DunyunDialog(Context context) {
        super(context, R.style.dunyunDialogStyle);
    }

    public DunyunDialog(Context context, String title, DialogListener dialogListener) {
        super(context, R.style.dunyunDialogStyle);
        this.title = title;
        this.dialogListener = dialogListener;

        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(-1);//重复次数
        animation.setFillAfter(true);//停在最后
        animation.setDuration(3000);
    }

    private DunyunDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dunyun_dialog);
        tv = (TextView)this.findViewById(R.id.tv_title);
        iv = (ImageView)this.findViewById(R.id.iv_dialog);
        btn_exit = (Button)this.findViewById(R.id.btn_exit);

        iv.startAnimation(animation);
        tv.setText((title == null) ? "请稍等..." : title);

        btn_exit.setOnClickListener(this);
    }

    public void startAnimation(){
        iv.startAnimation(animation);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        animation.cancel();
    }

    public void stop(){
        animation.cancel();
    }

    @Override
    public void onClick(View v) {
        dialogListener.onClick(v);
    }
}