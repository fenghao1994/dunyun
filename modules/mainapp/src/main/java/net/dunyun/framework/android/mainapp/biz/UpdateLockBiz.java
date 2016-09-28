package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.raizlabs.android.dbflow.StringUtils;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

/**
 * <DL>
 * <DD>更新锁.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class UpdateLockBiz {
    UpdateLockCallback updateLockCallback;

    public UpdateLockBiz(UpdateLockCallback updateLockCallback){
        this.updateLockCallback = updateLockCallback;
    }

    public void updateLock(LockVo lockVo, String token, String mobile){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("macCode", lockVo.getMacCode());
        if(StringUtils.isNotNullOrEmpty(lockVo.getPower())){
            paramsMap.put("power", lockVo.getPower());
        }
        if(StringUtils.isNotNullOrEmpty(lockVo.getTdState())){
            paramsMap.put("tdState", lockVo.getTdState());
        }
        if(StringUtils.isNotNullOrEmpty(lockVo.getCbssState())){
            paramsMap.put("cbssState", lockVo.getCbssState());
        }
        if(StringUtils.isNotNullOrEmpty(lockVo.getIsGrant())){
            paramsMap.put("isGrant", lockVo.getIsGrant());
        }
        paramsMap.put("token", token);
        paramsMap.put("mobile", mobile);

        HttpUtils.getInstance().httpPost(WebUrl.UPDATELOCK, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                updateLockCallback.onUpdateLockSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                updateLockCallback.onUpdateLockFailed(responseString);
            }
        });
    }
}
