package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

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
public class AddLockRecordBiz {
    private AddLockRecordCallback addLockRecordCallback;

    public AddLockRecordBiz(AddLockRecordCallback addLockRecordCallback) {
        this.addLockRecordCallback = addLockRecordCallback;
    }

    /***
     *
     * @param token
     * @param keyVo
     * @param recordType
     * @param unlockMethod
     * @param costTime
     */
    public void addLockRecord(KeyVo keyVo, String recordType, String unlockMethod, String costTime, String createDt, String operType, String address, String token){
        ////操作类型： 1:点击开锁，2:感应开锁，3:摇一摇开锁，4:手机开门、5:面板开门
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", keyVo.getMacCode());
        paramsMap.put("costTime", costTime);
        paramsMap.put("mobile", keyVo.getMobile());
        paramsMap.put("token", token);
        paramsMap.put("recordType", recordType);
        paramsMap.put("createDt", createDt);
        paramsMap.put("unlockMethod", unlockMethod);
        paramsMap.put("operType", operType);
        paramsMap.put("address", address);

        HttpUtils.getInstance().httpPost(WebUrl.ADDLOCKRECORD, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                addLockRecordCallback.onAddLockRecordSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                addLockRecordCallback.onAddLockRecordFailed(responseString);
            }
        });
    }

}
