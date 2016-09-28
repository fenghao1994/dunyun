package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.biz.UserInfoPictureBlz;
import net.dunyun.framework.android.mainapp.biz.UserInfoPictureCallback;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 名称：UserInfoPictureActivity.java
 * 描述：上传头像界面
 *
 * @author wuhx
 * @version v1.0
 * @date2016-04-21
 * @Copyright:
 */
public class UserInfoPictureActivity extends BaseActivity implements UserInfoPictureCallback {


    private ProgressDialog progressDialog = null;
    private UserInfoPictureBlz mUserInfoPictureBlz = null;
    private Context context = null;
    private static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private static final int SELECT_PIC_BY_TACK_PHOTO = 1;/*使用照相机拍照获取图片*/
    private Uri photoUri = null;
    private String picPath = null;
    private Intent intent_picture = new Intent();
    private UserVo userVo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_user_info_picture);
        ButterKnife.bind(this);
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
        }

    }

    @OnClick(R.id.bt_camera)
    void cameraOnclick() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            //Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
            showToast("内存卡不存在");
        }
    }

    @OnClick(R.id.bt_cancle)
    void cancleOnclick() {
        finish();
    }

    @OnClick(R.id.bt_pictures)
    void picturesOnclick() {
        //photoUri=null;
        intent_picture.setType("image/*");
        intent_picture.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent_picture, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 返回上一界面
     */
    class LeftOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /***
     * 提示信息
     *
     * @param content 提示内容
     */
    private void showToast(final String content) {
        UserInfoPictureActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错201", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错202", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        // Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        Cursor cursor = getContentResolver().query(photoUri, pojo, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
            LogUtil.i("ttt", " picPath = " + picPath + " columnIndex = " + columnIndex);
            //showToast(" 203picPath = " + picPath);
        }

        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {

            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // 去进行判断网络是否连接
            if (manager.getActiveNetworkInfo() != null) {
                progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.login_progressDialog_title),
                        getResources().getString(R.string.update_progressDialog_content));

                PushManager.getInstance().initialize(this.getApplicationContext());
                mUserInfoPictureBlz = new UserInfoPictureBlz(this);
                mUserInfoPictureBlz.userInfoPictureBlz(userVo, picPath);
            } else {
                showToast("请查看网络是否连接");
            }

        } else {
            Toast.makeText(this, "选择图片文件格式不正确", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * callback回调
     *
     * @param提示内容
     */
    @Override
    public void onSuccess(int code, String data) {
        DialogUtil.cancelProgressDialog(progressDialog);


        userVo.setAvatarUrl(data);
        SharedUtil.remove(context, UserVo.class + "");
        SharedUtil.putString(context, UserVo.class + "", JsonUtil.toJSONString(userVo));
        mainApplication.setUserVo(userVo);
        showToast("上传成功");
        /**查看修改后的本地信息**/
        String m = SharedUtil.getString(context, UserVo.class + "");
        UserVo xx = JsonUtil.parseObject(m, UserVo.class);
        LogUtil.i("修改后图片地址", "AvatarUrl+" + xx.getAvatarUrl());

    }

    @Override
    public void onFailed(int code, String data) {
        DialogUtil.cancelProgressDialog(progressDialog);
        showToast("上传失败," + data);
    }
}
