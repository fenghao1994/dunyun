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
public class KeyChainDb extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public Long numId;
    @Column
    public String id;
    @Column
    public String mobile;
    @Column
    public String communityId;
    @Column
    public String isAdmin;
    @Column
    public String chainName;
    @Column
    public String gateType;
    @Column
    public String chainType;
    @Column
    public String grantMbl;
    @Column
    public String grantEdt;
    @Column
    public String grantBdt;
    @Column
    public String grantNum;
    @Column
    public String owner;//拥有者
}
