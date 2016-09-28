package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <DL>
 * <DD>锁实体.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class LockVo implements Serializable{
    public String macCode;
    public String macName;
    public String lockName;
    public String power;// 锁电量
    public String state;// 待定
    public String djState;// 电机锁状态
    public String tdState;// 天地锁
    public String cbssState;// 常闭锁状态
    public String address;
    public String communityId;// 小区ID
    public String isGrant;// 0关：，1：开

    public List<KeyVo> lockKeys;

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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public List<KeyVo> getLockKeys() {
        return lockKeys;
    }

    public void setLockKeys(List<KeyVo> lockKeys) {
        this.lockKeys = lockKeys;
    }

    public String getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(String isGrant) {
        this.isGrant = isGrant;
    }
}
