package net.dunyun.framework.android.mainapp.vo;

/**
 * 带字母组合的联系人
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class ContactsLettersVo {
	/**联系人*/
	private ContactsVo mobileContactsVo;
	/**字母组合*/
	private String sortLetters;

	public ContactsVo getMobileContactsVo() {
		return mobileContactsVo;
	}

	public void setMobileContactsVo(ContactsVo mobileContactsVo) {
		this.mobileContactsVo = mobileContactsVo;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
