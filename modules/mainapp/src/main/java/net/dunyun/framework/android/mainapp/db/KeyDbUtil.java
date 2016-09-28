package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

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
public class KeyDbUtil {

    public static void update(String number, String mobile, String macCode, String owner){
        KeyDb keydb = SQLite.select().from(KeyDb.class)
                .where(KeyDb_Table.owner.eq(owner))
                .and(KeyDb_Table.macCode.eq(macCode))
                .and(KeyDb_Table.mobile.eq(mobile))
                .querySingle();
        if(keydb != null){
            keydb.grantNum = number;
            keydb.update();
        }
    }

    public static void insert(KeyVo keyVo, String owner){
        KeyDb keydb = SQLite.select().from(KeyDb.class)
                .where(KeyDb_Table.owner.eq(owner))
                .and(KeyDb_Table.macCode.eq(keyVo.getMacCode()))
                .and(KeyDb_Table.mobile.eq(keyVo.getMobile()))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
        KeyDb key = keyVo2KeyDb(keyVo, owner);
        key.insert();
    }

    public static KeyDb keyVo2KeyDb(KeyVo keyVo, String owner) {
        KeyDb keyDb = new KeyDb();
        keyDb.macCode = keyVo.macCode;
        keyDb.keyIndex = keyVo.keyIndex;
        keyDb.mobile = keyVo.mobile;
        keyDb.keyName = keyVo.keyName;
        keyDb.keyType = keyVo.keyType;
        keyDb.addTm = keyVo.addTm;
        keyDb.state = keyVo.state;
        keyDb.isShare = keyVo.isShare;
        keyDb.pushFlg = keyVo.pushFlg;
        keyDb.grantMbl = keyVo.grantMbl;
        keyDb.grantBdt = keyVo.grantBdt;
        keyDb.grantEdt = keyVo.grantEdt;
        keyDb.grantNum = keyVo.grantNum;
        keyDb.grantPwd = keyVo.grantPwd;
        keyDb.owner = owner;
        return keyDb;
    }

    public static KeyVo keyDb2KeyVo(KeyDb keyDb, String owner) {
        KeyVo keyVo = new KeyVo();
        keyVo.macCode = keyDb.macCode;
        keyVo.keyIndex = keyDb.keyIndex;
        keyVo.mobile = keyDb.mobile;
        keyVo.keyName = keyDb.keyName;
        keyVo.keyType = keyDb.keyType;
        keyVo.addTm = keyDb.addTm;
        keyVo.state = keyDb.state;
        keyVo.isShare = keyDb.isShare;
        keyVo.pushFlg = keyDb.pushFlg;
        keyVo.grantMbl = keyDb.grantMbl;
        keyVo.grantBdt = keyDb.grantBdt;
        keyVo.grantEdt = keyDb.grantEdt;
        keyVo.grantNum = keyDb.grantNum;
        keyVo.grantPwd = keyDb.grantPwd;
        return keyVo;
    }

}
