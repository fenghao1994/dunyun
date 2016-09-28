package net.dunyun.framework.android.mainapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.widget.EditText;

import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import com.psoft.framework.android.base.network.http.TextHttpResponseHandler;
import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.StrUtil;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.ContactsVo;
import net.dunyun.framework.lock.R;

/**
 * 名称：UpdatePasswordActivity.java
 * 描述：修改个人信息界面.
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:重庆平软科技有限公司
 *
 */
public class UpdateUserInfoActivity extends BaseActivity {
    private EditText name_et = null;
    private Context context = null;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_update_user_info);

        name_et = (EditText) findViewById(R.id.name_et);
        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.me_changename_title),new LeftOnClickListener(), null, null);
        name_et.setText(mainApplication.getMobileContactsVo().getName());
    }

    /***
     * 修改密码
     * @param v
     */
    public void okBtnClick(View v) {
        final String name = name_et.getText().toString().trim();
        if (StrUtil.isEmpty(name)) {
            ToastUtil.showToast(context, getResources().getString(R.string.input_name));
            return;
        }
        ContactsVo mobileContactsVo = mainApplication.getMobileContactsVo();

        progressDialog = DialogUtil.showProgressDialog(context, getResources().getString(R.string.login_progressDialog_title),
                getResources().getString(R.string.update_progressDialog_content));

        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
        paramsMap.put("contactsId", mobileContactsVo.getContactsId()+"");
        paramsMap.put("name", name);

        mainApplication.httpUtils.httpPost(ConfigVo.CONTACTS_UPDATE_URL, paramsMap, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseString) {
                LogUtil.d(responseString);
                DialogUtil.cancelProgressDialog(progressDialog);
                showToast(getResources().getString(R.string.update_password_success));
                getContactsByAccountId();
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                LogUtil.d(responseString);
                DialogUtil.cancelProgressDialog(progressDialog);
                showToast(getResources().getString(R.string.update_password_failed) + "," + responseString);
            }
        });
    }

    private void getContactsByAccountId(){
//        String accountId = mainApplication.getAuthenVo().getAccountId()+"";
        SimpleArrayMap<String, String> paramsMap = new SimpleArrayMap<String, String>();
//        paramsMap.put("accountId", accountId);
        mainApplication.httpUtils.httpPost(ConfigVo.CONTACTS_ONE_URL, paramsMap, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseString) {
                LogUtil.d(responseString);
                ContactsVo mobileContactsVo = JsonUtil.parseObject(responseString, ContactsVo.class);
                mainApplication.setMobileContactsVo(mobileContactsVo);
            }

            @Override
            public void onFailure(int statusCode, String responseString) {
                LogUtil.d(responseString);
            }
        });
    }

    /***
     * 提示信息
     * @param content 提示内容
     */
    private void showToast(final String content) {
        UpdateUserInfoActivity.this.runOnUiThread(new Runnable() {
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

