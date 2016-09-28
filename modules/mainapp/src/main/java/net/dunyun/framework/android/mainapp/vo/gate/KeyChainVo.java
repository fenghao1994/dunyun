package net.dunyun.framework.android.mainapp.vo.gate;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.dunyun.framework.android.mainapp.db.AppDatabase;

import java.io.Serializable;
import java.util.List;

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
public class KeyChainVo implements Serializable {

    public String id;
    public String mobile;
    public String chainName;
    public String gateType;
    public String chainType;
    public String grantMbl;
    public String grantEdt;
    public String grantBdt;
    public String grantNum;
    public String owner;
    public String communityId;
    public String isAdmin;
    public List<GatesVo> gates;

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

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getGateType() {
        return gateType;
    }

    public void setGateType(String gateType) {
        this.gateType = gateType;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    public String getGrantMbl() {
        return grantMbl;
    }

    public void setGrantMbl(String grantMbl) {
        this.grantMbl = grantMbl;
    }

    public String getGrantEdt() {
        return grantEdt;
    }

    public void setGrantEdt(String grantEdt) {
        this.grantEdt = grantEdt;
    }

    public String getGrantBdt() {
        return grantBdt;
    }

    public void setGrantBdt(String grantBdt) {
        this.grantBdt = grantBdt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<GatesVo> getGates() {
        return gates;
    }

    public void setGates(List<GatesVo> gates) {
        this.gates = gates;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getGrantNum() {
        return grantNum;
    }

    public void setGrantNum(String grantNum) {
        this.grantNum = grantNum;
    }
}
