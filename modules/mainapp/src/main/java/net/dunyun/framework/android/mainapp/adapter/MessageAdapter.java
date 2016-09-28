package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class MessageAdapter extends AngmaBaseAdapter<MessageVo> {

	public MessageAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final MessageVo messageVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_message, null);
			viewHolder.tv_txt = (TextView) view.findViewById(R.id.tv_txt);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_time.setText(messageVo.getCreateDt().replace("2016-","").replace(" ", "\r"));
		viewHolder.tv_txt.setText(messageVo.getContent());

		return view;
	}

	final static class ViewHolder {
		TextView tv_txt;
		TextView tv_time;
	}
}