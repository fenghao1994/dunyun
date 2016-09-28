package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import net.dunyun.framework.android.mainapp.util.AesUtil;

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
public class KeyPasswdDbUtil  {

    public static void insert(String macCode, String password, String owner){
        KeyPasswd keyPasswd = SQLite.select().from(KeyPasswd.class)
                .where(KeyPasswd_Table.owner.eq(owner))
                .and(KeyPasswd_Table.macCode.eq(macCode))
                .querySingle();
        if(keyPasswd != null){
            keyPasswd.delete();
        }
        keyPasswd = new KeyPasswd();
        keyPasswd.macCode = macCode;
        keyPasswd.owner = owner;
        keyPasswd.password = AesUtil.getInstance().encrypt(password.getBytes());
        keyPasswd.insert();
    }

    public static KeyPasswd query(String macCode, String owner){
        KeyPasswd keyPasswd = new Select().from(KeyPasswd.class)
                .where(KeyPasswd_Table.owner.eq(owner))
                .and(KeyPasswd_Table.macCode.eq(macCode))
                .querySingle();
        if(keyPasswd != null){
            keyPasswd.password = AesUtil.getInstance().decrypt(keyPasswd.password);
        }
        return keyPasswd;
    }
}
