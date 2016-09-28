package net.dunyun.framework.android.mainapp.util;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author chenzp
 * @date 2016/6/21
 * @Copyright:重庆平软科技有限公司
 */
public class BuglyUtil {

    public static void postInfo(String content){
        Throwable throwable = new Throwable("开锁失败,"+content);
        CrashReport.postCatchedException(throwable);  // bugly会将这个throwable上报
    }
}
