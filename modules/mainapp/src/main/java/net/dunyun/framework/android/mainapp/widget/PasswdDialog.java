package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.dunyun.framework.lock.R;

/**
 * @author chenzp
 * @date 2016/5/5
 * @Copyright:重庆平软科技有限公司
 */
public class PasswdDialog extends Dialog implements View.OnClickListener{
    private DialogListener dialogListener;
    private Button btn_ok;
    private Button btn_del;

    private ImageView iv_number1;
    private ImageView iv_number2;
    private ImageView iv_number3;
    private ImageView iv_number4;
    private ImageView iv_number5;
    private ImageView iv_number6;

    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;

    private StringBuffer sb;

    public PasswdDialog(Context context) {
        super(context, R.style.dunyunDialogStyle);
    }

    public PasswdDialog(Context context,
                        DialogListener dialogListener) {
        super(context, R.style.dunyunDialogStyle);
        this.dialogListener = dialogListener;
    }

    private PasswdDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.passwd_dialog);
        iv_number1 = (ImageView)this.findViewById(R.id.iv_number1);
        iv_number2 = (ImageView)this.findViewById(R.id.iv_number2);
        iv_number3 = (ImageView)this.findViewById(R.id.iv_number3);
        iv_number4 = (ImageView)this.findViewById(R.id.iv_number4);
        iv_number5 = (ImageView)this.findViewById(R.id.iv_number5);
        iv_number6 = (ImageView)this.findViewById(R.id.iv_number6);

        btn_0 = (Button)this.findViewById(R.id.btn_0);
        btn_1 = (Button)this.findViewById(R.id.btn_1);
        btn_2 = (Button)this.findViewById(R.id.btn_2);
        btn_3 = (Button)this.findViewById(R.id.btn_3);
        btn_4 = (Button)this.findViewById(R.id.btn_4);
        btn_5 = (Button)this.findViewById(R.id.btn_5);
        btn_6 = (Button)this.findViewById(R.id.btn_6);
        btn_7 = (Button)this.findViewById(R.id.btn_7);
        btn_8 = (Button)this.findViewById(R.id.btn_8);
        btn_9 = (Button)this.findViewById(R.id.btn_9);

        btn_ok = (Button)this.findViewById(R.id.btn_ok);
        btn_del = (Button)this.findViewById(R.id.btn_del);

        btn_ok.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);

        sb = new StringBuffer();
    }

    @Override
    public void onClick(View v) {
        dialogListener.onClick(v);

        if(v.getId() == R.id.btn_del){
            if(sb.length() > 0){
                sb.deleteCharAt(sb.length()-1);
                display();
            }
        }else if(v.getId() == R.id.btn_ok){

        }else {
            if(sb.length() < 6){
                switch (v.getId()){
                    case R.id.btn_0:
                        sb.append("0");
                        break;
                    case R.id.btn_1:
                        sb.append("1");
                        break;
                    case R.id.btn_2:
                        sb.append("2");
                        break;
                    case R.id.btn_3:
                        sb.append("3");
                        break;
                    case R.id.btn_4:
                        sb.append("4");
                        break;
                    case R.id.btn_5:
                        sb.append("5");
                        break;
                    case R.id.btn_6:
                        sb.append("6");
                        break;
                    case R.id.btn_7:
                        sb.append("7");
                        break;
                    case R.id.btn_8:
                        sb.append("8");
                        break;
                    case R.id.btn_9:
                        sb.append("9");
                        break;
                }
                display();
            }
        }
    }

    private void display(){
        iv_number1.setImageResource(R.drawable.passwd_number_02);
        iv_number2.setImageResource(R.drawable.passwd_number_02);
        iv_number3.setImageResource(R.drawable.passwd_number_02);
        iv_number4.setImageResource(R.drawable.passwd_number_02);
        iv_number5.setImageResource(R.drawable.passwd_number_02);
        iv_number6.setImageResource(R.drawable.passwd_number_02);
        int length = sb.length();
        switch (length){
            case 1:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                break;
            case 2:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                iv_number2.setImageResource(R.drawable.passwd_number_01);
                break;
            case 3:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                iv_number2.setImageResource(R.drawable.passwd_number_01);
                iv_number3.setImageResource(R.drawable.passwd_number_01);
                break;
            case 4:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                iv_number2.setImageResource(R.drawable.passwd_number_01);
                iv_number3.setImageResource(R.drawable.passwd_number_01);
                iv_number4.setImageResource(R.drawable.passwd_number_01);
                break;
            case 5:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                iv_number2.setImageResource(R.drawable.passwd_number_01);
                iv_number3.setImageResource(R.drawable.passwd_number_01);
                iv_number4.setImageResource(R.drawable.passwd_number_01);
                iv_number5.setImageResource(R.drawable.passwd_number_01);
                break;
            case 6:
                iv_number1.setImageResource(R.drawable.passwd_number_01);
                iv_number2.setImageResource(R.drawable.passwd_number_01);
                iv_number3.setImageResource(R.drawable.passwd_number_01);
                iv_number4.setImageResource(R.drawable.passwd_number_01);
                iv_number5.setImageResource(R.drawable.passwd_number_01);
                iv_number6.setImageResource(R.drawable.passwd_number_01);
                break;
        }
    }

    public String getPasswd(){
        return sb.toString();
    }
}