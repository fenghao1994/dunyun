package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.dunyun.framework.lock.R;

/**
 * @author chenzp
 * @date 2016/5/5
 * @Copyright:重庆平软科技有限公司
 */
public class WhiteDialog extends Dialog implements View.OnClickListener{
    private String title = null;
    private String content = null;
    private DialogListener dialogListener;
    private Button btn_ok;
    private TextView tv_content;
    private TextView tv_title;

    public WhiteDialog(Context context) {
        super(context, R.style.dunyunDialogStyle);
    }

    public WhiteDialog(Context context, String title,String content,
                       DialogListener dialogListener) {
        super(context, R.style.dunyunDialogStyle);
        this.title = title;
        this.content = content;
        this.dialogListener = dialogListener;
    }

    private WhiteDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.white_dialog);
        tv_title = (TextView)this.findViewById(R.id.tv_title);
        tv_content = (TextView)this.findViewById(R.id.tv_content);
        btn_ok = (Button)this.findViewById(R.id.btn_ok);

        tv_title.setText((title == null) ? "请确认是否删除该用户..." : title);
        tv_content.setText(content);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialogListener.onClick(v);
    }
}