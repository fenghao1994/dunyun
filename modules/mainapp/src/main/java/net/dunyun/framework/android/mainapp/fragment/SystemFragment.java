package net.dunyun.framework.android.mainapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.dunyun.framework.android.mainapp.activity.AboutActivity;
import net.dunyun.framework.android.mainapp.activity.ChangeActivity;
import net.dunyun.framework.android.mainapp.activity.FunctionModuleUpdateActivity;
import net.dunyun.framework.android.mainapp.activity.MeSettingActivity;
import net.dunyun.framework.android.mainapp.activity.MsgSettingActivity;
import net.dunyun.framework.android.mainapp.activity.UpdatePasswordActivity;
import net.dunyun.framework.android.mainapp.activity.UpdateUserInfoActivity;
import net.dunyun.framework.android.mainapp.activity.UserInfoActivity;
import net.dunyun.framework.android.mainapp.biz.GetKeysBiz;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.fund.GestureEditActivity;
import net.dunyun.framework.android.mainapp.fund.GestureVerifyActivity;
import net.dunyun.framework.android.mainapp.fund.common.Constants;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;

import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.activity.LoginActivity;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.RoundImageView;
import net.dunyun.framework.lock.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统设置，个人资料
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class SystemFragment extends BaseFragment {
    UserVo userVo;
    private boolean isLogin = false;
    private View view;
    @Bind(R.id.rl_msg) RelativeLayout rl_msg;
    @Bind(R.id.rl_shop_cart) RelativeLayout rl_shop_cart;
    @Bind(R.id.tv_phone) TextView tv_phone;
    @Bind(R.id.photo_iv)
    RoundImageView photo_iv;
    @Bind(R.id.cb_gesture) CheckBox cb_gesture;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view =  inflater.inflate(R.layout.fragment_system, null, false);
            ButterKnife.bind(this, view);
            ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
            activityTitleUtil.initTitle(view, view.getResources().getString(R.string.main_tab_4), null, null, null);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }
        if(userVo != null){
            tv_phone.setText(userVo.getMobile());
            isLogin = true;
            if(userVo.getAvatarUrl() != null && !"null".equals(userVo.getAvatarUrl()) && userVo.getAvatarUrl().length() > 6){
                Picasso.with(getActivity()).load(userVo.getAvatarUrl())
                        .placeholder(R.drawable.me_photo)
                        .error(R.drawable.me_photo)
                        .into(photo_iv);
            }
        }else{
            tv_phone.setText("未登录");
            isLogin = false;
        }
       String PASSWD_OPEN = SharedUtil.getString(getActivity(), Constants.PASSWD_OPEN);
        if("yes".equals(PASSWD_OPEN)){
            cb_gesture.setChecked(true);
        }else{
            cb_gesture.setChecked(false);
        }
    }

    public void reFresh(){
        String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }
        if(userVo != null){
            tv_phone.setText(userVo.getNickName().length()>0?userVo.getNickName():userVo.getMobile());
            isLogin = true;
            if(userVo.getAvatarUrl() != null && !"null".equals(userVo.getAvatarUrl()) && userVo.getAvatarUrl().length() > 6){
                Picasso.with(getActivity()).load(userVo.getAvatarUrl())
                        .placeholder(R.drawable.me_photo)
                        .error(R.drawable.me_photo)
                        .into(photo_iv);
            }
        }else{
            tv_phone.setText("未登录");
            isLogin = false;
        }
        String PASSWD_OPEN = SharedUtil.getString(getActivity(), Constants.PASSWD_OPEN);
        if("yes".equals(PASSWD_OPEN)){
            cb_gesture.setChecked(true);
        }else{
            cb_gesture.setChecked(false);
        }

    }

    @OnClick(R.id.rl_msg) void onClickMsg(){
        if(isLogin){
            SystemFragment.this.getActivity().startActivity(new Intent(SystemFragment.this.getActivity(), MsgSettingActivity.class));
        }else{
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }
    @OnClick(R.id.rl_about_us) void onClickAboutUs(){
        SystemFragment.this.getActivity().startActivity(new Intent(SystemFragment.this.getActivity(), AboutActivity.class));
    }

    @OnClick(R.id.rl_key_set) void onClickKeySet(){
        if(isLogin){
            SystemFragment.this.getActivity().startActivity(new Intent(SystemFragment.this.getActivity(), MeSettingActivity.class));
        }else{
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }

    @OnClick(R.id.photo_iv) void onClickPhoto(){
        if(isLogin){
            this.getActivity().startActivity(new Intent(this.getActivity(), UserInfoActivity.class));
        }else{
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }

    //购物车
    @OnClick(R.id.rl_shop_cart) void rl_shop_cart(){
        if(isLogin){

        }else{
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }

    //订单
    @OnClick(R.id.rl_order) void rl_order(){
        if(isLogin){

        }else{
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            String userVoStr = SharedUtil.getString(getActivity(), UserVo.class + "");
            if(userVoStr != null){
                userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            }
            if(userVo != null){
                tv_phone.setText(userVo.getMobile());
                isLogin = true;
                Picasso.with(getActivity()).load(userVo.getAvatarUrl())
                        .placeholder(R.drawable.me_photo)
                        .error(R.drawable.me_photo)
                        .into(photo_iv);
            }else{
                tv_phone.setText("未登录");
                isLogin = false;
            }
        }
    }

    @OnClick(R.id.cb_gesture) void onClickGesture(){
        if(isLogin){
            if(cb_gesture.isChecked()){
                this.getActivity().startActivity(new Intent(this.getActivity(), GestureEditActivity.class));
            }else{
                SharedUtil.putString(getActivity(), Constants.PASSWD_OPEN, "no");
                SharedUtil.putString(getActivity(), Constants.INPUT_CODE, "");
                Toast.makeText(getActivity(), "手势密码已关闭", Toast.LENGTH_SHORT).show();
            }
        }else {
            this.getActivity().startActivityForResult(new Intent(this.getActivity(), LoginActivity.class), 0);
        }
    }

}
