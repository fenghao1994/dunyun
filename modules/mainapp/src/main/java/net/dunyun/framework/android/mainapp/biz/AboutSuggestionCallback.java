package net.dunyun.framework.android.mainapp.biz;

import net.dunyun.framework.android.mainapp.vo.UserVo;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-13
 * @Copyright:
 *
 */
public interface AboutSuggestionCallback {

    void onSuccess(UserVo userVo);

    void onFailed(String result);
}
