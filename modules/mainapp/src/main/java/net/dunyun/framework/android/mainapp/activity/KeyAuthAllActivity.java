package net.dunyun.framework.android.mainapp.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.adapter.AuthAdapterCallback;
import net.dunyun.framework.android.mainapp.adapter.AuthKeysAdapter;
import net.dunyun.framework.android.mainapp.adapter.AuthPhonesAdapter;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyAllBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.ChoiceDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.time.JudgeDate;
import net.dunyun.framework.android.mainapp.widget.time.ScreenInfo;
import net.dunyun.framework.android.mainapp.widget.time.WheelMain;
import net.dunyun.framework.lock.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 远程授权(多个)
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyAuthAllActivity extends BaseActivity implements GrantLockKeyCallback {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private WheelMain wheelMain;
    private Context context = null;
    @Bind(R.id.tv_auth_start)
    TextView tv_auth_start;
    @Bind(R.id.tv_auth_end)
    TextView tv_auth_end;
    @Bind(R.id.btn_key)
    Button btn_key;
    @Bind(R.id.btn_phones)
    Button btn_phones;
    @Bind(R.id.et_key_number)
    EditText et_key_number;
    @Bind(R.id.lv_keys)
    ListView lv_keys;
    @Bind(R.id.lv_phone)
    ListView lv_phone;
    AuthKeysAdapter authKeysAdapter;
    AuthPhonesAdapter authPhonesAdapter;

    List<LockVo> lockVoListTotal;
    List<String> phoneList;
    List<LockVo> lockVoList;
    private LoadingDialog loadingDialog;
    GrantLockKeyAllBiz grantLockKeyAllBiz;
    String phone;
    String token;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_auth_all);
        ButterKnife.bind(this);
        setTitle("远程授权");
        lockVoList = new ArrayList<LockVo>();
        phoneList = new ArrayList<String>();
        phone = SharedUtil.getString(context, "phone");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            token = userVo.getToken();
        }
        grantLockKeyAllBiz = new GrantLockKeyAllBiz(this);

        lockVoListTotal = LockDbUtil.query(SharedUtil.getString(context, "phone"));
        for (LockVo lockVo:lockVoListTotal) {
            if(!"1".equals(BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone).getKeyType())){
                lockVoListTotal.remove(lockVo);
            }
        }
        authKeysAdapter = new AuthKeysAdapter(context, new AuthAdapterCallback() {
            @Override
            public void onItemSelected(int index) {
                for (LockVo lockVo : lockVoList) {
                    if (lockVo.getMacCode().equals(authKeysAdapter.getItem(index).getMacCode())) {
                        lockVoList.remove(lockVo);
                        break;
                    }
                }
                authKeysAdapter.remove(index);
                if (choiceDialog != null && choiceDialog.isShowing()) {
                    choiceDialog.dismiss();
                }
            }
        });
        lv_keys.setAdapter(authKeysAdapter);
        authKeysAdapter.refreshAdapter(lockVoList);

        authPhonesAdapter = new AuthPhonesAdapter(context, new AuthAdapterCallback() {
            @Override
            public void onItemSelected(int index) {
                for (String string : phoneList) {
                    if (string.equals(authPhonesAdapter.getItem(index))) {
                        phoneList.remove(string);
                        break;
                    }
                }
                authPhonesAdapter.remove(index);
            }
        });
        lv_phone.setAdapter(authPhonesAdapter);
        authPhonesAdapter.refreshAdapter(phoneList);
    }

    @OnTouch(R.id.tv_auth_start)
    boolean startTime() {
        showStartDateDialog();
        return false;
    }

    @OnTouch(R.id.tv_auth_end)
    boolean endTime() {
        showEndDateDialog();
        return false;
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        String startTime = tv_auth_start.getText().toString();
        String endTime = tv_auth_end.getText().toString();
        String number = et_key_number.getText().toString().trim();

        if (StrUtil.isEmpty(startTime) || !startTime.contains("-")) {
            ToastUtil.showToast(context, "请选择开始时间");
            return;
        }
        if (StrUtil.isEmpty(endTime) || !endTime.contains("-")) {
            ToastUtil.showToast(context, "请选择结束时间");
            return;
        }
        if (StrUtil.isEmpty(number)) {
            ToastUtil.showToast(context, "请输入授权次数");
            return;
        }
        if (lockVoList.size() == 0) {
            ToastUtil.showToast(context, "请选择钥匙");
            return;
        }
        if (phoneList.size() == 0) {
            ToastUtil.showToast(context, "请选择授权手机号");
            return;
        }
        loadingDialog = DialogUtil.showWaitDialog(this);
        grantLockKeyAllBiz.grantLockKey(phoneList, lockVoList, startTime+":00", endTime+":00", number, phone, token);
    }

    private void showStartDateDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(KeyAuthAllActivity.this);
        wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = tv_auth_start.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        new AlertDialog.Builder(context)
                .setTitle("选择时间")
                .setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_auth_start.setText(wheelMain.getTime());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void showEndDateDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(KeyAuthAllActivity.this);
        wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = tv_auth_end.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        new AlertDialog.Builder(context)
                .setTitle("选择时间")
                .setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_auth_end.setText(wheelMain.getTime());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void updateKeys() {
        authKeysAdapter.clear();
        authKeysAdapter.refreshAdapter(lockVoList);
    }

    private void updatePhones() {
        authPhonesAdapter.clear();
        authPhonesAdapter.refreshAdapter(phoneList);
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        KeyAuthAllActivity.this.finish();
    }

    ChoiceDialog choiceDialog;

    @OnClick(R.id.btn_key)
    void checkKeys() {
        if (choiceDialog != null && choiceDialog.isShowing()) {
            choiceDialog.dismiss();
        }
        choiceDialog = new ChoiceDialog(context, lockVoListTotal, new DialogListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismiss();
            }

            @Override
            public void onItemClick(int position) {
                LockVo lockVo = lockVoListTotal.get(position);
                KeyPasswd keyPasswd = KeyPasswdDbUtil.query(lockVo.getMacCode(), phone);
                if("1".equals(lockVo.getIsGrant())){
                    if(keyPasswd != null && keyPasswd.password != null && keyPasswd.password.length() == 6){
                        if (!lockVoList.contains(lockVoListTotal.get(position))) {
                            lockVoList.add(lockVoListTotal.get(position));
                            updateKeys();
                        }
                    }else {
                        ToastUtil.showToast(context, "此钥匙未保存开锁密码,不能授权");
                    }
                }else{
                    ToastUtil.showToast(context, "此钥匙管理员未开启远程授权功能");
                }
            }
        });
        choiceDialog.show();
    }

    @OnClick(R.id.btn_phones)
    void checkPhones() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                ContentResolver reContentResolverol = getContentResolver();
                Uri contactData = data.getData();
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                if(cursor != null && cursor.moveToFirst()){
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phone.moveToNext()) {
                        String usernumber = phone
                                .getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String number = usernumber.replace(" ", "").trim();
                        if (!phoneList.contains(number)) {
                            phoneList.add(number);
                            updatePhones();
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onGrantLockKeySuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "授权成功");

    }

    @Override
    public void onGrantLockKeyFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(context, "授权失败 "+result);
    }
}
