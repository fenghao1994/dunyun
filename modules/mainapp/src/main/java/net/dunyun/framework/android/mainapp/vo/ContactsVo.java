package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * 联系人VO
 * @author chenzp
 * @date 2015/11/25
 * @Copyright:重庆平软科技有限公司
 */
public class ContactsVo implements Serializable {

    /**联系人ID.*/
    private Long contactsId;

    /**姓名.*/
    private String name;

    /**电话号码.*/
    private String phone;

    /**accountId .*/
    private Long accountId;


    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
