package net.dunyun.framework.android.mainapp.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.dunyun.framework.android.mainapp.activity.FunctionModuleUpdateActivity;
import net.dunyun.framework.android.mainapp.activity.MainActivity;
import net.dunyun.framework.android.mainapp.adapter.FunctionModuleAdapter;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.FunctionModuleVo;

import com.bluelock.utils.StringUtils;
import com.bluelock.weixin.WeiXinPay;
import com.pay.AlipayPay;
import com.pay.PayOrderInfo;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.PackageUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.ViewUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;

import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class HomeFragment extends BaseFragment {
    private Context context;
    private WebView webView;
    private String userName;
    private String userPwd;
    private Map<String, String> parMap = null;

    public static int weixinPayType = BaseResp.ErrCode.ERR_USER_CANCEL;
    public static boolean weixinPayResultSta = false;
    //    private String MainUrl = "http://lock.tljnn.com";
//    private String MainUrl = "http://192.168.1.201:8082";
    private String MainUrl = "http://app.dunyun.net:82";
    private String HomeUrl = "/web_view/indexInWebView.action";

    private View view;
    private String userPar;
    private UserVo userVo;
    private String phone;
    private LinearLayout ll_error;

    public static boolean refresh = false;
    private ProgressBar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            context = getActivity();
            view = inflater.inflate(R.layout.fragment_home, null, false);

            userName = "";
            userPwd = "";
            String userVoStr = SharedUtil.getString(context, UserVo.class + "");
            if (userVoStr != null) {
                userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
                phone = userVo.getMobile();
                userName = userVo.getMobile();
                userPwd = userVo.getPassWord();
                try {
                    userPwd = java.net.URLEncoder.encode(userPwd, "utf-8");
                } catch (Exception e) {

                }

                LogUtil.d("---userName---" + userName + ", " + userPwd);
            }
            userPar = "?name=" + userName + "&pwd=" + userPwd;

            initViews(view);
        }
        return view;
    }

    public void refresh() {
        if (refresh) {
            userName = "";
            userPwd = "";
            String userVoStr = SharedUtil.getString(context, UserVo.class + "");
            if (userVoStr != null) {
                userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
                phone = userVo.getMobile();
                userName = userVo.getMobile();
                userPwd = userVo.getPassWord();
                try {
                    userPwd = java.net.URLEncoder.encode(userPwd, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogUtil.d("---userName---" + userName + ", " + userPwd);
                userPar = "?name=" + userName + "&pwd=" + userPwd+"&latitude="+ MainActivity.latitude+"&longtitude="+MainActivity.longitude;
            }else{
                userPar = "?name=" + userName + "&pwd=" + userPwd+"&latitude="+ MainActivity.latitude+"&longtitude="+MainActivity.longitude+"&clearFlag=Y";
            }

            initViews(view);
            refresh = false;
        }
    }

    private void initViews(View view) {
//        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
//        activityTitleUtil.initTitle(view, getResources().getString(R.string.main_tab_1), null, null, null);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        webView = (WebView) view.findViewById(R.id.main_view_activity_webview);

        ll_error = (LinearLayout) view.findViewById(R.id.ll_error);

        ll_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_error.setVisibility(View.INVISIBLE);
                initView();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MainUrl + HomeUrl + userPar);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置 缓存模式
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
//        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // if (url != null && url.equals("https://m.taobao.com/")) {
                // url =
                // "http://www.jd.com?duoqioDataExchange=zfb&failUrl=http://www.baidu.com&parameter1=1234567819&parameter2=0.01&parameter3=测试";
                // }
                if (url.indexOf("duoqioDataExchange") >= 0) {
                    LogUtil.d(url);
                    webOnClickUrlAnalytic(url);
                    return true;
                } else if (url.indexOf("tel:") >= 0) {
                    dialTelephone(url);
                    return true;
                } else if (url.indexOf("sessionLoseUrl") >= 0) {
                    Toast.makeText(context, "未登陆...", Toast.LENGTH_SHORT)
                            .show();
                    webView.loadUrl(MainUrl + HomeUrl + userPar);
                    return true;
                } else if (url.indexOf("appPayType") >= 0) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                String errorHtml = "<html><body></body></html>";
                view.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
                ToastUtil.showToast(context, "加载失败");

                ll_error.setVisibility(View.VISIBLE);
            }
        });

        bar = (ProgressBar) view.findViewById(R.id.myProgressBar);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (bar != null) {
                        bar.setVisibility(View.GONE);
                    }
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
            }
        });
    }

    /**
     * 拨打电话
     *
     * @param url
     */
    private void dialTelephone(String url) {
        String[] temp = url.split("\\:");
        if (temp.length == 2) {
            // 用intent启动拨打电话
            if (!StringUtils.isBlank(temp[1])) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + temp[1].trim()));
                startActivity(intent);
            }
        }
    }

    /**
     * @param url
     * @return 提交订单监听
     */
    private boolean webOnClickUrlAnalytic(String url) {
        parMap = new HashMap<String, String>();
        String[] temp = url.split("\\?");
        if (temp.length == 2) {
           // parMap.put("successUrl", temp[0]);
            String[] pars = temp[1].split("&");
            for (int i = 0; i < pars.length; i++) {
                String[] par = pars[i].split("=");
                if (par.length == 2) {
                    if(par[0].equals("successUrl")){
                        parMap.put(par[0], MainUrl+par[1]);
                    }else {
                        parMap.put(par[0], par[1]);
                    }
                }
            }
            String payType = parMap.get("duoqioDataExchange");
            String orderNo = parMap.get("parameter1");
            String orderName = "";

            String orderNameTemp = parMap.get("parameter3");
            if(orderNameTemp != null){
                orderName = URLDecoder.decode(orderNameTemp);
            }
            double orderPrice = StringUtils.stringToDouble(parMap
                    .get("parameter2"));
            if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(orderName)
                    || orderPrice <= 0) {
                Toast.makeText(
                        context,
                        context.getResources()
                                .getString(R.string.pay_par_error),
                        Toast.LENGTH_LONG).show();
                return false;
            }

            if (StringUtils.isEquals(payType, "zfb")) {
                zhifubaoPay(orderNo, orderName, orderName, orderPrice);
            } else {
                weixinPay(orderNo, orderName, orderName, orderPrice);
            }
            return true;
        } else {
            Toast.makeText(context,
                    context.getResources().getString(R.string.pay_par_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * 支付宝支付
     */
    private void zhifubaoPay(String orderNo, String orderName, String orderDec,
                             double orderPrice) {
        PayOrderInfo info = new PayOrderInfo();
        info.setOrderName(orderName);
        info.setOrderNo(orderNo);
        info.setOrderPrice(orderPrice + "");
        info.setOrderDec(orderDec);
        AlipayPay pay = new AlipayPay(getActivity(), info) {
            @Override
            public void paySuccessResult(String resultInfo, PayOrderInfo data,
                                         int payStaus) {
                if (payStaus == 0) {
                    if (!StringUtils.isBlank(parMap.get("successUrl")))
                        webView.loadUrl(parMap.get("successUrl")+"&payType=zfb"+"&orderNo="+parMap.get("parameter1"));
                } else {
                    if (!StringUtils.isBlank(parMap.get("failUrl")))
                        webView.loadUrl(MainUrl + parMap.get("failUrl"));
                }
            }
        };
    }



    /**
     * 微信支付
     */
    private void weixinPay(String orderNo, String orderName, String orderDec,
                           double orderPrice) {
        int price = (int) (orderPrice * 100);
        PayOrderInfo info = new PayOrderInfo();
        info.setOrderName(orderName);
        info.setOrderNo(orderNo);
        info.setOrderPrice(price + "");
        info.setOrderDec(orderDec);
        new WeiXinPay(getActivity(), info) {
            @Override
            public void weixinPayErrorResult(PayOrderInfo orderInfo) {
                if (!StringUtils.isBlank(parMap.get("failUrl"))) {
                    webView.loadUrl(MainUrl + parMap.get("failUrl"));
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (weixinPayResultSta) {
            weixinPayResultSta = false;
            weixinPayResult();
        }
    }

    public void updateWebView() {
        if (weixinPayResultSta) {
            weixinPayResultSta = false;
            weixinPayResult();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void weixinPayResult() {
        boolean status = false;
        if (weixinPayType == BaseResp.ErrCode.ERR_OK) {// 支付成功
            status = true;
            if (!StringUtils.isBlank(parMap.get("successUrl"))) {
                webView.loadUrl(parMap.get("successUrl")+"&payType=wx"+"&orderNo="+parMap.get("parameter1"));
            }
        } else if (weixinPayType == BaseResp.ErrCode.ERR_AUTH_DENIED) {// 认证被否决
            Toast.makeText(context,
                    getResources().getString(R.string.authentication_failed),
                    Toast.LENGTH_SHORT).show();
        } else if (weixinPayType == BaseResp.ErrCode.ERR_SENT_FAILED) {// 发送失败
            Toast.makeText(context,
                    getResources().getString(R.string.unable_to_send),
                    Toast.LENGTH_SHORT).show();
        } else if (weixinPayType == BaseResp.ErrCode.ERR_UNSUPPORT) {// 不支持错误
            Toast.makeText(context,
                    getResources().getString(R.string.unsupport_error),
                    Toast.LENGTH_SHORT).show();
        } else if (weixinPayType == BaseResp.ErrCode.ERR_USER_CANCEL) {// 用户取消
            Toast.makeText(context,
                    getResources().getString(R.string.user_canceled),
                    Toast.LENGTH_SHORT).show();
        } else {// 一般错误
            Toast.makeText(context,
                    getResources().getString(R.string.general_errors),
                    Toast.LENGTH_SHORT).show();
        }
        if (!status) {
            if (!StringUtils.isBlank(parMap.get("failUrl"))) {
                webView.loadUrl(MainUrl + parMap.get("failUrl"));
            }
        }
    }
}
