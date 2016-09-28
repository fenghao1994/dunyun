package net.dunyun.framework.android.mainapp.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.biz.UserInfoPictureCallback;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.UserUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.RoundImageView;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人资料界面
 * @author chenzp
 * @version v1.0
 * @date2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class UserInfoActivity extends BaseActivity {

    private Context context = null;
    private Uri photoUri;
    @Bind(R.id.photo_iv)
    RoundImageView roundImageView;

    @Bind(R.id.tv_nick_name)
    TextView tv_nick_name;


    private UserVo userVo;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setTitle("个人资料");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            String avatarUrl = userVo.getAvatarUrl();

            if(avatarUrl != null && avatarUrl.startsWith("http")){
                Picasso.with(context).load(avatarUrl)
                        .placeholder(R.drawable.me_photo)
                        .error(R.drawable.me_photo)
                        .into(roundImageView);
            }

            tv_nick_name.setText(userVo.getNickName());
        }

    }
    @OnClick(R.id.rl_photo) void photoOnclick(){
        UserInfoActivity.this.startActivity(new Intent(UserInfoActivity.this, UserInfoPictureActivity.class));
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
    @OnClick(R.id.title_left_btn) void leftOnclick(){
        UserInfoActivity.this.finish();
    }
    @OnClick(R.id.rl_nick) void changeNickname(){
        UserInfoActivity.this.startActivity(new Intent(UserInfoActivity.this, UserInfoChangeNameActivity.class));
    }
    @OnClick(R.id.rl_update_pwd) void onClickAboutUs(){
        UserInfoActivity.this.startActivity(new Intent(UserInfoActivity.this, ChangeActivity.class));
    }

    @OnClick(R.id.btn_exit) void exitOnclick(){
        UserUtil.loginOut(userVo);

        SharedUtil.clear(context);
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
        startActivity(intent);
        finish();
    }

    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        UserInfoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }
}
