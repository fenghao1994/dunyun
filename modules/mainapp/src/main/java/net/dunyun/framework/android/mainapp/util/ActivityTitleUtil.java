package net.dunyun.framework.android.mainapp.util;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import net.dunyun.framework.lock.R;

/**
 * 标题栏的处理
 * @author chenzp
 * @date 2015/11/2
 * @Copyright:重庆平软科技有限公司
 */
public class ActivityTitleUtil {

    Button right_two_btn;
    private TextView message_id;

    /***
     * 初始化 activity的标题栏
     *
     * @param view 当前view
     * @param title_center 标题
     * @param left_btn_click 左按钮点击事件
     * @param right_one_click 右一按钮点击事件
     * @param right_two_click 右二按钮点击事件
     */
    public void initTitle(View view, String title_center, View.OnClickListener left_btn_click
                        , View.OnClickListener right_one_click,
                          View.OnClickListener right_two_click){
        ImageButton title_left_btn = (ImageButton) view.findViewById(R.id.title_left_btn);
        Button right_one_btn = (Button) view.findViewById(R.id.right_one_btn);
        right_two_btn = (Button) view.findViewById(R.id.right_two_btn);
        message_id = (TextView) view.findViewById(R.id.message_id);

        TextView title_left_txt = (TextView) view.findViewById(R.id.title_left_txt);
        TextView title_center_txt = (TextView) view.findViewById(R.id.title_center_txt);
        if(left_btn_click != null){
            title_left_btn.setOnClickListener(left_btn_click);
            title_left_txt.setOnClickListener(left_btn_click);
        }else{
            title_left_btn.setVisibility(View.INVISIBLE);
        }
        if(right_one_click != null){
            right_one_btn.setOnClickListener(right_one_click);
        }else{
            right_one_btn.setVisibility(View.INVISIBLE);
        }
        if(right_two_click != null){
            right_two_btn.setOnClickListener(right_two_click);
            right_two_btn.setVisibility(View.VISIBLE);
        }else{
            right_two_btn.setVisibility(View.INVISIBLE);
        }
        if(title_center != null){
            title_center_txt.setText(title_center);
        }else{
            title_center_txt.setVisibility(View.INVISIBLE);
        }


    }

    public void setRightBtnText(String text){
        right_two_btn.setVisibility(View.VISIBLE);
        right_two_btn.setText(text);
    }

    public void showMsg(String msg){
        if(msg.equals("0")){
            message_id.setVisibility(View.INVISIBLE);
        }else{
            message_id.setVisibility(View.VISIBLE);
        }
        message_id.setText(msg);
    }

    public void setRightBtnBg(int res){
        right_two_btn.setVisibility(View.VISIBLE);
        right_two_btn.setBackgroundResource(res);
    }


}
