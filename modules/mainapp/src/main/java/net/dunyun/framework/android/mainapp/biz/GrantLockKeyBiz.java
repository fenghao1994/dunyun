package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.util.List;

/**
 * <DL>
 * <DD>授权.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class GrantLockKeyBiz {
    private GrantLockKeyCallback grantLockKeyCallback;

    public GrantLockKeyBiz(GrantLockKeyCallback grantLockKeyCallback) {
        this.grantLockKeyCallback = grantLockKeyCallback;
    }

    public void grantLockKey(String macCode, int keyIndex, String keyName, String mobile,
                             String grantBdt, String grantEdt, String grantNum,
                             String grantPwd, String token, String addTm){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", macCode);
        paramsMap.put("keyIndex", String.valueOf(keyIndex));
        paramsMap.put("mobile", mobile);
        paramsMap.put("keyName", keyName);
        paramsMap.put("grantBdt", grantBdt);
        paramsMap.put("grantEdt", grantEdt);
        paramsMap.put("grantNum", grantNum);
        paramsMap.put("grantPwd", grantPwd);
        paramsMap.put("token", token);
        paramsMap.put("addTm", addTm);
        HttpUtils.getInstance().httpPost(WebUrl.GRANTLOCKKEY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                grantLockKeyCallback.onGrantLockKeySuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                grantLockKeyCallback.onGrantLockKeyFailed(responseString);
            }
        });
    }
}
