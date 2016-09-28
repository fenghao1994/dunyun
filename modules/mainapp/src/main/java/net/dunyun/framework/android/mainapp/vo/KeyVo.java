package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * <DL>
 * <DD>钥匙实体.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class KeyVo implements Serializable{
    public String macCode;
    public String keyIndex;// 钥匙位置：从0开始，自动补齐第一个
    public String mobile;
    public String keyName;
    public String keyType;// 钥匙类型：1添加钥匙 2授权钥匙
    public String addTm;// 添加锁的时间
    public String state;// 钥匙状态（2:停用，1表示启用，0待删除）
    public String isShare;// 门锁记录是否共享（0：不共享；1共享）
    public String pushFlg;// 1:接收消息推送,2:不接收消息推送
    public String grantMbl;
    public String grantBdt;
    public String grantEdt;
    public String grantNum;
    public String grantPwd;

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(String keyIndex) {
        this.keyIndex = keyIndex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getAddTm() {
        return addTm;
    }

    public void setAddTm(String addTm) {
        this.addTm = addTm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getPushFlg() {
        return pushFlg;
    }

    public void setPushFlg(String pushFlg) {
        this.pushFlg = pushFlg;
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

    public String getGrantPwd() {
        return grantPwd;
    }

    public void setGrantPwd(String grantPwd) {
        this.grantPwd = grantPwd;
    }
}
