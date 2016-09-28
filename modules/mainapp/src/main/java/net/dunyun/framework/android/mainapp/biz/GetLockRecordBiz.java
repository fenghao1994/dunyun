package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.PageUtil;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.util.List;

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
public class GetLockRecordBiz {
    private GetLockRecordCallback getLockRecordCallback;

    public GetLockRecordBiz(GetLockRecordCallback getLockRecordCallback) {
        this.getLockRecordCallback = getLockRecordCallback;
    }

    public void getLockRecords(UserVo userVo, String keyType, String macCode, String recordType, int page, int rows){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("keyType",keyType);
        paramsMap.put("token", userVo.getToken());
        paramsMap.put("macCode", macCode);
        paramsMap.put("recordType", recordType);
        paramsMap.put("page", page+"");
        paramsMap.put("rows", rows+"");
        HttpUtils.getInstance().httpPost(WebUrl.GETLOCKRECORDS, paramsMap, new BaseHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);
                PageVo pageVo = null;
                try{
                    pageVo = PageUtil.getPageVo(responseString);
                    String result = pageVo.getResult();
                    List<LockRecordVo> lockRecordVos = JsonUtil.parseArray(result, new TypeToken<List<LockRecordVo>>() {
                    }.getType());
                    getLockRecordCallback.onLockRecordSuccess(lockRecordVos, pageVo);
                }catch (Exception e){
                    e.printStackTrace();
                    getLockRecordCallback.onLockRecordFailed("请求异常");
                }
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                getLockRecordCallback.onLockRecordFailed(responseString);
            }
        });
    }
}
