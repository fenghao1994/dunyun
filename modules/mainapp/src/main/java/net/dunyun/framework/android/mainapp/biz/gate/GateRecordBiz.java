package net.dunyun.framework.android.mainapp.biz.gate;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateRecordVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;

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
public class GateRecordBiz {
    private GateRecordCallback gateRecordCallback;

    public GateRecordBiz(GateRecordCallback gateRecordCallback) {
        this.gateRecordCallback = gateRecordCallback;
    }

    public void get(final UserVo userVo, String communityId, String logType){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("communityId", communityId);
        paramsMap.put("logType", logType);

        HttpUtils.getInstance().httpPost(WebUrl.GETGATELOGS, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                List<GateRecordVo> gateRecordVos = JsonUtil.parseArray(responseString, new TypeToken<List<GateRecordVo>>() {
                }.getType());
                gateRecordCallback.onGateSuccess(gateRecordVos);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                gateRecordCallback.onGateFailed(responseString);
            }
        });


    }
}
