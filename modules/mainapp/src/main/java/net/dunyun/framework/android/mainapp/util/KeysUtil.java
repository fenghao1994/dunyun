package net.dunyun.framework.android.mainapp.util;

import android.content.Context;

import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.GetKeysBiz;
import net.dunyun.framework.android.mainapp.biz.GetKeysCallback;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.util.List;

/**
 * @author chenzp
 * @date 2016/7/7
 * @Copyright:重庆平软科技有限公司
 */
public class KeysUtil {

    public static void getKeys(Context context){
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            GetKeysBiz getKeysBiz = new GetKeysBiz(new GetKeysCallback() {
                @Override
                public void onKeysSuccess(List<LockVo> lockVoList, int flag) {
                    LogUtil.d("---请求成功---");
                }

                @Override
                public void onKeysFailed(String result) {
                    LogUtil.d("---请求失败---");
                }
            });
            UserVo userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            getKeysBiz.getKeys(userVo);
        }
    }
}
