package net.dunyun.framework.android.mainapp.application;

import android.app.Application;

import net.dunyun.framework.android.mainapp.crash.CrashHandler;
import net.dunyun.framework.android.mainapp.vo.AuthenVo;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.ContactsVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import com.psoft.bluetooth.DunyunSDK;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.PropertiesUtil;

import com.psoft.framework.android.base.utils.SharedUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Properties;

/**
 * APP唯一的的全局类Application，缓存登录信息和全局变量
 *
 * @author chenzp
 * @date 2015/10/29
 * @Copyright:重庆平软科技有限公司
 */
public class MainApplication extends Application{
    /**登录用户*/
    protected UserVo userVo = null;
    protected String userId = null;
    /***/
    protected ContactsVo mobileContactsVo = null;
    public static HttpUtils httpUtils = null;
    /**配置文件文件名*/
    public static final String CONFIG_NAME = "config.properties";
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static MainApplication instance;

    private static boolean isBle = true;
    public static long lastClickTime = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        httpUtils = HttpUtils.getSessionInstance();
        initConfig();
        FlowManager.init(this);
        CrashReport.initCrashReport(getApplicationContext());

        String userVoStr = SharedUtil.getString(this, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            CrashReport.setUserId(userVo.getMobile());
        }
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    public synchronized static MainApplication getInstance(){
        if (null == instance) {
            instance = new MainApplication();
        }
        return instance;
    }

    /***
     * 加载配置文件
     */
    private void initConfig(){
        Properties properties = PropertiesUtil.loadConfigFromAssets(this, CONFIG_NAME);
        if(properties != null){
            String server_url = properties.getProperty("SERVER_URL");
            ConfigVo.SERVER_URL = server_url;

            ConfigVo.MODEL_APP_PACKAGE_PREFIX = properties.getProperty("MODEL_APP_PACKAGE_PREFIX");
            ConfigVo.CONTACTS_LIST_URL = server_url+properties.getProperty("CONTACTS_LIST_URL");
            ConfigVo.CONTACTS_ONE_URL = server_url+properties.getProperty("CONTACTS_ONE_URL");
            ConfigVo.CONTACTS_UPDATE_URL = server_url+properties.getProperty("CONTACTS_UPDATE_URL");
            ConfigVo.LOGIN_URL = server_url+properties.getProperty("LOGIN_URL");
            ConfigVo.UPDATE_PASSWORD_URL = server_url+properties.getProperty("UPDATE_PASSWORD_URL");
            ConfigVo.VERSION_UPGRADER_DOWNLOAD_URL = server_url+properties.getProperty("VERSION_UPGRADER_DOWNLOAD_URL");
            ConfigVo.VERSION_UPGRADER_POST_URL = server_url+properties.getProperty("VERSION_UPGRADER_POST_URL");
        }else{
            ToastUtil.showToast(this, CONFIG_NAME+"加载错误");
        }
    }

    public ContactsVo getMobileContactsVo() {
        return mobileContactsVo;
    }

    public void setMobileContactsVo(ContactsVo mobileContactsVo) {
        this.mobileContactsVo = mobileContactsVo;
    }

//    public UserVo getUserVo() {
//        return userVo;
//    }

    public void setUserVo(UserVo userVo) {
        if(userVo != null){
            userId = userVo.getMobile().substring(1);
        }
        this.userVo = userVo;
    }

    public String getUserId() {
        return userId;
    }

    public static boolean isBle() {
        return isBle;
    }

    public static void setIsBle(boolean isBle) {
        MainApplication.isBle = isBle;
    }
}
