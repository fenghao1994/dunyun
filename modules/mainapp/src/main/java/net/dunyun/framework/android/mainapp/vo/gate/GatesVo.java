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
public class GatesVo implements Serializable {
    public String gateId;
    public String username;
    public String wifiMac;
    public String nettyHost;
    public String localHost;
    public String localPort;
    public String nettyPort;
    public String keyIndex;
    public String keyType;
    public String password;
    public String aes128key;
    public List<DoorsVo> doors;//闸门
    public String keyChainId;//钥匙串ID
    public String owner;//拥有者

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAes128key() {
        return aes128key;
    }

    public void setAes128key(String aes128key) {
        this.aes128key = aes128key;
    }

    public String getKeyChainId() {
        return keyChainId;
    }

    public void setKeyChainId(String keyChainId) {
        this.keyChainId = keyChainId;
    }

    public List<DoorsVo> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorsVo> doors) {
        this.doors = doors;
    }

    public String getNettyHost() {
        return nettyHost;
    }

    public void setNettyHost(String nettyHost) {
        this.nettyHost = nettyHost;
    }

    public String getNettyPort() {
        return nettyPort;
    }

    public void setNettyPort(String nettyPort) {
        this.nettyPort = nettyPort;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }
}
