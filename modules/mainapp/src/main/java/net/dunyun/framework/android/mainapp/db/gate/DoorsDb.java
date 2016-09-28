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
public class DoorsDb extends BaseModel {
    //自增ID
    @PrimaryKey
    public String id;
    @Column
    public String gateId;
    @Column
    public String doorIndex;
    @Column
    public String doorMac;
    @Column
    public String owner;
}
