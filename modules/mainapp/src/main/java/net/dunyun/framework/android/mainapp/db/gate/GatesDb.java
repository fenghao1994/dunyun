package net.dunyun.framework.android.mainapp.db.gate;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.dunyun.framework.android.mainapp.db.AppDatabase;

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
@ModelContainer
@Table(database = AppDatabase.class)
public class GatesDb extends BaseModel {
    @PrimaryKey
    public String id;
    @Column
    public String gateId;
    @Column
    public String username;
    @Column
    public String wifiMac;
    @Column
    public String nettyHost;
    @Column
    public String nettyPort;
    @Column
    public String localHost;
    @Column
    public String localPort;
    @Column
    public String keyIndex;
    @Column
    public String keyType;
    @Column
    public String password;
    @Column
    public String aes128key;
    @Column
    public String doors;//闸门
    @Column
    public String keyChainId;//钥匙串ID
    @Column
    public String owner;//拥有者
}
