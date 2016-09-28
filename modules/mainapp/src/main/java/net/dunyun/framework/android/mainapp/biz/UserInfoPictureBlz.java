package net.dunyun.framework.android.mainapp.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.v4.util.SimpleArrayMap;

import com.psoft.framework.android.base.network.http.BaseHttpResponseHandler;
import com.psoft.framework.android.base.network.http.HttpUtils;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.ImageUtil;
import com.psoft.framework.android.base.utils.LogUtil;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.util.UpLoadUtil;
import net.dunyun.framework.android.mainapp.vo.UserVo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-20
 * @Copyright:
 *
 */
public class UserInfoPictureBlz {

    private UserInfoPictureCallback m;

    public UserInfoPictureBlz(UserInfoPictureCallback m) {
        this.m = m;
    }

    public void userInfoPictureBlz(final UserVo userVo,String picPath){

        String fileKey = "file";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userVo.getToken());
        params.put("mobile", userVo.getMobile());

        UpLoadUtil uploadUtil =UpLoadUtil.getInstance();
        uploadUtil.uploadFile(picPath, fileKey, WebUrl.CHANGE_PICTURE, params, new BaseHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                super.onSuccess(statusCode, responseString);
                m.onSuccess(statusCode,responseString);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                super.onFailure(statusCode, responseString);
                m.onFailed(statusCode,responseString);
            }
        });

        LogUtil.i("sendData", picPath + " " + userVo.getToken() + " " + userVo.getMobile() + " ");
    }
}