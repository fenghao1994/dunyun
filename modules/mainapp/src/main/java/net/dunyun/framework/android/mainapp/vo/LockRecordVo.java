package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <DL>
 * <DD>门锁记录.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class LockRecordVo implements Serializable{
    private String macCode;// MAC地址
    private String macName;// MAC名称
    private String lockName;// 锁名称
    private String djState;// 电机状态
    private String tdState;// 天机
    private String cbssState;// 常闭锁舌

    private String enterOut;// 进出
    private String keyIndex;// 钥匙编号
    private String keyType;// 钥匙类型
    private String mobile;// 电话号码
    private String keyName;// 钥匙名称
    private String addTm;// 时间戳
    private String state;// 钥匙状态
    private String isShare;// 开锁记录是否共享
    private String rssi;// 感应距离
    private String isRssi;// 是否支持感应开锁（0：不支持；1：支持）
    private String isShake;// 是否支持摇一摇开锁（0：不支持；1：支持）
    private String costTime;// 耗时

    private String grantMbl;// 授权手机
    private String grantBdt;// 授权开始时间
    private String grantEdt;// 授权结束时间
    private String grantNum;// 授权次数
    private String grantPwd;// 授权密码

    private String recordType;// 1:添加钥匙，2:授权钥匙，3:开锁
    private String createDt;// 创建时间
    private String avatarUrl;// 头像
    private String nickName;// 昵称

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getMacName() {
        return macName;
    }

    public void setMacName(String macName) {
        this.macName = macName;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getDjState() {
        return djState;
    }

    public void setDjState(String djState) {
        this.djState = djState;
    }

    public String getTdState() {
        return tdState;
    }

    public void setTdState(String tdState) {
        this.tdState = tdState;
    }

    public String getCbssState() {
        return cbssState;
    }

    public void setCbssState(String cbssState) {
        this.cbssState = cbssState;
    }

    public String getEnterOut() {
        return enterOut;
    }

    public void setEnterOut(String enterOut) {
        this.enterOut = enterOut;
    }

    public String getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(String keyIndex) {
        this.keyIndex = keyIndex;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
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

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getIsRssi() {
        return isRssi;
    }

    public void setIsRssi(String isRssi) {
        this.isRssi = isRssi;
    }

    public String getIsShake() {
        return isShake;
    }

    public void setIsShake(String isShake) {
        this.isShake = isShake;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
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

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
