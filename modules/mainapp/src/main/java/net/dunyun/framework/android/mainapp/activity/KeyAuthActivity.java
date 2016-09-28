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
import android.widget.EditText;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.biz.GrantLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.time.JudgeDate;
import net.dunyun.framework.android.mainapp.widget.time.ScreenInfo;
import net.dunyun.framework.android.mainapp.widget.time.WheelMain;
import net.dunyun.framework.lock.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 远程授权(单个)
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyAuthActivity extends BaseActivity implements GrantLockKeyCallback {

    private Context context = null;
    private WheelMain wheelMain;
    @Bind(R.id.tv_auth_start)
    TextView tv_auth_start;
    @Bind(R.id.tv_auth_end)
    TextView tv_auth_end;
    @Bind(R.id.et_auth_phone)
    EditText et_auth_phone;
    @Bind(R.id.et_key_name)
    EditText et_key_name;
    @Bind(R.id.et_key_number)
    EditText et_key_number;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    GrantLockKeyBiz grantLockKeyBiz;
    private LockVo lockVo;
    KeyPasswd keyPasswd;
    KeyVo keyVo;
    LoadingDialog loadingDialog;
    private UserVo userVo;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_auth);
        ButterKnife.bind(this);
        setTitle("远程授权");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");

        grantLockKeyBiz = new GrantLockKeyBiz(this);
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), userVo.getMobile());
        et_key_name.setText(keyVo.getKeyName());

        keyPasswd = KeyPasswdDbUtil.query(lockVo.getMacCode(),userVo.getMobile());

        tv_auth_start.setText(DateUtil.getCurrentDate(DateUtil.dateFormatYMDHM));

        tv_auth_end.setText(DateUtil.getCurrentDateByOffset(DateUtil.dateFormatYMDHM, Calendar.HOUR_OF_DAY, 24));
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        KeyAuthActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        String phone = et_auth_phone.getText().toString().trim();
        String keyName = et_key_name.getText().toString().trim();
        String startTime = tv_auth_start.getText().toString();
        String endTime = tv_auth_end.getText().toString();
        String number = et_key_number.getText().toString().trim();

        if (StrUtil.isEmpty(phone)) {
            ToastUtil.showToast(context, "请输入手机号");
            return;
        }
        if (StrUtil.isEmpty(keyName)) {
            ToastUtil.showToast(context, "请输入钥匙名称");
            return;
        }
        if (StrUtil.isEmpty(startTime) || !startTime.contains("-")) {
            ToastUtil.showToast(context, "请选择开始时间");
            return;
        }
        if (StrUtil.isEmpty(endTime) || !endTime.contains("-")) {
            ToastUtil.showToast(context, "请选择结束时间");
            return;
        }
        if (StrUtil.isEmpty(number)) {
            number = "1000";
//            ToastUtil.showToast(context, "请输入授权次数");
//            return;
        }
        if (keyPasswd == null) {
            ToastUtil.showToast(context, "此钥匙手机未缓存开锁密码，不能进行授权");
            return;
        }
        loadingDialog = DialogUtil.showWaitDialog(this);
        int index = Integer.parseInt(keyVo.getKeyIndex());
        String addTm = keyVo.getAddTm();
        String passwd = AesUtil.getInstance().encrypt(keyPasswd.password.getBytes());
        grantLockKeyBiz.grantLockKey(lockVo.getMacCode(), index, keyName ,phone, startTime+":00",
                endTime+":00", number, passwd, userVo.getToken(), addTm);
    }

    @OnClick(R.id.btn_contact)
    void contact() {
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
                cursor.moveToFirst();
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phone.moveToNext()) {
                    String usernumber = phone
                            .getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    et_auth_phone.setText(usernumber.replace(" ", "").trim());
                }
                break;

            default:
                break;
        }
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

    /***
     * 显示时间选择控件
     */
    private void showStartDateDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(KeyAuthActivity.this);
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

    /***
     * 显示时间选择控件
     */
    private void showEndDateDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(KeyAuthActivity.this);
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

    @Override
    public void onGrantLockKeySuccess(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "授权成功");
    }

    @Override
    public void onGrantLockKeyFailed(String result) {
        DialogUtil.cancelWaitDialog(loadingDialog);
        ToastUtil.showToast(this, "授权失败，"+result);

    }
}
