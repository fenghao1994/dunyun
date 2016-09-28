package net.dunyun.framework.android.mainapp.vo.gate;

import com.raizlabs.android.dbflow.structure.BaseModel;

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
public class DoorsVo implements Serializable {
    public String gateId;
    public String doorIndex;
    public String doorMac;
    public String owner;//拥有者

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getDoorIndex() {
        return doorIndex;
    }

    public void setDoorIndex(String doorIndex) {
        this.doorIndex = doorIndex;
    }

    public String getDoorMac() {
        return doorMac;
    }

    public void setDoorMac(String doorMac) {
        this.doorMac = doorMac;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
