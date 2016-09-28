package net.dunyun.framework.android.mainapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.ContactsVo;
import net.dunyun.framework.lock.R;

/**
 * 联系人详情
 *
 * @author Angma <WangZe>
 * @date ${date}
 */
public class ContactsDetailActivity extends BaseActivity {

    private TextView name_tv;
    private RelativeLayout phone_rl;
    private TextView phone_tv;
    private ContactsVo mobileContactsVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(getWindow().getDecorView(), getResources().getString(R.string.contacts_detail_title), new LeftOnClickListener(), null, null);
        phone_rl = (RelativeLayout)findViewById(R.id.phone_rl);
        name_tv = (TextView)findViewById(R.id.name_tv);
        phone_tv = (TextView)findViewById(R.id.phone_tv);
        mobileContactsVo = (ContactsVo)getIntent().getSerializableExtra("mobileContactsVo");

        name_tv.setText(mobileContactsVo.getName());
        phone_tv.setText(mobileContactsVo.getPhone());

        phone_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileContactsVo.getPhone()));
                startActivity(intent);
            }
        });
    }


    /**
     *  返回上一界面
     */
    class  LeftOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            finish();
        }
    }

}
