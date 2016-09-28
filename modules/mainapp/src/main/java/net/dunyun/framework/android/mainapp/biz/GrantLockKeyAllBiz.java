package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.JsonArray;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

import org.json.JSONObject;

import java.util.Arrays;
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
public class GrantLockKeyAllBiz {
    private GrantLockKeyCallback grantLockKeyCallback;

    public GrantLockKeyAllBiz(GrantLockKeyCallback grantLockKeyCallback) {
        this.grantLockKeyCallback = grantLockKeyCallback;
    }

    public void grantLockKey(List<String> mobileList, List<LockVo> lockVoList,
                             String grantBdt, String grantEdt, String grantNum, String mobile, String token) {
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        String mobiles = "";
        for (String string : mobileList) {
            mobiles = mobiles + string + ",";
        }
        mobiles = mobiles.substring(0, mobiles.length() - 1);
        StringBuffer sb = new StringBuffer();
        for (LockVo lockVo : lockVoList) {
            try {
                KeyVo keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), mobile);
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("keyName", "钥匙"+keyVo.getMacCode());
                jsonObj.put("macCode", keyVo.getMacCode());
                jsonObj.put("keyIndex", keyVo.getKeyIndex());
                jsonObj.put("addTm", keyVo.getAddTm());
                KeyPasswd keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), mobile);
                jsonObj.put("grantPwd", AesUtil.getInstance().encrypt(keyPasswd.password.getBytes()));
                sb.append(jsonObj.toString() + ",");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        paramsMap.put("mobiles", "[" + mobiles + "]");
        paramsMap.put("multiKeys",  "["+sb.toString()+"]");
        paramsMap.put("grantBdt", grantBdt);
        paramsMap.put("grantEdt", grantEdt);
        paramsMap.put("grantNum", grantNum);
        paramsMap.put("grantMbl", mobile);
        paramsMap.put("token", token);
        paramsMap.put("mobile", mobile);
        HttpUtils.getInstance().httpPost(WebUrl.GRANTLOCKKEYBATCH, paramsMap, new BaseHttpResponseHandler() {
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
