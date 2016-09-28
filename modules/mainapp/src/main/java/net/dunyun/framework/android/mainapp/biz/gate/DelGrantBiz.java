package net.dunyun.framework.android.mainapp.biz.gate;

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
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class DelGrantBiz {
    private DelGrantCallback delGrantCallback;

    public DelGrantBiz(DelGrantCallback delGrantCallback) {
        this.delGrantCallback = delGrantCallback;
    }

    public void delete(final UserVo userVo, String id){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("id", id);

        HttpUtils.getInstance().httpPost(WebUrl.REMOVECHAIN, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);

                LogUtil.d(responseString);
                delGrantCallback.onDelSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                delGrantCallback.onDelFailed(responseString);
            }
        });


    }
}
