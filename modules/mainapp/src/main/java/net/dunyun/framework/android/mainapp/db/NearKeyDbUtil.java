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
public class NearKeyDbUtil {

    public static void insert(String macCode, String mobile, int rssi, String owner){
        NearKeyDb nearKeyDb = SQLite.select().from(NearKeyDb.class)
                .where(NearKeyDb_Table.owner.eq(owner))
                .and(NearKeyDb_Table.macCode.eq(macCode))
                .and(NearKeyDb_Table.mobile.eq(mobile))
                .querySingle();
        if(nearKeyDb != null){
            nearKeyDb.delete();
        }
        NearKeyDb key = new NearKeyDb();
        key.macCode = macCode;
        key.mobile = mobile;
        key.owner = owner;
        key.rssi = rssi;
        key.insert();
    }

    public static NearKeyDb query(String macCode, String mobile, String owner) {
        NearKeyDb nearKeyDb = SQLite.select().from(NearKeyDb.class)
                .where(NearKeyDb_Table.owner.eq(owner))
                .and(NearKeyDb_Table.macCode.eq(macCode))
                .and(NearKeyDb_Table.mobile.eq(mobile))
                .querySingle();
        return nearKeyDb;
    }

    public static void delete(String macCode, String mobile,String owner){
        NearKeyDb keydb = SQLite.select().from(NearKeyDb.class)
                .where(NearKeyDb_Table.macCode.eq(macCode))
                .and(NearKeyDb_Table.mobile.eq(mobile))
                .and(NearKeyDb_Table.owner.eq(owner))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
    }

    public static void clear(String owner){
        List<NearKeyDb> keyDbs = list(owner);
        if(keyDbs != null && keyDbs.size()>0){
            for (int i = 0; i < keyDbs.size(); i++) {
                delete(keyDbs.get(i).macCode, keyDbs.get(i).mobile, owner);
            }
        }
    }

    public static List<NearKeyDb> list(String owner){
        List<NearKeyDb> keyDbs = SQLite.select().from(NearKeyDb.class)
                .where(NearKeyDb_Table.owner.eq(owner))
                .queryList();
        return keyDbs;
    }

}
