package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class LoginBiz {
    private LoginCallback loginCallback;

    public LoginBiz(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;

    }

    public void login(UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("passWord", userVo.getPassWord());
        paramsMap.put("pushId", userVo.getPushId());
        paramsMap.put("deviceType", "1");
        if(null != userVo.getToken() && userVo.getToken().length() > 10){
            paramsMap.put("token", userVo.getToken());
        }else {
            paramsMap.put("token", "");
        }
        HttpUtils.getInstance().httpPost(WebUrl.LOGIN, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                UserVo userVo = JsonUtil.parseObject(responseString, UserVo.class);
                loginCallback.onSuccess(userVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                loginCallback.onFailed(responseString);
            }
        });
    }

    public void loginOut(final UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("token", userVo.getToken());
        HttpUtils.getInstance().httpPost(WebUrl.LOGINOUT, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                loginCallback.onSuccess(userVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                loginCallback.onFailed(responseString);
            }
        });
    }
}
