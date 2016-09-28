package net.dunyun.framework.android.mainapp.util;

import com.psoft.bluetooth.beans.LockRecord;
import com.psoft.bluetooth.utils.LogUtil;

import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBatchCallback;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.AddLockRecordCallback;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainBiz;
import net.dunyun.framework.android.mainapp.biz.gate.UpdateChainCallback;
import net.dunyun.framework.android.mainapp.db.RecordBatchUtil;
import net.dunyun.framework.android.mainapp.db.RecordDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class GateUtil {


    private static UpdateChainBiz updateChainBiz;

    public static void update(String id, String token, String num) {
        UpdateChainCallback updateChainCallback = new UpdateChainCallback() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailed(String result) {

            }
        };
        updateChainBiz = new UpdateChainBiz(updateChainCallback);
        updateChainBiz.updateChain(id,"",token,"","",num);
    }
}
