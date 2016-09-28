package net.dunyun.framework.android.mainapp.util;

import com.psoft.bluetooth.beans.LockUser;
import com.psoft.bluetooth.utils.LogUtil;

import net.dunyun.framework.android.mainapp.biz.SyncLockUserBiz;
import net.dunyun.framework.android.mainapp.biz.SyncLockUserCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockCallback;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

import org.json.JSONObject;

import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class LockUtil {

    public static void updateLock(final LockVo lockVo, String token, String mobile) {
        UpdateLockBiz updateLockBiz = new UpdateLockBiz(new UpdateLockCallback() {

            @Override
            public void onUpdateLockSuccess(String result) {

            }

            @Override
            public void onUpdateLockFailed(String result) {

            }
        });

        updateLockBiz.updateLock(lockVo, token, mobile);
    }

}
