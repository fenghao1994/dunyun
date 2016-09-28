package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ListView;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapterCallback;
import net.dunyun.framework.android.mainapp.adapter.NearKeyAdapter;
import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.db.NearKeyDb;
import net.dunyun.framework.android.mainapp.db.NearKeyDbUtil;
import net.dunyun.framework.android.mainapp.db.YaoKeyDb;
import net.dunyun.framework.android.mainapp.db.YaoKeyDbUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.NearKeyVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 摇一摇开锁界面
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class ShakeActivity extends BaseActivity {

    private Context context = null;
    private NearKeyAdapter nearKeyAdapter;
    @Bind(R.id.lv_list)
    ListView lv_list;
    @Bind(R.id.yao_check)
    CheckBox yao_check;
    private UserVo userVo;

    private List<NearKeyVo> nearKeyVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        baseSetContentView(R.layout.activity_shake);
        ButterKnife.bind(this);
        setTitle("摇一摇开锁");
        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);

        }
        initData();
    }

    private void initData(){
        nearKeyVoList = new ArrayList<NearKeyVo>();
        List<LockVo> lockVos = LockDbUtil.query(userVo.getMobile());
        boolean isSet = false;
        if(lockVos != null && lockVos.size()>0){
            for (int i = 0; i < lockVos.size(); i++) {
                KeyVo keyVo = BluetoothUtil.getKeyVo(lockVos.get(i).getLockKeys(), userVo.getMobile());
                if(keyVo != null){
                    NearKeyVo nearKeyVo = new NearKeyVo();
                    nearKeyVo.setMobile(keyVo.getMobile());
                    nearKeyVo.setMacCode(keyVo.getMacCode());
                    nearKeyVo.setKeyName(keyVo.getKeyName());

                    YaoKeyDb nearKeyDb = YaoKeyDbUtil.query(keyVo.getMacCode(), userVo.getMobile(), userVo.getMobile());
                    if(nearKeyDb != null){
                        isSet = true;
                        nearKeyVo.setRssi(1);
                    }
                    nearKeyVoList.add(nearKeyVo);
                }
            }
        }

        if(isSet){
            yao_check.setChecked(true);
        }else{
            yao_check.setChecked(false);
        }

        nearKeyAdapter = new NearKeyAdapter(context, new KeysUserManageAdapterCallback() {
            @Override
            public void onItemSelected(int index) {
                del(nearKeyVoList.get(index));
            }
        }, new NearKeyAdapter.ButtonCallback() {
            @Override
            public void onItemSelected(int index) {
            //打开
                insert(nearKeyVoList.get(index));
            }
        }, true);
        lv_list.setAdapter(nearKeyAdapter);
        nearKeyAdapter.clear();
        nearKeyAdapter.refreshAdapter(nearKeyVoList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    private void del(NearKeyVo nearKeyVo){
        YaoKeyDbUtil.delete(nearKeyVo.getMacCode(), userVo.getMobile(), userVo.getMobile());
        initData();
    }

    private void insert(NearKeyVo nearKeyVo){
        YaoKeyDbUtil.insert(nearKeyVo.getMacCode(), userVo.getMobile(), userVo.getMobile());
        initData();
    }

    @OnClick(R.id.yao_check) void yao_check(){
        if(!yao_check.isChecked()){
            YaoKeyDbUtil.clear(userVo.getMobile());
            initData();
        }else{
            List<LockVo> lockVos = LockDbUtil.query(userVo.getMobile());
            List<KeyVo> keyVos = new ArrayList<KeyVo>();

            if(lockVos != null && lockVos.size()>0){
                for (int i = 0; i < lockVos.size(); i++) {
                    KeyVo keyVo = BluetoothUtil.getKeyVo(lockVos.get(i).getLockKeys(), userVo.getMobile());
                    if(keyVo != null){
                        keyVos.add(keyVo);
                    }
                }
            }

            if(keyVos != null && keyVos.size()>0){
                for (int i = 0; i < keyVos.size(); i++) {
                    YaoKeyDbUtil.insert(keyVos.get(i).macCode, keyVos.get(i).mobile, keyVos.get(i).mobile);
                }
            }

            initData();
        }
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        ShakeActivity.this.finish();
    }

}
