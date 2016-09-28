package net.dunyun.framework.android.mainapp.biz.gate;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.AreaVo;

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
public class GateAddBiz {
    private GateAddCallback areaAddCallback;

    public GateAddBiz(GateAddCallback areaAddCallback) {
        this.areaAddCallback = areaAddCallback;
    }

    public void add1(final UserVo userVo, String communityId, String reason){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("communityId", communityId);
        paramsMap.put("reason", reason);

        HttpUtils.getInstance().httpPost(WebUrl.GATE_ADD, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                areaAddCallback.onGateSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                areaAddCallback.onGateFailed(responseString);
            }
        });
    }

    public void add(final UserVo userVo, String applyMbl, String reason){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("applyMbl", applyMbl);
        paramsMap.put("reason", reason);
        paramsMap.put("mark", "");

        HttpUtils.getInstance().httpPost(WebUrl.GATE_ADD_FZ, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                areaAddCallback.onGateSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                areaAddCallback.onGateFailed(responseString);
            }
        });
    }
//    gate/chainApply/applyFz.do
}
