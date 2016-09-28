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
public class GateAuthBiz {
    private GateAuthCallback gateAuthCallback;

    public GateAuthBiz(GateAuthCallback gateAuthCallback) {
        this.gateAuthCallback = gateAuthCallback;
    }

    public void auth(final UserVo userVo, String id, String phone, String chainType, String grantBdt, String grantEdt, String grantNum){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", phone);
        paramsMap.put("id", id);
        paramsMap.put("chainType", chainType);

        if(StringUtils.isNotNullOrEmpty(grantBdt)){
            paramsMap.put("grantBdt", grantBdt);
        }
        if(StringUtils.isNotNullOrEmpty(grantEdt)){
            paramsMap.put("grantEdt", grantEdt);
        }
        if(StringUtils.isNotNullOrEmpty(grantNum)){
            paramsMap.put("grantNum", grantNum);
        }

        HttpUtils.getInstance().httpPost(WebUrl.ADDKEYBYMOBILE, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                gateAuthCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                gateAuthCallback.onFailed(responseString);
            }
        });


    }
}
