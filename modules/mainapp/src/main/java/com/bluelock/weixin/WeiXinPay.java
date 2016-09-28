package com.bluelock.weixin;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.pay.PayOrderInfo;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.dunyun.framework.android.mainapp.fragment.HomeFragment;
import net.dunyun.framework.lock.R;

public abstract class WeiXinPay {
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";

	private Context context;
	private PayReq req;
	private IWXAPI msgApi;
	private StringBuffer sb;
	private PayOrderInfo orderInfo;
	public abstract void weixinPayErrorResult(PayOrderInfo orderInfo);

	public WeiXinPay(Context context, PayOrderInfo orderInfo) {
		this.context = context;
		this.orderInfo = orderInfo;
		msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
		msgApi.registerApp(Constants.APP_ID);
		boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (isPaySupported) {
			req = new PayReq();
			sb = new StringBuffer();
			new GetPrepayIdTask().execute();
		} else {
			Toast.makeText(context,
					context.getString(R.string.weixin_nonsupport),
					Toast.LENGTH_LONG).show();
		}
	}


	/**
	 *
	 *
	 * @author Administrator
	 *
	 */
	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			dialog.setTitle(context.getString(R.string.app_tip));
			dialog.setMessage(context.getString(R.string.getting_prepayid));
			dialog.show();
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (result != null) {
				if (result.get("return_code").equals("SUCCESS")) {
					if (result.get("result_code").equals("SUCCESS")) {
						sb.append("prepay_id\n" + result.get("prepay_id")
								+ "\n\n");
						PayReq(result);
					} else {
						weixinPayErrorResult(orderInfo);
						Toast.makeText(context, result.get("err_code_des"),
								Toast.LENGTH_LONG).show();
					}
				} else {
					weixinPayErrorResult(orderInfo);
					Toast.makeText(context, result.get("return_msg"),
							Toast.LENGTH_LONG).show();
				}
			} else {
				weixinPayErrorResult(orderInfo);
				Toast.makeText(context,
						context.getString(R.string.weixin_request_error),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			Map<String, String> xml = null;
			String entity = genProductArgs();
			if (entity != null) {
				byte[] buf = Util.httpPost(url, entity);
				if (buf != null) {
					String content = new String(buf);
					xml = decodeXml(content);
				}
			}
			return xml;
		}
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", orderInfo
					.getOrderName()));
			if (orderInfo.getOrderDec() != null
					&& orderInfo.getOrderDec().length() > 0) {
				packageParams.add(new BasicNameValuePair("detail", orderInfo
						.getOrderDec()));
			}
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					Constants.NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no", orderInfo
					.getOrderNo()));
			String ip = getLocalIPV4Address();
			if (ip != null && ip.length() > 0) {
				ip = "127.0.0.1";
			}
			packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
			packageParams.add(new BasicNameValuePair("total_fee", orderInfo
					.getOrderPrice()));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			// return xmlstring;
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	public Map<String, String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:
						if ("xml".equals(nodeName) == false) {
							xml.put(nodeName, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}
			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	private void PayReq(Map<String, String> result) {
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = result.get("prepay_id");
		req.packageValue = "prepay_id=" + result.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		sb.append("sign\n" + req.sign + "\n\n");
		HomeFragment.weixinPayResultSta = true;
		HomeFragment.weixinPayType = BaseResp.ErrCode.ERR_USER_CANCEL;
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	/**
	 *
	 *
	 * @return
	 */
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 *
	 *
	 * @return
	 */
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion", appSign);
		return appSign;
	}

	/**
	 * 获取ip
	 *
	 * @return
	 */
	public static String getLocalIPV4Address() {
		try {
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
					.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf
						.getInetAddresses(); enumIPAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIPAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress.getAddress().length == 4) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			System.err.print("error");
		}
		return null;
	}

}
