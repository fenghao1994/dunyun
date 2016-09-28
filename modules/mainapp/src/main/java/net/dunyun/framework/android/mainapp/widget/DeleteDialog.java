package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
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
public class DeleteDialog extends Dialog implements android.view.View.OnClickListener{
    private TextView tv = null;
    private String title = null;
    private DialogListener dialogListener;
    private Button btn_ok;
    private Button btn_cancel;

    public DeleteDialog(Context context) {
        super(context, R.style.dunyunDialogStyle);
    }

    public DeleteDialog(Context context, String title,
                        DialogListener dialogListener) {
        super(context, R.style.dunyunDialogStyle);
        this.title = title;
        this.dialogListener = dialogListener;
    }

    private DeleteDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_dialog);
        tv = (TextView)this.findViewById(R.id.tv_title);
        btn_ok = (Button)this.findViewById(R.id.btn_ok);
        btn_cancel = (Button)this.findViewById(R.id.btn_cancel);

        tv.setText((title == null) ? "请确认是否删除该用户..." : title);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialogListener.onClick(v);
    }
}