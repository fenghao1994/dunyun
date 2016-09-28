package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

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
public class RecordBatchUtil {

    public static void insert(String macCode, String records, String owner) {
        RecordBatchDb recordBatchDb = new RecordBatchDb();
        recordBatchDb.macCode = macCode;
        recordBatchDb.owner = owner;
        recordBatchDb.records = records;
        recordBatchDb.insert();
    }

    public static List<RecordBatchDb> query(String owner) {
        List<RecordBatchDb> recordDbs = SQLite.select().from(RecordBatchDb.class)
                .where(RecordBatchDb_Table.owner.eq(owner))
                .queryList();
        return recordDbs;
    }

    public static void delete(Long id){
        RecordBatchDb recordDb = new Select().from(RecordBatchDb.class)
                .where(RecordBatchDb_Table.id.eq(id))
                .querySingle();
        recordDb.delete();
    }

}
