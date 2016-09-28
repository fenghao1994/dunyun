package net.dunyun.framework.android.mainapp.biz;

/**
 * <DL>
 * <DD>修改钥匙.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public interface UpdateLockKeyCallback {

    void onSuccess(String result);

    void onFailed(String result);

}
