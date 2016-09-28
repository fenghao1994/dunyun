package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.raizlabs.android.dbflow.StringUtils;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

/**
 * <DL>
 * <DD>更新锁.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class UpdateKeyBiz {
    UpdateKeyCallback updateKeyCallback;

    public UpdateKeyBiz(UpdateKeyCallback updateKeyCallback){
        this.updateKeyCallback = updateKeyCallback;
    }

    public void updateLock(KeyVo keyVo, String token, String mobile){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", keyVo.getMacCode());
        paramsMap.put("keyIndex", keyVo.getKeyIndex());
        paramsMap.put("token", token);
        paramsMap.put("mobile", mobile);
        if(StringUtils.isNotNullOrEmpty(keyVo.getKeyName())){
            paramsMap.put("keyName", keyVo.getKeyName());
        }
        if(StringUtils.isNotNullOrEmpty(keyVo.getIsShare())){
            paramsMap.put("isShare", keyVo.getIsShare());
        }
        if(StringUtils.isNotNullOrEmpty(keyVo.getState())){
            paramsMap.put("state", keyVo.getState());
        }
        if(StringUtils.isNotNullOrEmpty(keyVo.getPushFlg())){
            paramsMap.put("pushFlg", keyVo.getPushFlg());
        }

        HttpUtils.getInstance().httpPost(WebUrl.UPDATEKEY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                updateKeyCallback.onKeySuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                updateKeyCallback.onKeyFailed(responseString);
            }
        });
    }
}
