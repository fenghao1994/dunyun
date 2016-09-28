package net.dunyun.framework.android.mainapp.util;

import com.psoft.framework.android.base.utils.FileUtil;

import net.dunyun.framework.android.mainapp.biz.DownloadCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockCallback;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.VersionVo;

import java.io.File;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class VersionUtil {
    static DownloadCallback downloadCallback;
    static VersionVo versionVo;
    public static void download(final VersionVo version, final String downloadDir, final DownloadCallback callback) {
        downloadCallback = callback;
        versionVo = version;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String filepath = FileUtil.downloadFile(version.getUrl(), downloadDir);
                if(filepath == null){
                    callback.onError(0, "下载失败");
                }else {
                    callback.onFinish(new File(filepath));
                }
            }
        }).start();
    }

}
