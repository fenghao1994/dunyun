package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * @author chenzp
 * @date 2015/11/25
 * @Copyright:重庆平软科技有限公司
 */
public class ApplyRecordVo implements Serializable {

    private String id;
    private String mobile;
    private String applyMbl;
    private String reason;
    private String mark;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getApplyMbl() {
        return applyMbl;
    }

    public void setApplyMbl(String applyMbl) {
        this.applyMbl = applyMbl;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
