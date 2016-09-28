package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.lock.R;

/**
 *授权钥匙手机号列表适配器
 * @author chenzp
 * @date 2015/11/19
 */
public class AuthPhonesAdapter extends AngmaBaseAdapter<String> {
    private AuthAdapterCallback authAdapterCallback;
	public AuthPhonesAdapter(Context mContext, AuthAdapterCallback authAdapterCallback) {
		super(mContext);
        this.authAdapterCallback = authAdapterCallback;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final String phone = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_auth, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.iv_del = (ImageView) view.findViewById(R.id.iv_del);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
        viewHolder.tv_name.setText(phone);
        viewHolder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authAdapterCallback.onItemSelected(position);
            }
        });
		return view;
	}

	final static class ViewHolder {
		TextView tv_name;
		ImageView iv_del;
	}
}