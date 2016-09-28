package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

import java.util.List;

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
public class GetKeysByMacBiz {
    private GetKeysByMacCallback getKeysByMacCallback;

    public GetKeysByMacBiz(GetKeysByMacCallback getKeysByMacCallback) {
        this.getKeysByMacCallback = getKeysByMacCallback;
    }

    public void getKeys(String macCode, String token, String mobile){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", macCode);
        paramsMap.put("token", token);
        paramsMap.put("mobile", mobile);
        HttpUtils.getInstance().httpPost(WebUrl.GETKEYS_BY_MAC, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);

                List<KeyVo> keyVoList = JsonUtil.parseArray(responseString, new TypeToken<List<KeyVo>>() {
                }.getType());
                getKeysByMacCallback.onKeysSuccess(keyVoList);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                getKeysByMacCallback.onKeysFailed(responseString);
            }
        });
    }
}
