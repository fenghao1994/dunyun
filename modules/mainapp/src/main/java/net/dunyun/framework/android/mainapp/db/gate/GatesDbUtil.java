package net.dunyun.framework.android.mainapp.db.gate;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.dunyun.framework.android.mainapp.db.KeyDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
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
public class GatesDbUtil {

    public static GatesDb gateVo2GateDb(GatesVo gatesVo) {
        GatesDb gatesDb = new GatesDb();
        gatesDb.gateId = gatesVo.gateId;
        gatesDb.username = gatesVo.username;
        gatesDb.wifiMac = gatesVo.wifiMac;
        gatesDb.nettyHost = gatesVo.nettyHost;
        gatesDb.nettyPort = gatesVo.nettyPort;
        gatesDb.localHost = gatesVo.localHost;
        gatesDb.localPort = gatesVo.localPort;
        gatesDb.keyIndex = gatesVo.keyIndex;
        gatesDb.keyType = gatesVo.keyType;
        gatesDb.password = gatesVo.password;
        gatesDb.aes128key = gatesVo.aes128key;
        gatesDb.keyChainId = gatesVo.keyChainId;
        gatesDb.owner = gatesVo.owner;
        return gatesDb;
    }

    public static GatesVo gateDb2GateVo(GatesDb gatesDb) {
        GatesVo gatesVo = new GatesVo();
        gatesVo.gateId = gatesDb.gateId;
        gatesVo.username = gatesDb.username;
        gatesVo.wifiMac = gatesDb.wifiMac;
        gatesVo.nettyHost = gatesDb.nettyHost;
        gatesVo.nettyPort = gatesDb.nettyPort;
        gatesVo.localHost = gatesDb.localHost;
        gatesVo.localPort = gatesDb.localPort;
        gatesVo.keyIndex = gatesDb.keyIndex;
        gatesVo.keyType = gatesDb.keyType;
        gatesVo.password = gatesDb.password;
        gatesVo.aes128key = gatesDb.aes128key;
        gatesVo.keyChainId = gatesDb.keyChainId;
        gatesVo.owner = gatesDb.owner;
        return gatesVo;
    }

}
