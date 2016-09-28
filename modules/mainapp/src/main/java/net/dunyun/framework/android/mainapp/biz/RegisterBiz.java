package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;

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
public class RegisterBiz {
    RegisterCallback registerCallback;

    public RegisterBiz(RegisterCallback registerCallback){
        this.registerCallback = registerCallback;

    }

    public void register(UserVo userVo, String authCode){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("passWord", userVo.getPassWord());
        paramsMap.put("deviceType", userVo.getDeviceType());
        paramsMap.put("pushId", userVo.getPushId());
        paramsMap.put("authCode", authCode);

        HttpUtils.getInstance().httpPost(WebUrl.REGISTER, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                if(statusCode == 100){
                    UserVo userVo = new UserVo();
                    registerCallback.onRegSuccess(userVo);
                }else {
                    registerCallback.onRegFailed(responseString);
                }

            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                registerCallback.onRegFailed(responseString);
            }
        });
    }
}
