package net.dunyun.framework.android.mainapp.util;

import android.content.Context;

import com.psoft.bluetooth.callback.Callback;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.application.MainApplication;
import net.dunyun.framework.android.mainapp.biz.LoginBiz;
import net.dunyun.framework.android.mainapp.biz.LoginCallback;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/7
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class UserUtil {

    public static String getUserId(String phone){
        long time = getOffectMinutes();
        String first = Long.toHexString(time);
        LogUtil.d(first);
        int firstLength = first.length();
        if(firstLength >= 6){
            first = first.substring(0, 5);
        }else{
            int i = 0;
            while ((6-firstLength) > i){
                first="0"+first;
                i++;
            }
        }
        return  first+phone.substring(1);
    }

    public static String parseAddTime(String addTime){
        String first = Long.toHexString(Long.parseLong(addTime));
        LogUtil.d(first);
        int firstLength = first.length();
        if(firstLength >= 6){
            first = first.substring(0, 5);
        }else{
            int i = 0;
            while ((6-firstLength) > i){
                first="0"+first;
                i++;
            }
        }
        return  first;
    }

    public static String getPhone(String userId){
        return "1"+userId.substring(6);
    }

    public static String getTime(String userId){
        return userId.substring(0, 6);
    }

    public static String getTimeStr(String userId){
        return Long.valueOf(userId.substring(0, 6), 16)+"";
    }

    public static long getOffectMinutes(){
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse("2016-01-01 00:00:00");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            return  (System.currentTimeMillis() - timestamp)/(1000*60);
        }catch (Exception e){
            return 0l;
        }
    }

    public static String getRandomPasswd() {
        int strLength = 6;
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static void login(final Context context, final MainApplication mainApplication, final UserVo fUserVo){
        LoginBiz loginBiz = new LoginBiz(new LoginCallback() {
            @Override
            public void onSuccess(UserVo userVo) {
                userVo.setPassWord(fUserVo.getPassWord());
                mainApplication.setUserVo(userVo);
                SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
            }

            @Override
            public void onFailed(String result) {

            }
        });
        loginBiz.login(fUserVo);
    }

    public static void login(final Context context, final MainApplication mainApplication, final UserVo fUserVo, final Callback<String> callback){
        LoginBiz loginBiz = new LoginBiz(new LoginCallback() {
            @Override
            public void onSuccess(UserVo userVo) {
                userVo.setPassWord(fUserVo.getPassWord());
                mainApplication.setUserVo(userVo);
                SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
                callback.onSuccess("0");
            }

            @Override
            public void onFailed(String result) {

            }
        });
        loginBiz.login(fUserVo);
    }

    public static void loginOut(final UserVo fUserVo){
        LoginBiz loginBiz = new LoginBiz(new LoginCallback() {
            @Override
            public void onSuccess(UserVo userVo) {

            }

            @Override
            public void onFailed(String result) {

            }
        });
        if(fUserVo != null){
            loginBiz.loginOut(fUserVo);
        }
    }
}
