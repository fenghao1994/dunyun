package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.dunyun.framework.android.mainapp.vo.KeyVo;

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
public class KeyCacheDbUtil {

    public static void update(String number, String mobile, String macCode, String owner){
        KeyCacheDb keydb = SQLite.select().from(KeyCacheDb.class)
                .where(KeyDb_Table.owner.eq(owner))
                .and(KeyDb_Table.macCode.eq(macCode))
                .and(KeyDb_Table.mobile.eq(mobile))
                .querySingle();
        if(keydb != null){
            keydb.update();
        }
    }

    public static void insert(String macCode, String keyIndex, String lockName, String keyName,
                              String mobile, String addTm, String costTime,String macName, String owner){
        KeyCacheDb keydb = SQLite.select().from(KeyCacheDb.class)
                .where(KeyCacheDb_Table.owner.eq(owner))
                .and(KeyCacheDb_Table.macCode.eq(macCode))
                .and(KeyCacheDb_Table.mobile.eq(mobile))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
        KeyCacheDb key = new KeyCacheDb();
        key.macCode = macCode;
        key.keyIndex = keyIndex;
        key.lockName = lockName;
        key.keyName = keyName;
        key.mobile = mobile;
        key.addTm = addTm;
        key.costTime = costTime;
        key.macName = macName;
        key.owner = owner;

        key.insert();
    }

    public static void delete(Long id){
        KeyCacheDb keydb = SQLite.select().from(KeyCacheDb.class)
                .where(KeyDb_Table.id.eq(id))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
    }

    public static List<KeyCacheDb> list(String owner){
        List<KeyCacheDb> lockDbs = SQLite.select().from(KeyCacheDb.class)
                .where(KeyCacheDb_Table.owner.eq(owner))
                .queryList();
        return lockDbs;
    }




}
