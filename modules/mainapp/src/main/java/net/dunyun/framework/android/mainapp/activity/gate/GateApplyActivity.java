package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.GateApplyManageAdapter;
import net.dunyun.framework.android.mainapp.adapter.GateRecordAdapter;
import net.dunyun.framework.android.mainapp.adapter.GateRecordAdapterCallback;
import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapter;
import net.dunyun.framework.android.mainapp.adapter.KeysUserManageAdapterCallback;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacBiz;
import net.dunyun.framework.android.mainapp.biz.GetKeysByMacCallback;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyBiz;
import net.dunyun.framework.android.mainapp.biz.UpdateLockKeyCallback;
import net.dunyun.framework.android.mainapp.biz.gate.ApplyGateBiz;
import net.dunyun.framework.android.mainapp.biz.gate.ApplyGateCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GateApplyRecordBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GateApplyRecordCallback;
import net.dunyun.framework.android.mainapp.db.KeyPasswd;
import net.dunyun.framework.android.mainapp.db.KeyPasswdDbUtil;
import net.dunyun.framework.android.mainapp.db.gate.KeyChainUtil;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.ApplyRecordVo;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.android.mainapp.widget.AListView;
import net.dunyun.framework.android.mainapp.widget.ChoiceDialog;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.android.mainapp.widget.GateChoiceDialog;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
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
public class GateApplyActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterLoadListener, GateApplyRecordCallback, ApplyGateCallback {

    private Context context = null;
    @Bind(R.id.lv_user_list) AListView lv_list;
    @Bind(R.id.mPullRefreshView) PullToRefreshView mPullRefreshView;

    private GateApplyRecordBiz gateApplyRecordBiz;
    private List<ApplyRecordVo> applyRecordVos;
    private GateApplyManageAdapter gateRecordAdapter;
    private ApplyGateBiz applyGateBiz;

    DeleteDialog deleteDialog;
    private int selectedIndex;
    private String  status;
    private String  chainId;
    private UserVo userVo;
    private String phone;
    private boolean isAdmin = false;
    private GateChoiceDialog choiceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        baseSetContentView(R.layout.activity_gate_apply);
        ButterKnife.bind(this);
        setTitle("申请审核");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);

        mPullRefreshView.setLoadMoreEnable(false);
        mPullRefreshView.getFooterView().hide();

        gateApplyRecordBiz = new GateApplyRecordBiz(this);
        applyGateBiz = new ApplyGateBiz(this);

        gateApplyRecordBiz.getRecords(userVo);

        deleteDialog = new DeleteDialog(this, "请确认是否处理该申请", new DialogListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_ok:

                        if("1".equals(status)){
                            showDia();
                            deleteDialog.dismiss();
                        }else{
                            applyGateBiz.apply(applyRecordVos.get(selectedIndex).getId(),status, "");
                            deleteDialog.dismiss();
                        }
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

    List<LockVo> lockVoListTotal;
    private void showDia(){
        lockVoListTotal = new ArrayList<LockVo>();

        List<KeyChainVo> keyChainVos = KeyChainUtil.query(userVo.getMobile());
        if(keyChainVos ==null || keyChainVos.size() == 0){
            ToastUtil.showToast(GateApplyActivity.this, "暂无可以选择的通道闸或者门禁");
            return;
        }
        for (KeyChainVo  keyChainVo: keyChainVos){
            LockVo lockVo = new LockVo();
            lockVo.setMacName(keyChainVo.getChainName());
            lockVo.setAddress(keyChainVo.getId());
            lockVoListTotal.add(lockVo);
        }

        if (choiceDialog != null && choiceDialog.isShowing()) {
            choiceDialog.dismiss();
        }
        choiceDialog = new GateChoiceDialog(context, lockVoListTotal, new DialogListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismiss();
            }

            @Override
            public void onItemClick(int position) {
                LockVo lockVo = lockVoListTotal.get(position);
                applyGateBiz.apply(applyRecordVos.get(selectedIndex).getId(),status, lockVo.getAddress());
            }
        });
        choiceDialog.show();
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        GateApplyActivity.this.finish();
    }

    private void initData(){
        gateRecordAdapter = new GateApplyManageAdapter(context, new GateRecordAdapterCallback() {
            @Override
            public void onItemSelected(int index, boolean ok) {
                if(ok){
                    status = "1";
                }else{
                    status = "0";
                }
                selectedIndex = index;
                deleteDialog.show();
            }
        }, isAdmin);
        lv_list.setAdapter(gateRecordAdapter);
        gateRecordAdapter.clear();
        gateRecordAdapter.refreshAdapter(applyRecordVos);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView pullToRefreshView) {
        gateApplyRecordBiz.getRecords(userVo);
    }

    @Override
    public void onFooterLoad(PullToRefreshView pullToRefreshView) {
        mPullRefreshView.onFooterLoadFinish();
    }

    @Override
    public void onSuccess(String result) {
        ToastUtil.showToastInThread(context, "处理成功");
        gateApplyRecordBiz.getRecords(userVo);
        deleteDialog.dismiss();
    }

    @Override
    public void onFailed(String result) {
        ToastUtil.showToastInThread(context, "处理失败，"+result);
        deleteDialog.dismiss();
    }

    @Override
    public void onGatesSuccess(List<ApplyRecordVo> applyRecordVos) {
        this.applyRecordVos = applyRecordVos;
        GateApplyActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        mPullRefreshView.onHeaderRefreshFinish();
    }

    @Override
    public void onGatesFailed(String result) {
        ToastUtil.showToast(context, result);
        mPullRefreshView.onHeaderRefreshFinish();
    }
}
