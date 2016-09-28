package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.LockVersionVo;
import net.dunyun.framework.android.mainapp.vo.VersionVo;

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
public class GetVersionNewBiz {
    private GetVersionNewCallback getVersionNewCallback;

    public GetVersionNewBiz(GetVersionNewCallback getVersionNewCallback) {
        this.getVersionNewCallback = getVersionNewCallback;
    }

    public void getVersion(){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("type", "1");
        HttpUtils.getInstance().httpPost(WebUrl.GETVERSIONNEW, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);
                try{
                    VersionVo versionVo = JsonUtil.parseObject(responseString, VersionVo.class);
                    getVersionNewCallback.onGetVersionSuccess(versionVo);
                }catch (Exception e){
                    e.printStackTrace();
                    getVersionNewCallback.onGetVersionFailed("网络异常");
                }
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                getVersionNewCallback.onGetVersionFailed(responseString);
            }
        });
    }
}
