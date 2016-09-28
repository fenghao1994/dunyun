package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;

import java.util.ArrayList;
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
public class LockDbUtil {

    public static void insert(List<LockVo> keyVoList, String owner) {
        List<LockDb> lockDbs = SQLite.select().from(LockDb.class)
                .where(LockDb_Table.owner.eq(owner))
                .queryList();
        List<KeyDb> keyDbs = SQLite.select().from(KeyDb.class)
                .where(KeyDb_Table.owner.eq(owner))
                .queryList();

        for (LockDb lockDb : lockDbs) {
            lockDb.delete();
        }
        for (KeyDb keyDb : keyDbs) {
            keyDb.delete();
        }
        for (LockVo lockVo : keyVoList) {
            LockDb lockDb = lockVo2LockDb(lockVo, owner);
            lockDb.insert();
            List<KeyVo> keyVos = lockVo.getLockKeys();
            for (KeyVo keyVo : keyVos) {
                KeyDb keyDb = KeyDbUtil.keyVo2KeyDb(keyVo, owner);
                keyDb.insert();
            }
        }
    }

    public static void insert(LockVo lockVo, String owner) {
        LockDb lockDb = SQLite.select().from(LockDb.class)
                .where(LockDb_Table.owner.eq(owner))
                .and(LockDb_Table.macCode.eq(lockVo.getMacCode()))
                .querySingle();
        if(lockDb == null){
            LockDb lock = lockVo2LockDb(lockVo, owner);
            lock.insert();
        }
    }

    public static LockVo query(String  macCode, String owner) {
        LockDb lockDb = SQLite.select().from(LockDb.class)
                .where(LockDb_Table.owner.eq(owner))
                .and(LockDb_Table.macCode.eq(macCode))
                .querySingle();
        if(lockDb != null){
            List<KeyDb> keyDbs = SQLite.select().from(KeyDb.class)
                    .where(KeyDb_Table.owner.eq(owner))
                    .and(KeyDb_Table.macCode.eq(lockDb.macCode))
                    .queryList();

            LockVo lock = lockDb2lockVo(lockDb, owner);
            List<KeyVo> KeyVos = new ArrayList<KeyVo>();
            for (KeyDb keyDb : keyDbs) {
                KeyVo keyVo = KeyDbUtil.keyDb2KeyVo(keyDb, owner);
                KeyVos.add(keyVo);
            }
            lock.setLockKeys(KeyVos);
            return lock;
        }
        return null;
    }

    public static List<LockVo> query(String owner) {
        List<LockVo> lockVos = new ArrayList<LockVo>();
        List<LockDb> lockDbs = SQLite.select().from(LockDb.class)
                .where(LockDb_Table.owner.eq(owner))
                .queryList();
        for (LockDb lockDb : lockDbs) {
            LockVo lockVo = lockDb2lockVo(lockDb, owner);
            List<KeyDb> keyDbs = SQLite.select().from(KeyDb.class)
                    .where(KeyDb_Table.owner.eq(owner))
                    .and(KeyDb_Table.macCode.eq(lockDb.macCode))
                    .queryList();
            List<KeyVo> KeyVos = new ArrayList<KeyVo>();
            for (KeyDb keyDb : keyDbs) {
                KeyVo keyVo = KeyDbUtil.keyDb2KeyVo(keyDb, owner);
                KeyVos.add(keyVo);
            }
            lockVo.setLockKeys(KeyVos);
            lockVos.add(lockVo);
        }
        return lockVos;
    }

    public static LockDb lockVo2LockDb(LockVo lockVo, String owner) {
        LockDb lockDb = new LockDb();
        lockDb.address = lockVo.getAddress();
        lockDb.cbssState = lockVo.getCbssState();
        lockDb.communityId = lockVo.getCommunityId();
        lockDb.djState = lockVo.getDjState();
        lockDb.lockName = lockVo.getLockName();
        lockDb.macCode = lockVo.getMacCode();
        lockDb.macName = lockVo.getMacName();
        lockDb.owner = owner;
        lockDb.power = lockVo.getPower();
        lockDb.tdState = lockVo.getTdState();
        lockDb.isGrant = lockVo.getIsGrant();
        return lockDb;
    }

    public static LockVo lockDb2lockVo(LockDb lockDb, String owner) {
        LockVo lockVo = new LockVo();
        lockVo.setAddress(lockDb.address);
        lockVo.setCbssState(lockDb.cbssState);
        lockVo.setCommunityId(lockDb.communityId);
        lockVo.setDjState(lockDb.djState);
        lockVo.setLockName(lockDb.lockName);
        lockVo.setMacCode(lockDb.macCode);
        lockVo.setMacName(lockDb.macName);
        lockVo.setPower(lockDb.power);
        lockVo.setState(lockDb.state);
        lockVo.setTdState(lockDb.tdState);
        lockVo.setIsGrant(lockDb.isGrant);
        return lockVo;
    }


}
