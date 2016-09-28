package net.dunyun.framework.android.mainapp.fund;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.activity.LoginActivity;
import net.dunyun.framework.android.mainapp.activity.WelcomeActivity;
import net.dunyun.framework.android.mainapp.fund.common.Constants;
import net.dunyun.framework.android.mainapp.fund.widget.GestureContentView;
import net.dunyun.framework.android.mainapp.fund.widget.GestureDrawline;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.RoundImageView;
import net.dunyun.framework.lock.R;

/**
 * 
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends Activity implements View.OnClickListener {
	/** 手机号码*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private RelativeLayout mTopLayout;
	private TextView mTextTitle;
	private TextView mTextCancel;
	private RoundImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private TextView mTextOther;
	private String mParamPhoneNumber;
	private long mExitTime = 0;
	private int mParamIntentCode;
	
	/**错误次数*/
	private int errorTimes = 0;
    private UserVo userVo;
    private String phone;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}
	
	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	}
	
	private void setUpViews() {
		mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mTextCancel = (TextView) findViewById(R.id.text_cancel);
		mImgUserLogo = (RoundImageView) findViewById(R.id.user_logo);
		mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextOther = (TextView) findViewById(R.id.text_other_account);
		
		String inputCode = SharedUtil.getString(GestureVerifyActivity.this, Constants.INPUT_CODE);

        String userVoStr = SharedUtil.getString(this, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            String avatarUrl = userVo.getAvatarUrl();

            if(avatarUrl != null && avatarUrl.startsWith("http")){
                Picasso.with(this).load(avatarUrl)
                        .placeholder(R.drawable.photo)
                        .error(R.drawable.photo)
                        .into(mImgUserLogo);
            }
        }

		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, true, inputCode,
				new GestureDrawline.GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						errorTimes = 0;
						mGestureContentView.clearDrawlineState(0L);
						Toast.makeText(GestureVerifyActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
						setResult(1);
//						Intent intent = new Intent(GestureVerifyActivity.this, WelcomeActivity.class);
//						intent.putExtra("fromGesture", true);
//						startActivity(intent);
						GestureVerifyActivity.this.finish();
					}

					@Override
					public void checkedFail() {
						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText(Html
								.fromHtml("<font color='#c70c1e'>密码错误</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						if(errorTimes >= 5){
//							startActivity(new Intent(GestureVerifyActivity.this, LoginActivity.class));
//							GestureVerifyActivity.this.finish();
						}
						
						errorTimes++;
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}
	
	private void setUpListeners() {
		mTextCancel.setOnClickListener(this);
		mTextForget.setOnClickListener(this);
		mTextOther.setOnClickListener(this);
	}
	
	private String getProtectedMobile(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(phoneNumber.subSequence(0,3));
		builder.append("****");
		builder.append(phoneNumber.subSequence(7,11));
		return builder.toString();
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cancel:
			setResult(2);
			this.finish();
			break;
		case R.id.text_forget_gesture://忘记密码

			SharedUtil.putString(GestureVerifyActivity.this, Constants.PASSWD_OPEN, "no");
			SharedUtil.putString(GestureVerifyActivity.this, Constants.INPUT_CODE, "");
			SharedUtil.putString(GestureVerifyActivity.this, UserVo.class + "", null);
			setResult(2);
			startActivity(new Intent(GestureVerifyActivity.this, LoginActivity.class));
			GestureVerifyActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
			setResult(2);
			GestureVerifyActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
