package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.widget.EditText;

import com.psoft.framework.android.base.network.http.TextHttpResponseHandler;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.lock.R;

/**
 * 名称：UpdatePasswordActivity.java
 * 描述：修改密码界面.
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:重庆平软科技有限公司
 *
 */
public class UpdatePasswordActivity extends BaseActivity {
    private EditText et_old_password = null;
    private EditText et_new_password = null;
    private EditText et_new_password_check = null;
    private Context context = null;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_update_password);

        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_check = (EditText) findViewById(R.id.et_new_password_check);
        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.update_password_title),new LeftOnClickListener(), null, null);
    }

    /***
     * 修改密码
     * @param v
     */
    public void updatePassword(View v) {
        final String oldPassword = et_old_password.getText().toString().trim();
        final String newPassword = et_new_password.getText().toString().trim();
        final String newPasswordCheck = et_new_password_check.getText().toString().trim();
        if (StrUtil.isEmpty(oldPassword)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        if (StrUtil.isEmpty(newPassword)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        if (StrUtil.isEmpty(newPasswordCheck)) {
            ToastUtil.showToast(context, getResources().getString(R.string.login_password_input_tips));
            return;
        }
        if (!newPassword.equals(newPasswordCheck)) {
            ToastUtil.showToast(context, getResources().getString(R.string.update_password_check_tips));
            return;
        }
        progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.login_progressDialog_title),
                getResources().getString(R.string.update_progressDialog_content));

        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
//        paramsMap.put("accountId", authenVo.getAccountId()+"");
//        paramsMap.put("newPassword", newPassword+"");
//        paramsMap.put("salt", salt);

        mainApplication.httpUtils.httpPost(ConfigVo.UPDATE_PASSWORD_URL, paramsMap, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseString) {
                LogUtil.d(responseString);
                DialogUtil.cancelProgressDialog(progressDialog);
                showToast(getResources().getString(R.string.update_password_success));
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                LogUtil.d(responseString);
                DialogUtil.cancelProgressDialog(progressDialog);
                showToast(getResources().getString(R.string.update_password_failed) + "," + responseString);
            }
        });
    }

    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        UpdatePasswordActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(context, content);
            }
        });
    }

    /**
     *  返回上一界面
     */
    class  LeftOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
}

