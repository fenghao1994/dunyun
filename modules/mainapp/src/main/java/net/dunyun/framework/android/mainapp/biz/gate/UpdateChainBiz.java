package net.dunyun.framework.android.mainapp.biz.gate;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.LogUtil;
import com.raizlabs.android.dbflow.StringUtils;

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
public class UpdateChainBiz {
    private UpdateChainCallback updateChainCallback;

    public UpdateChainBiz(UpdateChainCallback updateChainCallback) {
        this.updateChainCallback = updateChainCallback;
    }

    public void updateChain(String id, String chainName, String token, String isShare, String isPush,String grantNum){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", token);
        paramsMap.put("id", id);
        if(StringUtils.isNotNullOrEmpty(chainName)){
            paramsMap.put("chainName", chainName);
        }
        if(StringUtils.isNotNullOrEmpty(isShare)){
            paramsMap.put("isShare", isShare);
        }
        if(StringUtils.isNotNullOrEmpty(isPush)){
            paramsMap.put("isPush", isPush);
        }
        if(StringUtils.isNotNullOrEmpty(grantNum)){
            paramsMap.put("grantNum", grantNum);
        }

        HttpUtils.getInstance().httpPost(WebUrl.UPDATE_CHAIN, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                updateChainCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                updateChainCallback.onFailed(responseString);
            }
        });


    }
}
