package net.dunyun.framework.android.mainapp.db.gate;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.dunyun.framework.android.mainapp.db.KeyCacheDb;
import net.dunyun.framework.android.mainapp.db.KeyDb;
import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.db.KeyDb_Table;
import net.dunyun.framework.android.mainapp.db.LockDb;
import net.dunyun.framework.android.mainapp.db.LockDb_Table;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.gate.DoorsVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;

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
public class KeyChainUtil {

    public static void insert(List<KeyChainVo> keyChainVos, String owner) {
        List<KeyChainDb> keyChainDbs = SQLite.select().from(KeyChainDb.class)
                .where(KeyChainDb_Table.owner.eq(owner))
                .queryList();
        List<GatesDb> gatesDbs = SQLite.select().from(GatesDb.class)
                .where(GatesDb_Table.owner.eq(owner))
                .queryList();
        List<DoorsDb> doorsDbs = SQLite.select().from(DoorsDb.class)
                .where(DoorsDb_Table.owner.eq(owner))
                .queryList();

        for (KeyChainDb keyChainDb : keyChainDbs) {
            keyChainDb.delete();
        }
        for (GatesDb gatesDb : gatesDbs) {
            gatesDb.delete();
        }
        for (DoorsDb doorsDb : doorsDbs) {
            doorsDb.delete();
        }

        for (KeyChainVo keyChainVo : keyChainVos) {
            KeyChainDb keyChainDb = keyVo2KeyDb(keyChainVo, owner);
            keyChainDb.insert();

            List<GatesVo> gatesVos = keyChainVo.getGates();
            for (GatesVo gatesVo : gatesVos) {
                List<DoorsVo> doorsVos = gatesVo.getDoors();
                gatesVo.setKeyChainId(keyChainVo.getId());
                GatesDb gatesDb = GatesDbUtil.gateVo2GateDb(gatesVo);
                gatesDb.insert();

                for(DoorsVo doorsVo :doorsVos){
                    DoorsDb doorsDb = DoorsUtil.toDb(doorsVo, owner);
                    doorsDb.insert();
                }
            }
        }
    }

    public static void insert(KeyChainVo keyChainVo, String owner){
        KeyChainDb keydb = SQLite.select().from(KeyChainDb.class)
                .where(KeyChainDb_Table.owner.eq(owner))
                .and(KeyChainDb_Table.id.eq(keyChainVo.getId()))
                .querySingle();
        if(keydb != null){
            keydb.delete();
        }
        KeyChainDb key = keyVo2KeyDb(keyChainVo, owner);
        key.insert();
    }

    public static List<KeyChainVo> query(String owner){
        List<KeyChainVo> keyChainVos = new ArrayList<KeyChainVo>();
        List<KeyChainDb> keyChainDbs = SQLite.select().from(KeyChainDb.class)
                .where(KeyChainDb_Table.owner.eq(owner))
                .queryList();

        for (KeyChainDb keyChainDb : keyChainDbs) {
            KeyChainVo keyChainVo = keyDb2KeyVo(keyChainDb, owner);
            List<GatesDb> keyDbs = SQLite.select().from(GatesDb.class)
                    .where(GatesDb_Table.owner.eq(owner))
                    .and(GatesDb_Table.keyChainId.eq(keyChainDb.id))
                    .queryList();
            List<GatesVo> gatesVos = new ArrayList<GatesVo>();
            for (GatesDb gatesDb : keyDbs) {
                GatesVo gatesVo = GatesDbUtil.gateDb2GateVo(gatesDb);

                List<DoorsDb> doorsDbs = SQLite.select().from(DoorsDb.class)
                        .where(DoorsDb_Table.owner.eq(owner))
                        .and(DoorsDb_Table.gateId.eq(gatesVo.gateId))
                        .queryList();
                List<DoorsVo> doorsVos = new ArrayList<DoorsVo>();
                for  (DoorsDb doorsDb : doorsDbs){
                    doorsVos.add(DoorsUtil.toVo(doorsDb,owner));
                }
                gatesVo.setDoors(doorsVos);
                gatesVos.add(gatesVo);
            }
            keyChainVo.setGates(gatesVos);
            keyChainVos.add(keyChainVo);
        }
        return keyChainVos;
    }

    public static KeyChainDb keyVo2KeyDb(KeyChainVo keyVo, String owner) {
        KeyChainDb keyDb = new KeyChainDb();
        keyDb.chainName = keyVo.chainName;
        keyDb.id = keyVo.id;
        keyDb.gateType = keyVo.gateType;
        keyDb.chainType = keyVo.chainType;
        keyDb.mobile = keyVo.mobile;
        keyDb.grantMbl = keyVo.grantMbl;
        keyDb.grantBdt = keyVo.grantBdt;
        keyDb.grantEdt = keyVo.grantEdt;
        keyDb.grantNum = keyVo.grantNum;
        keyDb.communityId = keyVo.communityId;
        keyDb.isAdmin = keyVo.isAdmin;
        keyDb.owner = owner;
        return keyDb;
    }

    public static KeyChainVo keyDb2KeyVo(KeyChainDb keyDb, String owner) {
        KeyChainVo keyVo = new KeyChainVo();
        keyVo.chainName = keyDb.chainName;
        keyVo.gateType = keyDb.gateType;
        keyVo.chainType = keyDb.chainType;
        keyVo.mobile = keyDb.mobile;
        keyVo.grantMbl = keyDb.grantMbl;
        keyVo.grantBdt = keyDb.grantBdt;
        keyVo.grantEdt = keyDb.grantEdt;
        keyVo.grantNum = keyDb.grantNum;
        keyVo.communityId = keyDb.communityId;
        keyVo.id = keyDb.id;
        keyVo.isAdmin = keyDb.isAdmin;
        keyVo.owner = owner;
        return keyVo;
    }

}
