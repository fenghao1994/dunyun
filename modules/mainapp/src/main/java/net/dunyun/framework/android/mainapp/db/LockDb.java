package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

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
public class LockDb extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String macCode;
    @Column
    public String macName;
    @Column
    public String lockName;
    @Column
    public String power;// 锁电量
    @Column
    public String state;// 待定
    @Column
    public String djState;// 电机锁状态
    @Column
    public String tdState;// 天地锁
    @Column
    public String cbssState;// 常闭锁状态
    @Column
    public String address;
    @Column
    public String communityId;// 小区ID
    @Column
    public String isGrant;
    @Column
    public String owner;//拥有者
}
