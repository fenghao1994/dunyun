package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;


/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-11
 * @Copyright:
 *
 */
public class UserInfoChangeBlz {

    private UserInfoChangeCallback m;

    public UserInfoChangeBlz(UserInfoChangeCallback m) {
        this.m = m;
    }

    public void userInfoChange(final UserVo userVo, final UserVo newUserVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();

        if(!StrUtil.isEmpty(newUserVo.getNickName())){
            paramsMap.put("nickName", newUserVo.getNickName());
        }
        if(!StrUtil.isEmpty(newUserVo.getQq())){
            paramsMap.put("qq", newUserVo.getQq());
        }
        if(!StrUtil.isEmpty(newUserVo.getAvatarUrl())){
            paramsMap.put("avataUrl", newUserVo.getAvatarUrl());
        }
        if(!StrUtil.isEmpty(newUserVo.getPushId())){
            paramsMap.put("pushId", newUserVo.getPushId());
        }
        if(!StrUtil.isEmpty(newUserVo.getIsPush())){
            paramsMap.put("isPush", newUserVo.getIsPush());
        }
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", userVo.getMobile());

        HttpUtils.getInstance().httpPost(WebUrl.CHANGE_NAME, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                m.onSuccess(userVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                m.onFailed(responseString);
            }
        });
    }
}