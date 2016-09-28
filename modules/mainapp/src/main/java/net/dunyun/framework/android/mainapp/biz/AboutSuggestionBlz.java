package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
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
 * @date：2016-04-13
 * @Copyright:
 *
 */
public class AboutSuggestionBlz {

    private AboutSuggestionCallback mAboutSuggestionCallback;

    public AboutSuggestionBlz(AboutSuggestionCallback aboutSuggestionCallback) {
        this.mAboutSuggestionCallback = aboutSuggestionCallback;

    }

    public void aboutSuggestionBlz(final UserVo userVo,String suggestionstext){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("content", suggestionstext);
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("deviceType", "android");
        paramsMap.put("token", userVo.getToken());

        LogUtil.i("AboutSuggestionBlzs+endData", "android" + " " + suggestionstext + " " + userVo.getToken() + " " + userVo.getMobile() + "");
        HttpUtils.getInstance().httpPost(WebUrl.ABUOUT_SUGGESTION, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                mAboutSuggestionCallback.onSuccess(userVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                mAboutSuggestionCallback.onFailed(responseString);
            }
        });
    }
}