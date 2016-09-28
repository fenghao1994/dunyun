package net.dunyun.framework.android.mainapp.activity.gate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.ui.view.dialog.LoadingDialog;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.activity.BaseActivity;
import net.dunyun.framework.android.mainapp.adapter.AreaAdapter;
import net.dunyun.framework.android.mainapp.adapter.GateInfoAdapter;
import net.dunyun.framework.android.mainapp.biz.gate.GetAreaBiz;
import net.dunyun.framework.android.mainapp.biz.gate.GetAreaCallback;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.android.mainapp.vo.gate.AreaVo;
import net.dunyun.framework.android.mainapp.vo.gate.DoorsVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateInfoVo;
import net.dunyun.framework.android.mainapp.vo.gate.GatesVo;
import net.dunyun.framework.android.mainapp.vo.gate.KeyChainVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class GateInfoActivity extends BaseActivity {

    private Context context = null;
    private UserVo userVo;
    private String phone;
    @Bind(R.id.tv_content)
    ListView tv_content;
    private KeyChainVo keyChainVo;
    private GateInfoAdapter gateInfoAdapter;
    private ArrayList<GateInfoVo> gateInfoVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle mainBundle = new Bundle();
        mainBundle.putInt("showScrollView", 1);
        super.onCreate(mainBundle);

        context = this;
        baseSetContentView(R.layout.activity_area_info);
        ButterKnife.bind(this);
        setTitle("详情");

        String userVoStr = SharedUtil.getString(context, UserVo.class + "");
        if (userVoStr != null) {
            userVo = JsonUtil.parseObject(userVoStr, UserVo.class);
            phone = userVo.getMobile();
        }

        Bundle bundle = getIntent().getExtras();
        keyChainVo = (KeyChainVo) bundle.getSerializable("keyChainVo");

        gateInfoVoList = new ArrayList<GateInfoVo>();
        List<GatesVo> gates = keyChainVo.getGates();
        for(GatesVo gatesVo : gates){
            GateInfoVo gateInfoVo = new GateInfoVo();
            gateInfoVo.setName(gatesVo.getUsername());
            gateInfoVo.setType("1");
            gateInfoVoList.add(gateInfoVo);

            List<DoorsVo> doorsVoList = gatesVo.getDoors();
            for (DoorsVo doorsVo : doorsVoList){
                GateInfoVo gate = new GateInfoVo();
                gate.setName(doorsVo.getDoorIndex()+"号闸");
                gate.setType("2");
                gateInfoVoList.add(gate);
            }
        }
        gateInfoAdapter = new GateInfoAdapter(this);
        tv_content.setAdapter(gateInfoAdapter);
        gateInfoAdapter.refreshAdapter(gateInfoVoList);
    }

    @OnClick(R.id.title_left_btn)
    void leftOnclick() {
        GateInfoActivity.this.finish();
    }

}
