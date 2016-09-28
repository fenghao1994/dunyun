package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;

/**
 * <DL>
 * <DD>重设用户密码.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class UserResetPwdBiz {
    UserResetPwdCallback userResetPwdCallback;

    public UserResetPwdBiz(UserResetPwdCallback userResetPwdCallback){
        this.userResetPwdCallback = userResetPwdCallback;

    }

    public void resetPwd(UserVo userVo, String authCode){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("passWord", userVo.getPassWord());
        paramsMap.put("authCode", authCode);

        HttpUtils.getInstance().httpPost(WebUrl.RESET_PWD, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                userResetPwdCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                userResetPwdCallback.onFailed(responseString);
            }
        });
    }
}
