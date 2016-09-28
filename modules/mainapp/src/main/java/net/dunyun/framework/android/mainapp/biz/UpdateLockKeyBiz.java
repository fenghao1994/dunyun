package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

/**
 * <DL>
 * <DD>修改钥匙.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class UpdateLockKeyBiz {
    UpdateLockKeyCallback updateLockKeyCallback;

    public UpdateLockKeyBiz(UpdateLockKeyCallback updateLockKeyCallback){
        this.updateLockKeyCallback = updateLockKeyCallback;

    }

    public void updateLockKey(KeyVo keyVo, UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", keyVo.getMacCode());
        paramsMap.put("keyIndex", keyVo.getKeyIndex());
        paramsMap.put("mobile", keyVo.getMobile());
        paramsMap.put("operMbl", userVo.getMobile());

        if(!StrUtil.isEmpty(keyVo.getKeyName())){
            paramsMap.put("keyName", keyVo.getKeyName());
        }
        if(!StrUtil.isEmpty(keyVo.getIsShare())){
            paramsMap.put("share", keyVo.getIsShare());
        }
        if(!StrUtil.isEmpty(keyVo.getState())){
            paramsMap.put("state", keyVo.getState());
        }
        if(!StrUtil.isEmpty(keyVo.getPushFlg())){
            paramsMap.put("pushFlg", keyVo.getPushFlg());
        }
        if(!StrUtil.isEmpty(keyVo.getKeyType())){
            paramsMap.put("keyType", keyVo.getKeyType());
        }
        if(!StrUtil.isEmpty(keyVo.getGrantNum())){
            paramsMap.put("grantNum", keyVo.getGrantNum());
        }
        paramsMap.put("token", userVo.getToken());

        HttpUtils.getInstance().httpPost(WebUrl.UPDATEKEY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                updateLockKeyCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                updateLockKeyCallback.onFailed(responseString);
            }
        });
    }
}
