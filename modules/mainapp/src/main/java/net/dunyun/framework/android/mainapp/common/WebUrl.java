package net.dunyun.framework.android.mainapp.common;

/**
 * <DL>
 * <DD>后台请求地址.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 *         2016/3/30
 *         修改记录:
 *         初始化
 *         重庆平软科技有限公司 2015
 */
public class WebUrl {
//    public static final String ROOT = "http://192.168.1.202:8080/mes-dunyun-mobile/";
    /**开发环境*/
//    public static final String ROOT = "http://114.55.72.250:8080/mes-dunyun-mobile/";
    public static final String ROOT = "http://app.dunyun.net:81/mes-dunyun-mobile/";
    /**正式环境*/
//    public static final String ROOT = "http://114.55.113.240:8080/mes-dunyun-mobile/";
    /**
     * 我
     */
    public static final String LOGIN = ROOT + "common/user/login.do";
    /**
     * 登录
     */
    public static final String LOGINOUT = ROOT + "common/user/logout.do";
    /**
     * 登录
     */
    public static final String CHANGE = ROOT + "common/user/modify.do";/*修改密码*/
    public static final String CHANGE_NAME = ROOT + "common/user/alterUser.do";/*修改昵称*/
    public static final String CHANGE_PICTURE = ROOT + "common/user/userImg.do";/*上传头像*/
//    public static final String ABUOUT_HELP = "http://121.42.157.223:8080/BluetoothLock/zProgress/News.html";/*使用帮助url*/
//    public static final String ABUOUT_QUESTION = "http://115.28.225.251:8080/blk/Blook_upload/face/"; /*常见问题url*/
    public static final String ABUOUT_HELP = "http://dy-pr-file.oss-cn-hangzhou.aliyuncs.com/index.html";/*使用帮助url*/
    public static final String ABUOUT_QUESTION = "http://dy-pr-file.oss-cn-hangzhou.aliyuncs.com/index.html"; /*常见问题url*/

    public static final String ABUOUT_SUGGESTION = ROOT + "common/feedBack/add.do";/*意见反馈*/

    public static final String GETAUTHCODE = ROOT + "lock/verify/getAuthCode.do";//获取验证码
    public static final String GETAUTHCODE_AGAIN = ROOT + "lock/verify/getAgainCode.do";
    public static final String REGISTER = ROOT + "common/user/register.do";//注册
    public static final String GETKEYS = ROOT + "lock/lockKey/getKeysByMobile.do";//通过手机号获取钥匙列表
    public static final String GETLOCKRECORDS = ROOT + "lock/lock/getLockRecords.do";//获取钥匙记录
    public static final String GETKEYS_BY_MAC = ROOT + "lock/lockKey/getKeysByMacCode.do";//通过macCode获取
    public static final String GETGRANTKEYS = ROOT + "lock/lockKey/getGrantKeysByMobile.do";//
    public static final String ADD_KEY = ROOT + "lock/lockKey/addLockKey.do";
    public static final String LOGIN_OUT = ROOT + "common/user/logoff.do";
    public static final String RESET_PWD = ROOT + "common/user/seekPW.do";
    public static final String UPDATELOCK = ROOT + "lock/lock/updateLock.do";
    public static final String ADDLOCKRECORD = ROOT + "lock/lock/addLockRecord.do";
    public static final String UPDATEKEY = ROOT + "lock/lockKey/updateLockKey.do";
    public static final String SYNCLOCK = ROOT + "lock/lock/syncLock.do";
    public static final String ADDLOCKRECORDBATCH = ROOT + "lock/lock/addLockRecordBatch.do";
    public static final String MESSAGE = ROOT + "common/message/listPage.do";
    public static final String UNREADCOUNT = ROOT + "common/message/unReadCount.do";
    public static final String READALL = ROOT + "common/message/readAll.do";
    public static final String CLEARMESSAGE = ROOT + "common/message/clearMessage.do";
    public static final String GETVERSIONNEW = ROOT + "common/version/getVersionNew.do";
    public static final String ADDLOCKLOG = ROOT + "lock/lock/addLockLog.do";

    /**
     * 授权
     */
    public static final String GRANTLOCKKEY = ROOT + "lock/lockKey/grantLockKey.do";
    public static final String GRANTLOCKKEYBATCH = ROOT + "lock/lockKey/grantLockKeyBatch.do";
    /**
     * 忘记密码
     */
    public static final String FORGET = ROOT + "common/user/seekPW.do";
    public static final String IMAGE = ROOT + "common/image/image.do";
    public static final String GETADVERTISES = ROOT + "common/advertise/getAdvertises.do";
    public static final String GETNEWFIRMWARE = ROOT + "common/firmware/getNewFirmware.do";
    public static final String FIRMWARE = ROOT + "common/file/firmware.do";

    //通道闸
    public static final String GETKEYSBYMOBILE = ROOT + "gate/chain/getKeysByMobile.do";
    public static final String AREA_LIST = ROOT + "common/community/list.do";
    public static final String GATE_ADD = ROOT + "gate/chainApply/applyWg.do";
    public static final String GATE_ADD_FZ = ROOT + "gate/chainApply/applyFz.do";
    public static final String UPDATE_CHAIN = ROOT + "gate/chain/updateChain.do";
    public static final String GETGRANT_KEYSBYMOBILE = ROOT + "gate/chain/getMembersByMobile.do";
    public static final String REMOVECHAIN = ROOT + "gate/chain/removeChain.do";
    public static final String GETGATELOGS = ROOT + "gate/chain/getGateLogs.do";
    public static final String ADDKEYBYMOBILE = ROOT + "gate/chain/addKeyByMobile.do";
    public static final String GETCAHINAPPLYBYMOBILE = ROOT + "gate/chainApply/getChainApplyByMobile.do";
    public static final String HANDLEFZAPPLY = ROOT + "gate/chainApply/handleFzApply.do";
}
