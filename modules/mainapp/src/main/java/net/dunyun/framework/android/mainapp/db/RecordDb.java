package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

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
public class RecordDb extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String macCode;
    @Column
    public String costTime;
    @Column
    public String mobile;
    @Column
    public String recordType;
    @Column
    public String createDt;
    @Column
    public String unlockMethod;
    @Column
    public String owner;//拥有者
}
