package net.dunyun.framework.android.mainapp.util;

import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchCallback;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordCallback;
import net.dunyun.framework.android.mainapp.biz.SyncLockUserBiz;
import net.dunyun.framework.android.mainapp.biz.SyncLockUserCallback;
import net.dunyun.framework.android.mainapp.db.RecordBatchUtil;
import net.dunyun.framework.android.mainapp.db.RecordDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

import org.json.JSONObject;

import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class LockUserUtil {

    public static void addLockUser(final KeyVo keyVo, String token, List<LockUser> data, String mobile) {
        LogUtil.d("addLockUser---------------");
        SyncLockUserBiz syncLockUserBiz = new SyncLockUserBiz(new SyncLockUserCallback() {
            @Override
            public void onSyncLockUserSuccess(String result) {
                LogUtil.d("--同步用户成功--");
            }

            @Override
            public void onSyncLockUserFailed(String result) {
                LogUtil.d("--同步用户失败--");
            }
        });

        StringBuffer sb = new StringBuffer();
        if(data.size() == 0){

        }else {
            for (LockUser lockUser : data) {
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("keyIndex", lockUser.getUserIndex() + "");
                    jsonObj.put("mobile", UserUtil.getPhone(lockUser.getUserId()));
                    jsonObj.put("addTm", IntegerUtil.toLongString(UserUtil.getTime(lockUser.getUserId())));
                    sb.append(jsonObj.toString() + ",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        String lockKeys = sb.toString();
        syncLockUserBiz.syncLockUser(keyVo, lockKeys, token, mobile);
    }

}
