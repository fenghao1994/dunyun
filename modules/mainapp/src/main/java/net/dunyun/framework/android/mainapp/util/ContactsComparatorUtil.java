package net.dunyun.framework.android.mainapp.util;

import net.dunyun.framework.android.mainapp.vo.ContactsLettersVo;

import java.util.Comparator;

/**
 * 联系人按姓名首字母比较
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class ContactsComparatorUtil implements Comparator<ContactsLettersVo> {

	public int compare(ContactsLettersVo o1, ContactsLettersVo o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
