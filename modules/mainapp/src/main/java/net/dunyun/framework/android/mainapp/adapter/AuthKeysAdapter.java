package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.lock.R;

/**
 * 授权钥匙列表适配器
 * @author chenzp
 * @date 2015/11/19
 */
public class AuthKeysAdapter extends AngmaBaseAdapter<LockVo> {
    private String phone;
    private AuthAdapterCallback authAdapterCallback;

	public AuthKeysAdapter(Context mContext, AuthAdapterCallback authAdapterCallback) {
		super(mContext);
        phone = SharedUtil.getString(mContext, "phone");
        this.authAdapterCallback = authAdapterCallback;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final LockVo lockVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_auth, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.iv_del = (ImageView) view.findViewById(R.id.iv_del);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
        KeyVo keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
        viewHolder.tv_name.setText(keyVo.getKeyName());
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