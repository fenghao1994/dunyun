package net.dunyun.framework.android.mainapp.fragment;

import android.content.Intent;
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

import net.dunyun.framework.android.mainapp.adapter.RecordAdapter;
import net.dunyun.framework.android.mainapp.biz.GetLockRecordBiz;
import net.dunyun.framework.android.mainapp.biz.GetLockRecordCallback;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
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
public class RecordFragment extends BaseFragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener
        , GetLockRecordCallback {

    LayoutInflater mInflater;
    private View view;
    @Bind(R.id.mPullRefreshView)
    PullToRefreshView mPullRefreshView;
    @Bind(R.id.lv_user_list)
    ListView lv_user_list;

    private List<LockRecordVo> lockRecordList;
    private RecordAdapter recordAdapter;

    GetLockRecordBiz getLockRecordBiz;

    String macCode;
    String keyType;
    String type;
    private int rows = 10;
    private int pageNumber = 1;
    //1:添加钥匙，2:授权钥匙，3：开锁，4：关锁，5：远程报警，6：门锁检测
    private String recordType = "";
    private boolean headerRefresh;
    PageVo pageVo;
    UserVo userVo;

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
            macCode = data.getString("macCode");
            type = data.getString("type");
            keyType = data.getString("keyType");
            if ("0".equals(type)) {
                type = "";
            }
            recordType = type;
            mInflater = inflater;
            view = inflater.inflate(R.layout.fragment_key_record_all, null, false);
            ButterKnife.bind(this, view);

            userVo = JsonUtil.parseObject(SharedUtil.getString(getActivity(), UserVo.class + ""), UserVo.class);

            initViews(view);

            getLockRecordBiz = new GetLockRecordBiz(this);
            if (getUserVisibleHint()) {
                getData();
                mPullRefreshView.headerRefreshing();
            }
        }
        return view;
    }

    private boolean isFirst = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        /**
         * 判断此Fragment是否正在前台显示
         * 通过判断就知道是否要进行数据加载了
         */
        if (isVisibleToUser && isVisible()) {
            getData();
            mPullRefreshView.headerRefreshing();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData() {
        if (!isFirst) {
            getLockRecordBiz.getLockRecords(userVo, keyType, macCode, recordType, pageNumber, rows);
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

        lockRecordList = new ArrayList<LockRecordVo>();

        recordAdapter = new RecordAdapter(getActivity());
        lv_user_list.setAdapter(recordAdapter);
//        lv_user_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        recordAdapter.clear();
        recordAdapter.refreshAdapter(lockRecordList);
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        headerRefresh = false;
        if (pageVo != null) {
            if (pageNumber <= pageVo.getTotalPages()) {
                getLockRecordBiz.getLockRecords(userVo, keyType, macCode, recordType, pageNumber, rows);
            } else {
                ToastUtil.showToast(getActivity(), "没有更多数据了");
                mPullRefreshView.onFooterLoadFinish();
            }
        } else {
            mPullRefreshView.onFooterLoadFinish();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        headerRefresh = true;
        pageNumber = 1;
        if (view.getId() == mPullRefreshView.getId()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getLockRecordBiz.getLockRecords(userVo, keyType, macCode, recordType, pageNumber, rows);
                }
            });
        }
    }

    @Override
    public void onLockRecordSuccess(List<LockRecordVo> lockRecordVoList, PageVo pageVo) {
        this.pageVo = pageVo;
        if (headerRefresh) {
            lockRecordList = lockRecordVoList;
            recordAdapter.clear();
            recordAdapter.refreshAdapter(lockRecordVoList);
            mPullRefreshView.onHeaderRefreshFinish();
        } else {
            recordAdapter.refreshAdapter(lockRecordVoList);
            mPullRefreshView.onFooterLoadFinish();
        }
        pageNumber++;
    }

    @Override
    public void onLockRecordFailed(String result) {
        mPullRefreshView.onHeaderRefreshFinish();
        ToastUtil.showToast(getActivity(), result);
    }
}
