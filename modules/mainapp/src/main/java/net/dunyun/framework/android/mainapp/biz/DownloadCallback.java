package net.dunyun.framework.android.mainapp.biz;


import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.io.File;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/11
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public interface DownloadCallback {

    void onProgress(int progress);

    void onFinish(File file);

    void onError(int code, String error);
}
