package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.annotation.Database;

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
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "AppDatabase";
    //数据库版本号
    public static final int VERSION = 3;
}
