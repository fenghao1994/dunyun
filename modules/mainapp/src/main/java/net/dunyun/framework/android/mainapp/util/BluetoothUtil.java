package net.dunyun.framework.android.mainapp.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.utils.LogUtil;

import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenzp
 * @date 2016/5/3
 * @Copyright:重庆平软科技有限公司
 */
public class BluetoothUtil {

    public static boolean bluetoothIsOpen(Context context) {
        BluetoothManager mBluetoothManager = null;
        BluetoothAdapter mBluetoothAdapter = null;
        if (mBluetoothManager == null) {
            mBluetoothManager = (android.bluetooth.BluetoothManager) context
                    .getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                // LogUtil.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            //LogUtil.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            // 请求打开 Bluetooth
            Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 请求开启 Bluetooth
            ((Activity) context).startActivityForResult(requestBluetoothOn, 1);
            return false;
        }
        return true;
    }

    /**
     * 找到信号最好的设备
     */
    public synchronized static DYLockDevice findBestDevice(List<DYLockDevice> dyLockDevices) {
        int rssi = -1000;
        int index = 0;
        if (dyLockDevices != null && dyLockDevices.size() > 0) {
            for (int i = 0; i < dyLockDevices.size(); i++) {
                if (dyLockDevices.get(i).getRssi() > rssi) {
                    rssi = dyLockDevices.get(i).getRssi();
                    index = i;
                }
            }
            LogUtil.d("-----信号最好的锁--------" + dyLockDevices.get(index).getName() + " | " + dyLockDevices.get(index).getMac());
            return dyLockDevices.get(index);
        } else {
            return null;
        }
    }

    public static DYLockDevice findBestDevice(List<DYLockDevice> data, String macCode) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName() != null && data.get(i).getName().contains(macCode)) {
                return data.get(i);
            }
        }
        return null;
    }

    public static synchronized DYLockDevice findBestDevice(List<DYLockDevice> data, Set<String> keys) {
        DYLockDevice device = null;
        if (keys != null && !keys.isEmpty()) {
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String str = it.next();
                for (int j = 0; j < data.size(); j++) {
                    LogUtil.d(data.get(j).getName() + "---------" + str);
                    if (data.get(j).getName().contains(str)) {
                        device = data.get(j);
                        break;
                    }
                }
            }
        }
        return device;
    }

    public static KeyVo findKeyVo(String macCode, String ower, List<LockVo> lockVoList) {
        if (lockVoList != null && lockVoList.size() > 0) {
            for (int i = 0; i < lockVoList.size(); i++) {
                if (lockVoList.get(i).getMacCode().contains(macCode)) {
                    LockVo lockVo = lockVoList.get(i);
                    List<KeyVo> keyVoList = lockVo.getLockKeys();
                    for (KeyVo keyVo : keyVoList) {
                        if (keyVo.getMobile().equals(ower)) {
                            return keyVo;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Map<KeyVo, KeyPasswd> getKeyVos(String mobile, List<LockVo> lockVoList) {
        Map<KeyVo, KeyPasswd> keyVos = new HashMap<KeyVo, KeyPasswd>();
        if (lockVoList != null && lockVoList.size() > 0) {
            for (int i = 0; i < lockVoList.size(); i++) {
                LockVo lockVo = lockVoList.get(i);
                List<KeyVo> keyVoList = lockVo.getLockKeys();
                for (KeyVo keyVo : keyVoList) {
                    if (keyVo.getMobile().equals(mobile)) {
                        KeyPasswd keyPasswd = KeyPasswdDbUtil.query(keyVo.getMacCode(), mobile);
                        keyVos.put(keyVo, keyPasswd);
                    }
                }
            }
        }
        return keyVos;
    }

    public static List<KeyVo> findKeyVosDelIndexs(LockVo lockVo) {
        List<KeyVo> delIndexs = new ArrayList<KeyVo>();
        List<KeyVo> keyVos = lockVo.getLockKeys();
        if (keyVos != null && keyVos.size() > 0) {
            for (int i = 0; i < keyVos.size(); i++) {
                if (keyVos.get(i).getKeyType().equals("1") && keyVos.get(i).getState().equals("0")) {
                    delIndexs.add(keyVos.get(i));
                }
            }
        }
        return delIndexs;
    }

    public static List<DYLockDevice> delNotSetDevice(List<DYLockDevice> dyLockDevices) {
        List<DYLockDevice> devices = new ArrayList<DYLockDevice>();
        for (int i = 0; i < dyLockDevices.size(); i++) {
            char[] chars = dyLockDevices.get(i).getName().toCharArray();
            if (chars[chars.length - 2] == 'B') {
                devices.add(dyLockDevices.get(i));
            }
        }
        return devices;
    }

    public static LockUser getAddLockUser(List<LockUser> lockUsers, String phone) {
        LockUser lockUser = new LockUser();
        int userIndex = 0;
        String userId = UserUtil.getUserId(phone);
        String phoneEnd = phone.substring(1);
        lockUser.setOpenLockPwd(UserUtil.getRandomPasswd());
        lockUser.setUserId(userId);

        if (lockUsers == null || lockUsers.size() == 0) {
            userIndex = 0;
        } else {
            if (lockUsers.size() >= 10) {
                userIndex = -1;
            } else {
                boolean isfind = false;
                for (int i = 0; i < lockUsers.size(); i++) {
                    if (lockUsers.get(i).getUserId().endsWith(phoneEnd)) {
                        userIndex = lockUsers.get(i).getUserIndex();
                        isfind = true;
                        break;
                    }
                }

                if (!isfind) {
                    int i = 0;
                    while (i < 10) {
                        boolean isRightIndex = true;
                        for (int j = 0; j < lockUsers.size(); j++) {
                            if (lockUsers.get(j).getUserIndex() == i) {
                                isRightIndex = false;
                                continue;
                            }
                        }
                        if (isRightIndex) {
                            userIndex = i;
                            break;
                        }
                        i++;
                    }
                }
            }
        }
        lockUser.setUserIndex(userIndex);
        return lockUser;
    }

    public static KeyVo getKeyVo(List<KeyVo> keyVos, String phone) {
        if (keyVos != null && keyVos.size() > 0) {
            for (int i = 0; i < keyVos.size(); i++) {
                if (keyVos.get(i).getMobile().equals(phone)) {
                    return keyVos.get(i);
                }
            }
        }
        return null;
    }

    public static List<LockVo> query(List<LockVo> lockVos, String phone) {
        if (lockVos != null && lockVos.size() > 0) {
            for (int i = 0; i < lockVos.size(); i++) {
                List<KeyVo> KeyVos = lockVos.get(i).getLockKeys();
                for (KeyVo keyVo : KeyVos) {
                    if (keyVo.getMobile().equals(phone)) {
                        if (!"1".equals(keyVo.getState())) {
                            lockVos.remove(lockVos.get(i));
                            break;
                        }
                    }
                }
            }
        }
        return lockVos;
    }
}
