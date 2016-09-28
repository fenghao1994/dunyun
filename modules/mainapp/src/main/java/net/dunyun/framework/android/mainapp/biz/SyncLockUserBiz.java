package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

import org.json.JSONObject;

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
public class SyncLockUserBiz {
    private SyncLockUserCallback syncLockUserCallback;

    public SyncLockUserBiz(SyncLockUserCallback syncLockUserCallback) {
        this.syncLockUserCallback = syncLockUserCallback;
    }

    /***
     * @param token
     * @param keyVo
     */
    public void syncLockUser(KeyVo keyVo, String lockKeys, String token, String mobile) {
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();

            paramsMap.put("macCode", keyVo.getMacCode());
            paramsMap.put("lockKeys", "[" + lockKeys + "]");
            paramsMap.put("token", token);
            paramsMap.put("mobile", mobile);

            HttpUtils.getInstance().httpPost(WebUrl.SYNCLOCK, paramsMap, new BaseHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String responseString) {
                    super.onSuccess(statusCode, responseString);
                    syncLockUserCallback.onSyncLockUserSuccess(responseString);
                }

                @Override
                public void onFailure(int statusCode, String responseString) {
                    super.onFailure(statusCode, responseString);
                    syncLockUserCallback.onSyncLockUserFailed(responseString);
                }
            });

    }

}
