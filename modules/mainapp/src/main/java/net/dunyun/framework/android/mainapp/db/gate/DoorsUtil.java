package net.dunyun.framework.android.mainapp.db.gate;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
public class DoorsUtil {

    public static DoorsDb toDb(DoorsVo doorsVo, String owner) {
        DoorsDb doorsDb = new DoorsDb();
        doorsDb.gateId = doorsVo.gateId;
        doorsDb.doorIndex = doorsVo.doorIndex;
        doorsDb.doorMac = doorsVo.doorMac;
        return doorsDb;
    }

    public static DoorsVo toVo(DoorsDb doorsDb, String owner) {
        DoorsVo doorsVo = new DoorsVo();
        doorsVo.gateId = doorsDb.gateId;
        doorsVo.doorIndex = doorsDb.doorIndex;
        doorsVo.doorMac = doorsDb.doorMac;
        return doorsVo;
    }

}
