package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * <DL>
 * <DD>钥匙密码.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/31
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class YaoKeyDbUtil {

    public static void insert(String macCode, String mobile, String owner){
        YaoKeyDb nearKeyDb = SQLite.select().from(YaoKeyDb.class)
                .where(YaoKeyDb_Table.owner.eq(owner))
                .and(YaoKeyDb_Table.macCode.eq(macCode))
                .and(YaoKeyDb_Table.mobile.eq(mobile))
                .querySingle();
        if(nearKeyDb != null){
            nearKeyDb.delete();
        }
        YaoKeyDb key = new YaoKeyDb();
        key.macCode = macCode;
        key.mobile = mobile;
        key.owner = owner;
        key.insert();
    }

    public static YaoKeyDb query(String macCode, String mobile, String owner) {
        YaoKeyDb nearKeyDb = SQLite.select().from(YaoKeyDb.class)
                .where(YaoKeyDb_Table.owner.eq(owner))
                .and(YaoKeyDb_Table.macCode.eq(macCode))
                .and(YaoKeyDb_Table.mobile.eq(mobile))
                .querySingle();
        return nearKeyDb;
    }

    public static void delete(String macCode, String mobile,String owner){
        YaoKeyDb keydb = SQLite.select().from(YaoKeyDb.class)
                .where(YaoKeyDb_Table.macCode.eq(macCode))
                .and(YaoKeyDb_Table.mobile.eq(mobile))
                .and(YaoKeyDb_Table.owner.eq(owner))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
    }

    public static void clear(String owner){
        List<YaoKeyDb> keyDbs = list(owner);
        if(keyDbs != null && keyDbs.size()>0){
            for (int i = 0; i < keyDbs.size(); i++) {
                delete(keyDbs.get(i).macCode, keyDbs.get(i).mobile, owner);
            }
        }
    }

    public static List<YaoKeyDb> list(String owner){
        List<YaoKeyDb> keyDbs = SQLite.select().from(YaoKeyDb.class)
                .where(YaoKeyDb_Table.owner.eq(owner))
                .queryList();
        return keyDbs;
    }

}
