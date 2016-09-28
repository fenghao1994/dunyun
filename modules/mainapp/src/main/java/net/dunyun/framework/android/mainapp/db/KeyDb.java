package net.dunyun.framework.android.mainapp.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/31
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
@ModelContainer
@Table(database = AppDatabase.class)
public class KeyDb extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String macCode;
    @Column
    public String keyIndex;// 钥匙位置：从0开始，自动补齐第一个
    @Column
    public String mobile;
    @Column
    public String keyName;
    @Column
    public String keyType;// 钥匙类型：1添加钥匙 2授权钥匙
    @Column
    public String addTm;// 添加锁的时间
    @Column
    public String state;// 钥匙状态（2:停用，1表示启用，0待删除）
    @Column
    public String isShare;// 门锁记录是否共享（0：不共享；1共享）
    @Column
    public String pushFlg;// 1:接收消息推送,2:不接收消息推送
    @Column
    public String grantMbl;
    @Column
    public String grantBdt;
    @Column
    public String grantEdt;
    @Column
    public String grantNum;
    @Column
    public String grantPwd;
    @Column
    public String owner;//拥有者
}
