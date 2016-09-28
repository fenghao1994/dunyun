package net.dunyun.framework.android.mainapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.ui.view.refreshview.PullToRefreshView;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.adapter.MessageAdapter;
import net.dunyun.framework.android.mainapp.biz.MessageBiz;
import net.dunyun.framework.android.mainapp.biz.MessageCallback;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.widget.AListView;
import net.dunyun.framework.android.mainapp.widget.DeleteDialog;
import net.dunyun.framework.android.mainapp.widget.DialogListener;
import net.dunyun.framework.lock.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *　系统消息
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 *
 */
public class MsgActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterLoadListener, MessageCallback {

    private Context context = null;
    @Bind(R.id.lv_list) ListView lv_list;
    @Bind(R.id.mPullRefreshView) PullToRefreshView mPullRefreshView;

    private MessageBiz messageBiz;
    private List<MessageVo> messageVoLists = new ArrayList<MessageVo>();
    private MessageAdapter messageAdapter;

    private int rows = 10;
    private int pageNumber = 1;
    PageVo pageVo;

    private boolean headerRefresh = false;
    private DeleteDialog deleteDialog;
    private LoadingDialog loadingDialog;
    private UserVo userVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if(userVoStr != null){
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            String phone = userVo.getMobile();
        }
        baseSetContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        setTitle("系统消息");
        setRightTwoButton(R.drawable.title_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog = new DeleteDialog(MsgActivity.this, "请确认是否清空消息", new DialogListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.btn_ok:
                                loadingDialog = DialogUtil.showWaitDialog(context, "请等待...");
                                messageBiz.clearMessage(userVo);
                                deleteDialog.dismiss();
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
                deleteDialog.show();
            }
        });

        mPullRefreshView.setOnHeaderRefreshListener(this);
        mPullRefreshView.setOnFooterLoadListener(this);


        initData();

        messageBiz = new MessageBiz(this);
        messageBiz.getMessage(userVo, pageNumber, rows);

        mPullRefreshView.headerRefreshing();
    }

    @OnClick(R.id.title_left_btn) void leftOnclick(){
        MsgActivity.this.finish();
    }

    private void initData(){
        messageAdapter = new MessageAdapter(context);
        lv_list.setAdapter(messageAdapter);
//        lv_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageAdapter.clear();
        messageAdapter.refreshAdapter(messageVoLists);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView pullToRefreshView) {
        headerRefresh = true;
        pageNumber = 1;
        messageBiz.getMessage(userVo, pageNumber, rows);
    }

    @Override
    public void onFooterLoad(PullToRefreshView pullToRefreshView) {
        headerRefresh = false;
        if(pageVo != null){
            if(pageNumber <= pageVo.getTotalPages()){
                messageBiz.getMessage(userVo, pageNumber, rows);
            }else{
                mPullRefreshView.onFooterLoadFinish();
                ToastUtil.showToast(MsgActivity.this, "没有更多数据了");
            }
        }else{
            mPullRefreshView.onFooterLoadFinish();
        }
    }

    @Override
    public void onMessageSuccess(List<MessageVo> messageVoList, PageVo pageVo) {
        this.pageVo = pageVo;
        if(headerRefresh){
            this.messageVoLists = messageVoList;
            pageNumber = 1;
            messageAdapter.clear();
            messageAdapter.refreshAdapter(messageVoLists);
            mPullRefreshView.onHeaderRefreshFinish();
        }else{
            mPullRefreshView.onFooterLoadFinish();
            pageNumber++;
            messageAdapter.refreshAdapter(messageVoList);
        }

        MessageBiz readAll =  new MessageBiz(this);
        readAll.readAll(userVo);
    }

    @Override
    public void onMessageFailed(String result) {
        if(headerRefresh){
            mPullRefreshView.onHeaderRefreshFinish();
        }else{
            mPullRefreshView.onFooterLoadFinish();
        }
        ToastUtil.showToast(context, result);
    }

    @Override
    public void onMessageCount(String number) {

    }

    @Override
    public void onMessageClearSuccess(String result) {
        headerRefresh = true;
        pageNumber = 1;
        messageBiz = new MessageBiz(this);
        messageBiz.getMessage(userVo, pageNumber, rows);

        DialogUtil.cancelWaitDialog(loadingDialog);
    }

    @Override
    public void onMessageClearFailed(String result) {
        ToastUtil.showToast(MsgActivity.this, "清空失败");
        DialogUtil.cancelWaitDialog(loadingDialog);
    }

}
