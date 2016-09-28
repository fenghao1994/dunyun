package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.callback.Callback;
import com.psoft.bluetooth.callback.ConnectCallback;
import com.psoft.bluetooth.utils.DialogUtil;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.network.http.ThreadPoolUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.db.KeyCacheDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
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
public class AddKeyBiz{
    private AddKeyCallback addKeyCallback;

    public AddKeyBiz(AddKeyCallback addKeyCallback) {
        this.addKeyCallback = addKeyCallback;
    }

    /***
     *
     * @param token
     * @param keyVo
     * @param lockName
     * @param macName
     * @param costTime
     */
    public void addKeyToWeb(final KeyVo keyVo, final String lockName, final String macName, final String costTime, String address, String token){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();

        paramsMap.put("macCode", keyVo.getMacCode());
        paramsMap.put("keyIndex", keyVo.getKeyIndex());
        paramsMap.put("lockName", lockName);
        paramsMap.put("keyName", keyVo.getKeyName());
        paramsMap.put("mobile", keyVo.getMobile());
        paramsMap.put("addTm", keyVo.getAddTm());
        paramsMap.put("costTime", costTime);
        paramsMap.put("token", token);
        paramsMap.put("macName", macName);
        paramsMap.put("address", address+"");

        HttpUtils.getInstance().httpPost(WebUrl.ADD_KEY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                addKeyCallback.onAddKeyToWebSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                KeyCacheDbUtil.insert(keyVo.getMacCode(), keyVo.getKeyIndex(),lockName,keyVo.getKeyName(),
                        keyVo.getMobile(),keyVo.getAddTm(),costTime,macName,keyVo.getMobile());
                addKeyCallback.onAddKeyToWebFailed(responseString);
            }
        });
    }

    public void addKeyToWebCache(String macCode, String keyIndex, String lockName, String keyName,
                                 String mobile, String addTm, String costTime,String macName, String token){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();

        paramsMap.put("macCode", macCode);
        paramsMap.put("keyIndex", keyIndex);
        paramsMap.put("lockName", lockName);
        paramsMap.put("keyName", keyName);
        paramsMap.put("mobile", mobile);
        paramsMap.put("addTm", addTm);
        paramsMap.put("costTime", costTime);
        paramsMap.put("token", token);
        paramsMap.put("macName", macName);

        HttpUtils.getInstance().httpPost(WebUrl.ADD_KEY, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                addKeyCallback.onAddKeyToWebSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                if(statusCode == 205){
                    addKeyCallback.onAddKeyToWebSuccess(responseString);
                }else{
                    addKeyCallback.onAddKeyToWebFailed(responseString);
                }
            }
        });
    }

}
