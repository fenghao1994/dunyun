package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.GateUserManageAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapterCallback;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacBiz;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.DelGrantBiz;
import net.dunyun.framework.android.mainapp.biz.gate.DelGrantCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GrantBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GrantCallback;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.GrantVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.android.mainapp.widget.AListView;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class GateUserManageActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener, GrantCallback, DelGrantCallback{

    private Context context = null;
    @Bind(R.id.lv_user_list) AListView lv_list;
    @Bind(R.id.mPullRefreshView) PullToRefreshView mPullRefreshView;

    private List<GrantVo> keysList;
    private GateUserManageAdapter keysAdapter;

    DeleteDialog deleteDialog;
    private int selectedIndex;
    private UserVo userVo;
    private String phone;
    private boolean isAdmin = false;
    private KeyChainVo keyChainVo;
    private GrantBiz grantBiz;
    private DelGrantBiz delGrantBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_gate_user_manage);
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

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        grantBiz = new GrantBiz(this);
        delGrantBiz = new DelGrantBiz(this);

        deleteDialog = new DeleteDialog(this, "请确认是否删除该用户", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_ok:
                        delGrantBiz.delete(userVo, keysList.get(selectedIndex).getId());
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
        GateUserManageActivity.this.finish();
    }

    private void initData(){
        keysAdapter = new GateUserManageAdapter(context, new KeysUserManageAdapterCallback() {
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
        grantBiz.get(keyChainVo.getId(), userVo.getToken());
    }

    @Override
    public void onFooterLoad(PullToRefreshView pullToRefreshView) {
        mPullRefreshView.onFooterLoadFinish();
    }

    @Override
    public void onDelSuccess(String result) {
        ToastUtil.showToastInThread(context, "删除成功");
        grantBiz.get(keyChainVo.getId(), userVo.getToken());
        deleteDialog.dismiss();
    }

    @Override
    public void onDelFailed(String result) {
        ToastUtil.showToastInThread(context, "删除失败，"+result);
        deleteDialog.dismiss();
    }

    @Override
    public void onGetSuccess(List<GrantVo> grantVoList) {
        keysList = grantVoList;
        GateUserManageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        mPullRefreshView.onHeaderRefreshFinish();
    }

    @Override
    public void onGetFailed(String result) {
        ToastUtil.showToast(context, result);
        mPullRefreshView.onHeaderRefreshFinish();
    }
}
