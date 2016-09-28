package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.GrantKeyVo;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
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
public class GetGrantKeysBiz {
    private GetGrantKeysCallback getGrantKeysCallback;

    public GetGrantKeysBiz(GetGrantKeysCallback getGrantKeysCallback) {
        this.getGrantKeysCallback = getGrantKeysCallback;
    }

    public void getGrantKeys(UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        HttpUtils.getInstance().httpPost(WebUrl.GETGRANTKEYS, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                List<GrantKeyVo> keyVoList = JsonUtil.parseArray(responseString, new TypeToken<List<GrantKeyVo>>() {
                }.getType());
                getGrantKeysCallback.onGrantKeysSuccess(keyVoList);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                getGrantKeysCallback.onGrantKeysFailed(responseString);
            }
        });
    }
}
