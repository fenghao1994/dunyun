package net.dunyun.framework.android.mainapp.biz;

import net.dunyun.framework.android.mainapp.vo.MessageVo;
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
public interface MessageCallback {

    void onMessageSuccess(List<MessageVo> messageVoList, PageVo pageVo);

    void onMessageFailed(String result);

    void onMessageCount(String number);

    void onMessageClearSuccess(String result);

    void onMessageClearFailed(String result);

}
