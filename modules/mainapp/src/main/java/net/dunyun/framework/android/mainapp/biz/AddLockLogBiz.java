package net.dunyun.framework.android.mainapp.biz;

import android.os.Build;
import android.support.v4.util.SimpleArrayMap;

import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.utils.Base64Util;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.AppUtil;
import com.psoft.framework.android.base.utils.DateUtil;

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
public class AddLockLogBiz {
    AddLockLogCallback addLockLogCallback;

    public AddLockLogBiz(AddLockLogCallback addLockLogCallback) {
        this.addLockLogCallback = addLockLogCallback;
    }

    public void addLockLog(UserVo userVo, DYLockDevice dyLockDevice, String operType, String operLog, String gps) {
        int verserionInt = 210;
        String lockMac = "";
        if(dyLockDevice != null){
            if (dyLockDevice.getName() != null && dyLockDevice.getName().length() == 16) {
                String versionStr3 = Base64Util.getPosition(dyLockDevice.getName().charAt(dyLockDevice.getName().length() - 5)) + "";
                String versionStr1 = Base64Util.getPosition(dyLockDevice.getName().charAt(dyLockDevice.getName().length() - 4)) + "";
                String versionStr2 = Base64Util.getPosition(dyLockDevice.getName().charAt(dyLockDevice.getName().length() - 3)) + "";
                int verserionInt3 = Integer.parseInt(versionStr3) * 100;
                int verserionInt1 = Integer.parseInt(versionStr1) * 10;
                int verserionInt2 = Integer.parseInt(versionStr2);
                verserionInt =verserionInt3+ verserionInt1 + verserionInt2;
            }

            lockMac = dyLockDevice.getMac();
            if(lockMac == null || lockMac.equals("")){
                lockMac = "";
            }
            if("".equals(lockMac)){
                if(dyLockDevice.getBluetoothDevice() !=null && dyLockDevice.getBluetoothDevice().getName() != null){
                    lockMac = dyLockDevice.getBluetoothDevice().getName();
                }
            }
            if(lockMac == null || "null".equals(lockMac)){
                lockMac = "";
            }
        }

        if(gps == null || gps.equals("null")){
            gps = "";
        }

        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("mobileModels", Build.DEVICE);
        paramsMap.put("mobileVersion", Build.VERSION.RELEASE);
        paramsMap.put("lockVersion", verserionInt+"");
        paramsMap.put("lockMac", lockMac+"");
        paramsMap.put("operDt", DateUtil.getCurrentDate());
        paramsMap.put("operType", operType);
        paramsMap.put("devicetype", "1");
        paramsMap.put("operLog", operLog);
        paramsMap.put("gps", gps+"");

        HttpUtils.getInstance().httpPost(WebUrl.ADDLOCKLOG, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                addLockLogCallback.onSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                addLockLogCallback.onFailed(responseString);
            }
        });
    }
}
