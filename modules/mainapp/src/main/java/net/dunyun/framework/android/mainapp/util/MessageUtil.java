package net.dunyun.framework.android.mainapp.util;

import net.dunyun.framework.android.mainapp.biz.MessageBiz;
import net.dunyun.framework.android.mainapp.biz.MessageCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockCallback;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/12
 * @Copyright:重庆平软科技有限公司
 */
public class MessageUtil {

    public static void getUnReadMessage(UserVo userVo) {
        MessageBiz messageBiz = new MessageBiz(new MessageCallback() {
            @Override
            public void onMessageSuccess(List<MessageVo> messageVoList, PageVo pageVo) {

            }

            @Override
            public void onMessageFailed(String result) {

            }

            @Override
            public void onMessageCount(String number) {

            }

            @Override
            public void onMessageClearSuccess(String result) {

            }

            @Override
            public void onMessageClearFailed(String result) {

            }
        });

        messageBiz.getUnReadCount(userVo);
    }

}
