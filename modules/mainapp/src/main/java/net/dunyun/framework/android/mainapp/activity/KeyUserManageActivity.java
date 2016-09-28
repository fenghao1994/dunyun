package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapterCallback;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacBiz;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.AListView;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *　锁用户管理
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class KeyUserManageActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterLoadListener, GetKeysByMacCallback, UpdateLockKeyCallback{

    private Context context = null;
    @Bind(R.id.lv_user_list) AListView lv_list;
    @Bind(R.id.mPullRefreshView) PullToRefreshView mPullRefreshView;
    private LockVo lockVo;

    private GetKeysByMacBiz getKeysByMacBiz;
    private List<KeyVo> keysList;
    private KeysUserManageAdapter keysAdapter;
    private UpdateLockKeyBiz updateLockKeyBiz;

    DeleteDialog deleteDialog;
    private int selectedIndex;
    private UserVo userVo;
    private String phone;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_key_user_manage);
        ButterKnife.bind(this);
        setTitle("用户管理");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);

        mPullRefreshView.setLoadMoreEnable(false);
        mPullRefreshView.getFooterView().hide();

        final Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo)bundle.getSerializable("lockVo");
        getKeysByMacBiz = new GetKeysByMacBiz(this);
        updateLockKeyBiz = new UpdateLockKeyBiz(this);
        getKeysByMacBiz.getKeys(lockVo.getMacCode(), userVo.getToken(), phone);

        KeyVo keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
        if("0".equals(keyVo.getKeyIndex())){
            isAdmin = true;
        }

        deleteDialog = new DeleteDialog(this, "请确认是否删除该用户", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_ok:
                        KeyVo keyVo = new KeyVo();
                        keyVo.setMacCode(keysList.get(selectedIndex).getMacCode());
                        keyVo.setKeyIndex(keysList.get(selectedIndex).getKeyIndex());
                        keyVo.setMobile(keysList.get(selectedIndex).getMobile());
                        keyVo.setKeyType(keysList.get(selectedIndex).getKeyType());
                        keyVo.setState("0");
                        updateLockKeyBiz.updateLockKey(keyVo, userVo);
                        break;
                    case R.id.btn_cancel:
                        deleteDialog.dismiss();
                        break;
                }
            }

            @Override
            public void onItemClick(int position) {

            }
        });

        mPullRefreshView.headerRefreshing();
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        KeyUserManageActivity.this.finish();
    }

    private void initData(){
        keysAdapter = new KeysUserManageAdapter(context, new KeysUserManageAdapterCallback() {
            @Override
            public void onItemSelected(int index) {
                selectedIndex = index;
                deleteDialog.show();
            }
        }, isAdmin);
        lv_list.setAdapter(keysAdapter);
        keysAdapter.clear();
        keysAdapter.refreshAdapter(keysList);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView pullToRefreshView) {
        getKeysByMacBiz.getKeys(lockVo.getMacCode(), userVo.getToken(), userVo.getMobile());
    }

    @Override
    public void onKeysSuccess(List<KeyVo> keyVoList) {
        keysList = keyVoList;
        KeyUserManageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        mPullRefreshView.onHeaderRefreshFinish();
    }

    @Override
    public void onKeysFailed(String result) {
        ToastUtil.showToast(context, result);
        mPullRefreshView.onHeaderRefreshFinish();
    }

    @Override
    public void onFooterLoad(PullToRefreshView pullToRefreshView) {
        mPullRefreshView.onFooterLoadFinish();
    }

    @Override
    public void onSuccess(String result) {
        ToastUtil.showToastInThread(context, "删除成功");
        getKeysByMacBiz.getKeys(lockVo.getMacCode(), userVo.getToken(), phone);
        deleteDialog.dismiss();
    }

    @Override
    public void onFailed(String result) {
        ToastUtil.showToastInThread(context, "删除失败，"+result);
        deleteDialog.dismiss();
    }
}
