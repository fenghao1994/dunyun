package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class RecordAdapter extends AngmaBaseAdapter<LockRecordVo> {

	public RecordAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final LockRecordVo lockRecordVo = list.get(position);
//		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_lock_record, null);

			viewHolder.ItemImage = (ImageView) view.findViewById(R.id.ItemImage);
			viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
			viewHolder.tv_nick_name = (TextView) view.findViewById(R.id.tv_nick_name);
			viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
			viewHolder.tv_item1 = (TextView) view.findViewById(R.id.tv_item1);
			viewHolder.tv_item2 = (TextView) view.findViewById(R.id.tv_item2);
			viewHolder.tv_item3 = (TextView) view.findViewById(R.id.tv_item3);
			viewHolder.tv_item4 = (TextView) view.findViewById(R.id.tv_item4);
			viewHolder.tv_item5 = (TextView) view.findViewById(R.id.tv_item5);

			view.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) view.getTag();
//            resetViewHolder(viewHolder);
//		}

        if(lockRecordVo.getNickName() != null && !lockRecordVo.getNickName().equals("null")){
            viewHolder.tv_nick_name.setText("昵称:"+lockRecordVo.getNickName());
        }else{
            viewHolder.tv_nick_name.setText("昵称:"+lockRecordVo.getMobile());
        }

        //1:添加钥匙，2:授权钥匙，3:开锁
        String type = "";
        if("1".equals(lockRecordVo.getRecordType())){
            type = "添加";
			viewHolder.tv_time.setText(lockRecordVo.getCreateDt());
			viewHolder.rl_bottom.setVisibility(View.GONE);
        }else if("2".equals(lockRecordVo.getRecordType())){
            type = "授权";
            viewHolder.rl_bottom.setVisibility(View.VISIBLE);
			viewHolder.tv_time.setText(lockRecordVo.getCreateDt());
        }else if("3".equals(lockRecordVo.getRecordType())){
            type = "开门";
            viewHolder.tv_time.setText(lockRecordVo.getCreateDt());
			viewHolder.rl_bottom.setVisibility(View.GONE);

            if((null==lockRecordVo.getMobile() || "null".equals(lockRecordVo.getMobile())) ){
                viewHolder.tv_nick_name.setText("密码开门");
                viewHolder.tv_phone.setVisibility(View.INVISIBLE);
            }
        }else if("4".equals(lockRecordVo.getRecordType())){
            type = "关门";
            viewHolder.tv_time.setText(lockRecordVo.getCreateDt());
            viewHolder.rl_bottom.setVisibility(View.GONE);
            viewHolder.tv_phone.setVisibility(View.GONE);
            viewHolder.tv_nick_name.setVisibility(View.GONE);
        }

        if(lockRecordVo.getAvatarUrl() != null && !"null".equals(lockRecordVo.getAvatarUrl()) && lockRecordVo.getAvatarUrl().startsWith("http")){
            Picasso.with(mContext).load(lockRecordVo.getAvatarUrl())
                    .placeholder(R.drawable.me_photo1)
                    .error(R.drawable.me_photo1)
                    .into(viewHolder.ItemImage);
        }

        viewHolder.tv_type.setText(type);

        viewHolder.tv_phone.setText("手机号:"+lockRecordVo.getMobile());

        viewHolder.tv_item1.setText("授权人:"+lockRecordVo.getGrantMbl());
        viewHolder.tv_item2.setText("被授权人:"+lockRecordVo.getMobile());
        viewHolder.tv_item3.setText("授权开始时间:"+lockRecordVo.getGrantBdt());
        viewHolder.tv_item4.setText("授权截止时间:"+lockRecordVo.getGrantEdt());
        viewHolder.tv_item5.setText("授权次数:"+lockRecordVo.getGrantNum());

		return view;
	}

    protected void resetViewHolder(ViewHolder p_ViewHolder)
    {
        p_ViewHolder.ItemImage.setImageDrawable(null);
        p_ViewHolder.tv_type.setText(null);
        p_ViewHolder.tv_nick_name.setText(null);
        p_ViewHolder.tv_phone.setText(null);
        p_ViewHolder.tv_time.setText(null);

        p_ViewHolder.tv_item1.setText(null);
        p_ViewHolder.tv_item2.setText(null);
        p_ViewHolder.tv_item3.setText(null);
        p_ViewHolder.tv_item4.setText(null);
        p_ViewHolder.tv_item5.setText(null);
    }

	final class ViewHolder {
		ImageView ItemImage;
		TextView tv_type;
		TextView tv_nick_name;
		TextView tv_phone;
		TextView tv_time;
		RelativeLayout rl_bottom;

		TextView tv_item1;
		TextView tv_item2;
		TextView tv_item3;
		TextView tv_item4;
		TextView tv_item5;
	}
}