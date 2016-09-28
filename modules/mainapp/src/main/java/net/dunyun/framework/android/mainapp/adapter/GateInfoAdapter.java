package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.gate.GateInfoVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class GateInfoAdapter extends AngmaBaseAdapter<GateInfoVo> {

	public GateInfoAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final GateInfoVo gateInfoVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			if("1".equals(gateInfoVo.getType())){
				view = inflater.inflate(R.layout.item_gate_info_title, null);
			}else{
				view = inflater.inflate(R.layout.item_gate_info_content, null);
			}
			viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_content.setText(gateInfoVo.getName());

		return view;
	}

	final static class ViewHolder {
		TextView tv_content;
	}
}