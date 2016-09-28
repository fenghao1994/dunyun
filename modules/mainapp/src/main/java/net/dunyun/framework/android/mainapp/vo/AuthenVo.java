package net.dunyun.framework.android.mainapp.vo;

/**
 * 登录认证的VO
 *
 * @author chenzp
 * @date 2015/10/29
 * @Copyright:重庆平软科技有限公司
 */
public class AuthenVo {
    /**用户名.*/
    private String username;
    /**系统账号.*/
    private Long accountId;
    /**系统密码.*/
    private String password;
    /**使用有效.*/
    private boolean enabled = false;
    /**密码盐值.*/
    private String salt;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
