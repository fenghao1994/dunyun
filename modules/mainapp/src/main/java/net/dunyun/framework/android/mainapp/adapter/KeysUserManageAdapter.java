package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class KeysUserManageAdapter extends AngmaBaseAdapter<KeyVo> {

	private KeysUserManageAdapterCallback callback;
	private boolean isAdmin;
	public KeysUserManageAdapter(Context mContext, KeysUserManageAdapterCallback callback, boolean isAdmin) {
		super(mContext);
		this.callback = callback;
		this.isAdmin = isAdmin;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final KeyVo keyVo = list.get(position);
		int temp = position%2;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_lock_user, null);
			viewHolder.admin = (ImageView) view.findViewById(R.id.admin);
			viewHolder.tv_key_index = (TextView) view.findViewById(R.id.tv_key_index);
			viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
			viewHolder.ib_del = (ImageButton) view.findViewById(R.id.ib_del);
			viewHolder.ll_item_lock_user = (LinearLayout) view.findViewById(R.id.ll_item_lock_user);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tv_key_index.setText(keyVo.getKeyIndex());
		viewHolder.tv_phone.setText(keyVo.getMobile());
		if("1".equals(keyVo.getState())){
			viewHolder.tv_state.setText("正常");
		}else{
			viewHolder.tv_state.setText("待删除");
		}
		if(temp == 0){
			viewHolder.ll_item_lock_user.setBackgroundResource(R.color.user_bg_color);
		}else {
			viewHolder.ll_item_lock_user.setBackgroundResource(R.color.white);
		}

		if("0".equals(keyVo.getKeyIndex())){
			viewHolder.admin.setVisibility(View.VISIBLE);
			viewHolder.ib_del.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.admin.setVisibility(View.INVISIBLE);
			viewHolder.ib_del.setVisibility(View.VISIBLE);
		}

        if(isAdmin){
            if(!"0".equals(keyVo.getKeyIndex())){
                viewHolder.ib_del.setVisibility(View.VISIBLE);
            }
        }else{
            viewHolder.ib_del.setVisibility(View.INVISIBLE);
        }

		viewHolder.ib_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onItemSelected(position);
			}
		});

		return view;
	}

	final static class ViewHolder {
		ImageView admin;
		TextView tv_key_index;
		TextView tv_phone;
		TextView tv_state;
		ImageButton ib_del;
		LinearLayout ll_item_lock_user;
	}
}