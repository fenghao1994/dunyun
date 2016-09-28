package net.dunyun.framework.android.mainapp.activity.gate;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.DateUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;
import com.psoft.framework.android.base.utils.StrUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.AreaAdapter;
import net.dunyun.framework.android.mainapp.adapter.MessageAdapter;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.GrantLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GetAreaBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GetAreaCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.util.AesUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.AreaVo;
import net.dunyun.framework.android.mainapp.widget.time.JudgeDate;
import net.dunyun.framework.android.mainapp.widget.time.ScreenInfo;
import net.dunyun.framework.android.mainapp.widget.time.WheelMain;
import net.dunyun.framework.lock.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class AreaSelectActivity extends BaseActivity implements GetAreaCallback{

    private Context context = null;
    LoadingDialog loadingDialog;
    private UserVo userVo;
    private String phone;

    @Bind(R.id.lv_list)
    ListView lv_list;

    GetAreaBiz getAreaBiz;
    private AreaAdapter areaAdapter;

    private List<AreaVo> areaVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        baseSetContentView(R.layout.activity_area_select);

        ButterKnife.bind(this);
        setTitle("选择小区");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
            getAreaBiz = new GetAreaBiz(this);
            getAreaBiz.getAreaList(userVo);
        }

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("id", areaVoList.get(position).id);
                intent.putExtra("name", areaVoList.get(position).name);
                AreaSelectActivity.this.setResult(0, intent);
                AreaSelectActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        AreaSelectActivity.this.finish();
    }

    @Override
    public void onGetAreaSuccess(List<AreaVo> areaVos, int flag) {
        areaVoList = areaVos;
        areaAdapter = new AreaAdapter(context);
        lv_list.setAdapter(areaAdapter);
//        lv_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        areaAdapter.clear();
        areaAdapter.refreshAdapter(areaVos);
    }

    @Override
    public void onGetAreaFailed(String result) {
        ToastUtil.showToast(context, "加载小区信息失败，"+result);
    }
}
