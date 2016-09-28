package net.dunyun.framework.android.mainapp.util;

import android.content.Context;

import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchCallback;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.RecordBatchUtil;
import net.dunyun.framework.android.mainapp.db.RecordDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class RecordsUtil {

    static LockRecord lockRecord = null;

    static StringBuffer sb = null;

    public static void addLockRecord(final KeyVo keyVo, final String recordType,
                                     final String unlockMethod, final String costTime, String token,
                                     final List<LockRecord> lockRecords, String mobile, String operType,String address) {
        Collections.sort(lockRecords);
        for (LockRecord lockRecord : lockRecords) {
            LogUtil.d("--------------"+lockRecord.getTime());
        }
        if (lockRecords.size() == 1) {
            lockRecord = lockRecords.get(0);
        } else {
            lockRecord = lockRecords.get(lockRecords.size() - 1);
        }

        AddLockRecordBiz addLockRecordBiz = new AddLockRecordBiz(new AddLockRecordCallback() {
            @Override
            public void onAddLockRecordSuccess(String result) {
                LogUtil.d("上传开锁记录成功");
            }

            @Override
            public void onAddLockRecordFailed(String result) {
                LogUtil.d("上传开锁记录失败");
                RecordDbUtil.insert(keyVo.getMacCode(), costTime, keyVo.getMobile(), recordType, lockRecord.getTime(), unlockMethod, keyVo.getMobile());
            }
        });

        addLockRecordBiz.addLockRecord(keyVo, recordType, unlockMethod, costTime, lockRecord.getTime(), operType, address, token);

        if (lockRecords.size() > 1) {
            sb = new StringBuffer();
            lockRecords.remove(lockRecord);
            for (LockRecord record : lockRecords) {
                try {
                    JSONObject jsonObj = new JSONObject();
                    //
                    String type = "";
                    if (record.isOpenRecord()) {
                        type = "3";
                    } else {
                        type = "4";
                    }
                    jsonObj.put("keyIndex", record.getUserIndex());
                    jsonObj.put("time", record.getTime());
//                    jsonObj.put("operType", type);
                    jsonObj.put("recordType", type);//"recordType": "1",//记录类型：recordType;// 1:添加钥匙，2:授权钥匙，3:开门、4:关门
//                    "operType": "1"   // 操作类型：1:点击开锁，2:感应开锁，3:摇一摇开锁，4:手机开门、5:面板开门
                    sb.append(jsonObj.toString() + ",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length() - 1);

            AddLockRecordBatchBiz addLockRecordBatchBiz = new AddLockRecordBatchBiz(new AddLockRecordBatchCallback() {
                @Override
                public void onAddLockRecordBatchSuccess(String result) {
                    LogUtil.d("上传历史记录成功");
                }

                @Override
                public void onAddLockRecordBatchFailed(String result) {
                    LogUtil.d("上传历史记录失败");
                    RecordBatchUtil.insert(keyVo.getMacCode(), sb.toString(), keyVo.getMobile());
                }
            });
            addLockRecordBatchBiz.addLockRecord(keyVo, sb.toString(), token, mobile);
        }
    }
}
