package net.dunyun.framework.android.mainapp.biz;

import net.dunyun.framework.android.mainapp.vo.AdvertiseVo;

import java.util.List;

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
public interface GetAdvertisesCallback {

    void onGetAdvertisesSuccess(List<AdvertiseVo> advertiseVoList);

    void onGetAdvertisesFailed(String result);
}
