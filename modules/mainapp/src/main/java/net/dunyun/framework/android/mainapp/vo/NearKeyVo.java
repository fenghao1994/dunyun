package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * <DL>
 * <DD>钥匙实体.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class NearKeyVo extends KeyVo implements Serializable{
  private int rssi;
    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
