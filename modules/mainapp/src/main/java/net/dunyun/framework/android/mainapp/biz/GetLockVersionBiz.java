package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.PageUtil;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.LockVersionVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

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
public class GetLockVersionBiz {
    private GetLockVersionCallback getLockVersionCallback;

    public GetLockVersionBiz(GetLockVersionCallback getLockVersionCallback) {
        this.getLockVersionCallback = getLockVersionCallback;
    }

    public void getLockVersion(String type, String token, String mobile){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("type", type);
        paramsMap.put("token", token);
        paramsMap.put("mobile", mobile);
        HttpUtils.getInstance().httpPost(WebUrl.GETNEWFIRMWARE, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);
                try{
                    LockVersionVo lockVersionVo = JsonUtil.parseObject(responseString, LockVersionVo.class);
                    getLockVersionCallback.onLockVersionSuccess(lockVersionVo);
                }catch (Exception e){
                    e.printStackTrace();
                    getLockVersionCallback.onLockVersionFailed("网络异常");
                }
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                getLockVersionCallback.onLockVersionFailed(responseString);
            }
        });
    }
}
