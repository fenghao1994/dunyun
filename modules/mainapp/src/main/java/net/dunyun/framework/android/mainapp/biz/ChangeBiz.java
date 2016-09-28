package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

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
public class ChangeBiz {

    private ChangeCallback mChangeCallback;

    public ChangeBiz(ChangeCallback changeCallback) {
        this.mChangeCallback = changeCallback;

    }

    public void changeBiz(final UserVo userVo,String newpassWord,String passWord){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("oldPW", passWord);
        paramsMap.put("newPW", newpassWord);
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", userVo.getMobile());
        LogUtil.i("sendData",userVo.getPassWord()+""+newpassWord+""+userVo.getToken()+""+userVo.getMobile()+"");
        HttpUtils.getInstance().httpPost(WebUrl.CHANGE, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                mChangeCallback.onSuccess(userVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                mChangeCallback.onFailed(responseString);
            }
        });
    }
}
