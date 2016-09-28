package net.dunyun.framework.android.mainapp.util;

import com.psoft.bluetooth.beans.DYLockDevice;
import com.psoft.bluetooth.utils.LogUtil;

import net.dunyun.framework.android.mainapp.biz.AddLockLogBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockLogCallback;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockCallback;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class AddLockLogUtil {

    public static final String operType_Add = "1";
    public static final String operType_Open = "2";

    public static void addLockLog(final UserVo userVo, final DYLockDevice dyLockDevice, final String operType, final String operLog, final String gps) {
        final AddLockLogBiz addLockLogBiz = new AddLockLogBiz(new AddLockLogCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d("成功");
            }

            @Override
            public void onFailed(String result) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                addLockLogBiz.addLockLog(userVo, dyLockDevice, operType, operLog, gps);
            }
        }).start();
    }

}
