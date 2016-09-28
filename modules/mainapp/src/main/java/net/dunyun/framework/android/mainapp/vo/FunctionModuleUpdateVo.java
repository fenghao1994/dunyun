package net.dunyun.framework.android.mainapp.vo;

import android.graphics.drawable.Drawable;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author Angma <Chenzp>
 * @date 2015/12/28
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2012
 */
public class FunctionModuleUpdateVo extends FunctionModuleVo {
    /**本地是否有安装版本*/
    private boolean installStatus;

    /**服务器版本*/
    private String serverVersion;

    public FunctionModuleUpdateVo() {
        super();
    }

    public FunctionModuleUpdateVo(int id, String name, Drawable icon, String packageName, String mainActivity,
                                  String installVersion, boolean installStatus, String serverVersion) {
        super(id, name, icon, packageName, mainActivity, installVersion);
        this.installStatus = installStatus;
        this.serverVersion = serverVersion;
    }

    /***
     * 本地是否有安装版
     * @return
     */
    public boolean isInstall(){
        if(installVersion != null && !installVersion.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 是否需要更新版本
     * @return
     */
    public boolean isNeedUpdateVersion(){
        if(installVersion!= null && !installVersion.isEmpty() && installVersion.equals(serverVersion)){
            return false;
        }else{
            return true;
        }
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }
}
