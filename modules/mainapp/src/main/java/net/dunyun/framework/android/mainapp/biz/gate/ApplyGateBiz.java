package net.dunyun.framework.android.mainapp.biz.gate;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.LogUtil;
import com.raizlabs.android.dbflow.StringUtils;

import net.dunyun.framework.android.mainapp.common.WebUrl;

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
public class ApplyGateBiz {
    private ApplyGateCallback applyGateCallback;

    public ApplyGateBiz(ApplyGateCallback applyGateCallback) {
        this.applyGateCallback = applyGateCallback;
    }

    public void apply(String id, String status, String chainId){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("id", id);
        paramsMap.put("status", status);
        if(StringUtils.isNotNullOrEmpty(chainId)){
            paramsMap.put("chainId", chainId);
        }
        HttpUtils.getInstance().httpPost(WebUrl.HANDLEFZAPPLY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                applyGateCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                applyGateCallback.onFailed(responseString);
            }
        });


    }
}
