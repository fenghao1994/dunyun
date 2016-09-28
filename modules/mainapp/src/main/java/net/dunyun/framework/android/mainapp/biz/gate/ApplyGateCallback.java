package net.dunyun.framework.android.mainapp.biz.gate;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/3/30
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public interface ApplyGateCallback {

    void onSuccess(String result);

    void onFailed(String result);
}
