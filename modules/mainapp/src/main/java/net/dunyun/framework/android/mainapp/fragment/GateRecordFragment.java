package net.dunyun.framework.android.mainapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.adapter.GateRecordAdapter;
import net.dunyun.framework.android.mainapp.adapter.RecordAdapter;
import net.dunyun.framework.android.mainapp.biz.GetLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.GetLockRecordCallback;
import net.dunyun.framework.android.mainapp.biz.gate.GateRecordBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GateRecordCallback;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateRecordVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 钥匙
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class GateRecordFragment extends BaseFragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener
        , GateRecordCallback {

    LayoutInflater mInflater;
    private View view;
    @Bind(R.id.mPullRefreshView)
    PullToRefreshView mPullRefreshView;
    @Bind(R.id.lv_user_list)
    ListView lv_user_list;

    private List<GateRecordVo> lockRecordList;
    private GateRecordAdapter recordAdapter;

    GetLockRecordBiz getLockRecordBiz;

    private int rows = 10;
    private int pageNumber = 1;
    private String recordType = "";
    private boolean headerRefresh;
    PageVo pageVo;
    UserVo userVo;
    private GateRecordBiz gateRecordBiz;
    private String communityId;
    private String logType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            LogUtil.d("--onCreateView---");
            Bundle data = getArguments();//获得从activity中传递过来的值
            communityId = data.getString("communityId");
            logType = data.getString("logType");

            mInflater = inflater;
            view = inflater.inflate(R.layout.fragment_key_record_all, null, false);
            ButterKnife.bind(this, view);

            userVo = JsonUtil.parseObject(SharedUtil.getString(getActivity(), UserVo.class + ""), UserVo.class);

            initViews(view);

            gateRecordBiz = new GateRecordBiz(this);
            getData();
            mPullRefreshView.headerRefreshing();
        }
        return view;
    }

    private boolean isFirst = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData() {
        if (!isFirst) {
            gateRecordBiz.get(userVo,communityId,logType);
            isFirst = true;
            headerRefresh = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews(View view) {
        // 设置监听器
        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);

        lockRecordList = new ArrayList<GateRecordVo>();

        recordAdapter = new GateRecordAdapter(getActivity());
        lv_user_list.setAdapter(recordAdapter);
//        lv_user_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        recordAdapter.clear();
        recordAdapter.refreshAdapter(lockRecordList);
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        headerRefresh = false;

        mPullRefreshView.onFooterLoadFinish();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        headerRefresh = true;
        pageNumber = 1;
        if (view.getId() == mPullRefreshView.getId()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gateRecordBiz.get(userVo,communityId,logType);
                }
            });
        }
    }

    @Override
    public void onGateSuccess(List<GateRecordVo> list) {
        if (headerRefresh) {
            lockRecordList = list;
            recordAdapter.clear();
            recordAdapter.refreshAdapter(lockRecordList);
            mPullRefreshView.onHeaderRefreshFinish();
        } else {
            recordAdapter.refreshAdapter(lockRecordList);
            mPullRefreshView.onFooterLoadFinish();
        }
    }

    @Override
    public void onGateFailed(String result) {
        mPullRefreshView.onHeaderRefreshFinish();
        ToastUtil.showToast(getActivity(), result);
    }
}
