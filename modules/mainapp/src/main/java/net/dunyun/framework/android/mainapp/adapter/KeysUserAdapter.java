package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class KeysUserAdapter extends AngmaBaseAdapter<KeyVo> {

	public KeysUserAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final KeyVo keyVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_key, null);
			viewHolder.ItemImage = (ImageView) view.findViewById(R.id.ItemImage);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
        if(position == (list.size() - 1)){
            viewHolder.ItemImage.setBackgroundResource(R.drawable.add_key);
            viewHolder.ItemImage.setImageDrawable(null);
            viewHolder.name.setText("添加钥匙");
        }else{
            viewHolder.name.setText(keyVo.getKeyName());
        }
		return view;
	}

	final static class ViewHolder {
		ImageView ItemImage;
		TextView name;
	}
}