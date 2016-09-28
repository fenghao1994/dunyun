package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.PageUtil;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import org.json.JSONObject;

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
public class MessageBiz {
    private MessageCallback messageCallback;

    public MessageBiz(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public void getMessage(UserVo userVo, int page, int rows){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("page", page+"");
        paramsMap.put("rows", rows+"");
        paramsMap.put("token", userVo.getToken());
        HttpUtils.getInstance().httpPost(WebUrl.MESSAGE, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);
                PageVo pageVo = null;
                try{
                    pageVo = PageUtil.getPageVo(responseString);
                    String result = pageVo.getResult();

                    List<MessageVo> messageVoList = JsonUtil.parseArray(result, new TypeToken<List<MessageVo>>() {
                    }.getType());

                    messageCallback.onMessageSuccess(messageVoList, pageVo);

                }catch (Exception e){
                    e.printStackTrace();
                    messageCallback.onMessageFailed("请求异常");
                }
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

                messageCallback.onMessageFailed(responseString);
            }
        });
    }

    public void getUnReadCount(UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("isRead", "0");
        paramsMap.put("token", userVo.getToken());
        HttpUtils.getInstance().httpPost(WebUrl.UNREADCOUNT, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);

                messageCallback.onMessageCount(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

            }
        });
    }

    public void readAll(UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("token", userVo.getToken());
        HttpUtils.getInstance().httpPost(WebUrl.READALL, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);

            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);

            }
        });
    }

    public void clearMessage(UserVo userVo){
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("mobile", userVo.getMobile());
        paramsMap.put("token", userVo.getToken());
        HttpUtils.getInstance().httpPost(WebUrl.CLEARMESSAGE, paramsMap, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                LogUtil.d(responseString);
                messageCallback.onMessageClearSuccess(responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                messageCallback.onMessageClearFailed(responseString);
            }
        });
    }

}
