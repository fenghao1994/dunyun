package net.dunyun.framework.android.mainapp.vo;

import android.graphics.drawable.Drawable;

/**
 * 功能模块
 *
 * @author chenzp
 * @date 2015/8/21
 */
public class FunctionModuleVo {

    protected int id;
    /**模块名称*/
    protected String name = null;
    /**模块的图标*/
    protected Drawable icon = null;
    /**模块的包名*/
    protected String packageName = null;
    /**入口*/
    protected String mainActivity = null;
    /**安装版本*/
    protected String installVersion = null;

    public FunctionModuleVo() {
    }

    public FunctionModuleVo(int id, String name, Drawable icon, String packageName,
                            String mainActivity, String installVersion) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.mainActivity = mainActivity;
        this.installVersion = installVersion;
    }

    /**功能模块编号*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**功能模块名称*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**功能模块图标*/
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**功能模块包名*/
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setMainActivity(String mainActivity){
        this.mainActivity = mainActivity;
    }

    public String getMainActivity(){
        return mainActivity;
    }

    public String getInstallVersion() {
        return installVersion;
    }

    public void setInstallVersion(String installVersion) {
        this.installVersion = installVersion;
    }
}
