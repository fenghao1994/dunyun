package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import net.dunyun.framework.android.mainapp.util.AesUtil;

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
public class RecordDbUtil {

    public static void insert(String macCode, String costTime, String mobile, String recordType,
                              String createDt, String unlockMethod, String owner) {
        RecordDb recordDb = new RecordDb();
        recordDb.macCode = macCode;
        recordDb.owner = owner;
        recordDb.costTime = costTime;
        recordDb.mobile = mobile;
        recordDb.recordType = recordType;
        recordDb.createDt = createDt;
        recordDb.unlockMethod = unlockMethod;
        recordDb.insert();
    }

    public static List<RecordDb> query(String owner) {
        List<RecordDb> recordDbs = SQLite.select().from(RecordDb.class)
                .where(RecordDb_Table.owner.eq(owner))
                .and(RecordDb_Table.owner.eq(owner))
                .queryList();
        return recordDbs;
    }

    public static void delete(Long id){
        RecordDb recordDb = new Select().from(RecordDb.class)
                .where(RecordDb_Table.id.eq(id))
                .querySingle();
        recordDb.delete();
    }

}
