package net.dunyun.framework.android.mainapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.activity.BaseFragmentActivity;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.adapter.ViewPagerAdapter;
import net.dunyun.framework.android.mainapp.fragment.BaseFragment;
import net.dunyun.framework.android.mainapp.fragment.RecordFragment;
import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.lock.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 门锁记录
 *
 * @author chenzp
 * @version v1.0
 * @date：2014-10-29 下午11:52:13
 * @Copyright:
 */
public class KeyRecordActivity extends BaseFragmentActivity {

    private ViewPagerAdapter adapter;
    private int currentItem;

    @Bind(R.id.tv_title_all)
    TextView tv_title_all;
    @Bind(R.id.tv_title_open)
    TextView tv_title_open;
    @Bind(R.id.tv_title_close)
    TextView tv_title_close;
    @Bind(R.id.tv_title_add)
    TextView tv_title_add;
    @Bind(R.id.tv_title_auth)
    TextView tv_title_auth;

    @Bind(R.id.tv_title_all_sep)
    TextView tv_title_all_sep;
    @Bind(R.id.tv_title_open_sep)
    TextView tv_title_open_sep;
    @Bind(R.id.tv_title_close_sep)
    TextView tv_title_close_sep;
    @Bind(R.id.tv_title_add_sep)
    TextView tv_title_add_sep;
    @Bind(R.id.tv_title_auth_sep)
    TextView tv_title_auth_sep;

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.title_left_btn)
    ImageButton title_left_btn;
    @Bind(R.id.right_two_btn)
    Button right_two_btn;
    @Bind(R.id.right_one_btn)
    Button right_one_btn;
    @Bind(R.id.right_two_btn_lay)
    RelativeLayout right_two_btn_lay;
    @Bind(R.id.title_center_txt)
    TextView title_center_txt;
    private List<Fragment> fragmentList;
    private LockVo lockVo;
    private KeyVo keyVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_key_record);

        ButterKnife.bind(this);
        String phone = SharedUtil.getString(this, "phone");
        Bundle bundle = getIntent().getExtras();
        lockVo = (LockVo) bundle.getSerializable("lockVo");
        keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);

        right_two_btn_lay.setVisibility(View.INVISIBLE);
        right_one_btn.setVisibility(View.INVISIBLE);
        title_center_txt.setText("门锁记录");

        fragmentList = new ArrayList<Fragment>();

        initFragments();
    }

    private void initFragments() {
        //1:添加钥匙，2:授权钥匙，3：开锁，4：关锁，5：远程报警，6：门锁检测
        int[] types = {0, 3, 4, 1, 2};
        int i = 0;
        while (i < 5) {
            BaseFragment recordFragment = new RecordFragment();
            Bundle args = new Bundle();
            args.putString("macCode", lockVo.getMacCode());
            args.putString("keyType", keyVo.getKeyType());
            args.putString("type", "" + types[i]);
            recordFragment.setArguments(args);
            fragmentList.add(recordFragment);
            i++;
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                titleChange(arg0);
                currentItem = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick(R.id.tv_title_all)
    void tv_title_all() {
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.tv_title_open)
    void tv_title_open() {
        viewPager.setCurrentItem(1);
    }

    @OnClick(R.id.tv_title_close)
    void tv_title_close() {
        viewPager.setCurrentItem(2);
    }

    @OnClick(R.id.tv_title_add)
    void tv_title_add() {
        viewPager.setCurrentItem(3);
    }

    @OnClick(R.id.tv_title_auth)
    void tv_title_auth() {
        viewPager.setCurrentItem(4);
    }

    @OnClick(R.id.title_left_btn)
    void title_left_btn_click() {
        this.finish();
    }

    private void titleChange(int currentItem) {
        tv_title_all.setTextColor(getResources().getColor(R.color.record_title_color));
        tv_title_open.setTextColor(getResources().getColor(R.color.record_title_color));
        tv_title_close.setTextColor(getResources().getColor(R.color.record_title_color));
        tv_title_add.setTextColor(getResources().getColor(R.color.record_title_color));
        tv_title_auth.setTextColor(getResources().getColor(R.color.record_title_color));

        tv_title_all_sep.setBackgroundResource(R.color.record_title_color);
        tv_title_open_sep.setBackgroundResource(R.color.record_title_color);
        tv_title_close_sep.setBackgroundResource(R.color.record_title_color);
        tv_title_add_sep.setBackgroundResource(R.color.record_title_color);
        tv_title_auth_sep.setBackgroundResource(R.color.record_title_color);
        if (currentItem == 0) {
            tv_title_all.setTextColor(getResources().getColor(R.color.record_title_sep_color));
            tv_title_all_sep.setBackgroundResource(R.color.record_title_sep_color);
        } else if (currentItem == 1) {
            tv_title_open.setTextColor(getResources().getColor(R.color.record_title_sep_color));
            tv_title_open_sep.setBackgroundResource(R.color.record_title_sep_color);
        } else if (currentItem == 2) {
            tv_title_close.setTextColor(getResources().getColor(R.color.record_title_sep_color));
            tv_title_close_sep.setBackgroundResource(R.color.record_title_sep_color);
        } else if (currentItem == 3) {
            tv_title_add.setTextColor(getResources().getColor(R.color.record_title_sep_color));
            tv_title_add_sep.setBackgroundResource(R.color.record_title_sep_color);
        } else if (currentItem == 4) {
            tv_title_auth.setTextColor(getResources().getColor(R.color.record_title_sep_color));
            tv_title_auth_sep.setBackgroundResource(R.color.record_title_sep_color);
        }
    }

}
