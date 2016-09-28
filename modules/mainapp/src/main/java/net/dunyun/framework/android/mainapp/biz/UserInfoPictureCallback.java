package net.dunyun.framework.android.mainapp.biz;

import net.dunyun.framework.android.mainapp.vo.UserVo;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author wuhx
 * @version v1.0
 * 2016-04-20
 * @Copyright:
 */
public interface UserInfoPictureCallback {

    void onSuccess(int code,String data);

    void onFailed(int code,String data);
}