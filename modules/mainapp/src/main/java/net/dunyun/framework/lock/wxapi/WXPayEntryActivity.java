package net.dunyun.framework.lock.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bluelock.weixin.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.dunyun.framework.android.mainapp.fragment.HomeFragment;
import net.dunyun.framework.lock.R;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private Context context;
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		context = this;
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			HomeFragment.weixinPayResultSta = true;
			HomeFragment.weixinPayType = resp.errCode;
			finish();
			// new AlertDialog.Builder(new ContextThemeWrapper(context,
			// R.style.AppTheme_Dialog))
			// .setTitle(R.string.app_tip)
			// .setMessage(
			// getString(
			// R.string.pay_result_callback_msg,
			// (resp.errStr == null ? getString(R.string.pay_unknown_error)
			// : resp.errStr)
			// + ",code="
			// + String.valueOf(resp.errCode)))
			// .setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// finish();
			// }
			// }).show();
		}
	}
}