package net.dunyun.framework.android.mainapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.psoft.bluetooth.utils.LogUtil;
import com.psoft.framework.android.base.network.http.ResponseHandlerInterface;
import com.psoft.framework.android.base.utils.FileUtil;
import com.psoft.framework.android.base.utils.ImageUtil;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.biz.UserInfoPictureCallback;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


/**
 * <DL>
 * <DD>上传图片类、接口说明.</DD><BR>
 * </DL>
 *
 * @author wuhx
 * @version v1.0
 * @date：2016-04-20
 * @Copyright:
 */
public class UpLoadUtil {


    private UpLoadUtil() {

    }

    /**
     * 单例模式获取上传工具类
     *
     * @return
     */
    public static UpLoadUtil getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UpLoadUtil();
        }

        return uploadUtil;
    }

    private static UpLoadUtil uploadUtil;
    private final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private String reason = "请检查网络";
    private static final String TAG = "UploadUtil";
    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 10 * 1000; // 超时时间

    private static final String CHARSET = "utf-8"; // 设置编码

    private String results = null;
    /**
     * android上传文件到服务器
     *
     * @param filePath   需要上传的文件的路径
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    ResponseHandlerInterface resHandler = null;

    public void uploadFile(final String filePath, final String fileKey, final String RequestURL,
                           final Map<String, String> param, final ResponseHandlerInterface responseHandler) {
        if (filePath == null) {
            Log.e(TAG, "request error+上传失败03");
            return;
        }

        resHandler = responseHandler;
        try {
            new Thread() {
                public void run() {

                    LogUtil.d("---------results---------"+results);
                    toUploadFile(filePath, fileKey, RequestURL, param);
                    ResultBean resultBean = JsonUtil.parseObject(results, ResultBean.class);
                    if (resultBean.code != 100 || results == null) {
                        switch (resultBean.code) {
                            case 102:
                                reason = "无效token或手机号";
                                break;
                            case 200:
                                reason = "数据异常";
                                break;
                            case 201:
                                reason = "文件上传失败";
                                break;
                            case 202:
                                reason = "报错数据失败";
                                break;
                            default:
                                reason = "请检查网络";
                                break;
                        }
                        responseHandler.onFailure(202, reason);
                    } else {
                        responseHandler.onSuccess(resultBean.code, resultBean.data);
                    }
                }
            }.start();

        } catch (Exception e) {
            Log.e(TAG, "request error+上传失败04" + e.getMessage());
            e.printStackTrace();
            responseHandler.onFailure(202, reason);
        }
    }

    /**
     * android上传文件到服务器
     * 网络连接
     */
    private void toUploadFile(String filepath, String fileKey, String RequestURL,
                              Map<String, String> param) {
        File files = new File(filepath);
        Bitmap ompressedBitmap = ImageUtil.getScaleBitmap(files, 1200, 1200);
        float scanle = (float) 0.8;
        while (ompressedBitmap.getByteCount() > 200000) {
            ompressedBitmap = ImageUtil.scaleBitmap(ompressedBitmap, scanle);
        }

        FileUtil.writeBitmapToSD("/sdcard/test.jpeg", ompressedBitmap, true);
        File file = new File("/sdcard/test.jpeg");
        /*重点的传输方式，用para会造成读取不到token*/
        RequestURL = RequestURL + "?" + "mobile=" + param.get("mobile") + "&token=" + param.get("token");
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            String params = "";
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            dos.write(params.getBytes());
            Log.e(TAG, "params:" + params);

            /**上传文件*/
            FileInputStream fStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = fStream.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
            }
            fStream.close();
            Log.e(TAG, "curLen:" + curLen + "file.getName()" + file.getName());
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /*** 获取响应码 200=成功 当响应成功，获取响应的流*/
            int res = conn.getResponseCode();
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                results = sb1.toString();
                Log.e(TAG, "result ssss: " + results);
                return;
            } else {
                Log.e(TAG, "request error" + results);
                return;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "request error+上传失败01" + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.e(TAG, "request error+上传失败02" + e.getMessage());

            e.printStackTrace();
        }
        resHandler.onFailure(123, "dadadsf");
    }

    class ResultBean {
        public int code;
        public String data;
    }


}
