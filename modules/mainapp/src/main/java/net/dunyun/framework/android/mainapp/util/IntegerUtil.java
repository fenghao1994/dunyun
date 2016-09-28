package net.dunyun.framework.android.mainapp.util;

/**
 * @author chenzp
 * @date 2016/5/19
 * @Copyright:重庆平软科技有限公司
 */
public class IntegerUtil {

    public static String toHexString(String intString) {
        String str = Integer.toHexString(Integer.valueOf(intString));
        int length = 6 - str.length();
        while (length > 0){
            str="0"+str;
            length--;
        }
        return str;
    }
    public static String toLongString(String hexString) {
        Long long1 = Long.parseLong(hexString, 16);
        return long1+"";
    }
}
