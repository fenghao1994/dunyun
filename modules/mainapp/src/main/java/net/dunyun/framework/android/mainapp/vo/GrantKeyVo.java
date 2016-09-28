package net.dunyun.framework.android.mainapp.vo;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class GrantKeyVo extends KeyVo{

    private String grantBdt;
    private String grantEdt;
    private String grantNum;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
