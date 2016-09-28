package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.util.NearKeyUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
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
public class GetKeysBiz {
    private GetKeysCallback getKeysCallback;

    public GetKeysBiz(GetKeysCallback getKeysCallback) {
        this.getKeysCallback = getKeysCallback;
    }

    public void getKeys(final UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("token", userVo.getToken());

        getKeysCallback.onKeysSuccess(LockDbUtil.query(userVo.getMobile()), 0);
        HttpUtils.getInstance().httpPost(WebUrl.GETKEYS, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                NearKeyUtil.clear(userVo.getMobile());

                LogUtil.d(responseString);
                List<LockVo> keyVoList = JsonUtil.parseArray(responseString, new TypeToken<List<LockVo>>() {
                }.getType());
                LockDbUtil.insert(keyVoList, userVo.getMobile());
                getKeysCallback.onKeysSuccess(keyVoList, 1);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                getKeysCallback.onKeysFailed(responseString);
            }
        });


    }
}
