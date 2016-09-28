package net.dunyun.framework.android.mainapp.vo;

/**
 * <DL>
 * <DD>版本升级请求</DD><BR>
 * </DL>
 *
 * @author Angma <Chenzp>
 * @date 2015/12/2
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2012
 */
public class ClientVo {
    /**项目名称*/
    private String appName = null;
    /**客户端版本*/
    private String currentVersion = null;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }


}

