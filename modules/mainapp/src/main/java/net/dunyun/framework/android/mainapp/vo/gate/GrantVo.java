package net.dunyun.framework.android.mainapp.vo.gate;

import java.io.Serializable;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/31
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class GrantVo implements Serializable {
    public String id;
    public String mobile;
    public String grantMbl;
    public String grantBdt;
    public String grantEdt;
    public String grantNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGrantMbl() {
        return grantMbl;
    }

    public void setGrantMbl(String grantMbl) {
        this.grantMbl = grantMbl;
    }

    public String getGrantBdt() {
        return grantBdt;
    }

    public void setGrantBdt(String grantBdt) {
        this.grantBdt = grantBdt;
    }

    public String getGrantEdt() {
        return grantEdt;
    }

    public void setGrantEdt(String grantEdt) {
        this.grantEdt = grantEdt;
    }

    public String getGrantNum() {
        return grantNum;
    }

    public void setGrantNum(String grantNum) {
        this.grantNum = grantNum;
    }
}
