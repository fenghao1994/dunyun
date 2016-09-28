package net.dunyun.framework.android.mainapp.biz;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

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
public interface GetLockRecordCallback {

    void onLockRecordSuccess(List<LockRecordVo> lockRecordVoList, PageVo pageVo);

    void onLockRecordFailed(String result);
}
