package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * <DL>
 * <DD>消息实体.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class MessageVo implements Serializable{
    private String title;
    private String content;
    private String type;
    private String isRead;// 0:未读,1:已读
    private String readDt;
    private String pushFm;
    private String pushTo;
    private String pushFlg;// 1:接收消息推送,0:不接收消息推送
    private String isPush;
    private String pushDt;
    private String pushRst;
    private String pushId;
    private String del;
    private String createBy;// 创建人
    private String createDt;// 创建时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getReadDt() {
        return readDt;
    }

    public void setReadDt(String readDt) {
        this.readDt = readDt;
    }

    public String getPushFm() {
        return pushFm;
    }

    public void setPushFm(String pushFm) {
        this.pushFm = pushFm;
    }

    public String getPushTo() {
        return pushTo;
    }

    public void setPushTo(String pushTo) {
        this.pushTo = pushTo;
    }

    public String getPushFlg() {
        return pushFlg;
    }

    public void setPushFlg(String pushFlg) {
        this.pushFlg = pushFlg;
    }

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }

    public String getPushDt() {
        return pushDt;
    }

    public void setPushDt(String pushDt) {
        this.pushDt = pushDt;
    }

    public String getPushRst() {
        return pushRst;
    }

    public void setPushRst(String pushRst) {
        this.pushRst = pushRst;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
