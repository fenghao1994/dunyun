package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.MessageVo;
import net.dunyun.framework.android.mainapp.vo.gate.AreaVo;
import net.dunyun.framework.lock.R;

/**
 * 小区列表适配器
 * @author chenzp
 * @date 2015/11/19
 */
public class AreaAdapter extends AngmaBaseAdapter<AreaVo> {

	public AreaAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final AreaVo areaVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_area, null);
			viewHolder.tv_area = (TextView) view.findViewById(R.id.tv_area);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_area.setText(areaVo.getName());

		return view;
	}

	final static class ViewHolder {
		TextView tv_area;
	}
}