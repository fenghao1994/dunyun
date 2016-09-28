package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;

import net.dunyun.framework.android.mainapp.vo.ApplyRecordVo;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class GateApplyManageAdapter extends AngmaBaseAdapter<ApplyRecordVo> {

	private GateRecordAdapterCallback callback;
	private boolean isAdmin;
	public GateApplyManageAdapter(Context mContext, GateRecordAdapterCallback callback, boolean isAdmin) {
		super(mContext);
		this.callback = callback;
		this.isAdmin = isAdmin;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final ApplyRecordVo applyRecordVo = list.get(position);
		int temp = position%2;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_apply_record, null);
			viewHolder.tv_apply_phone = (TextView) view.findViewById(R.id.tv_apply_phone);
			viewHolder.tv_apply_reason = (TextView) view.findViewById(R.id.tv_apply_reason);
			viewHolder.btn_ok = (Button) view.findViewById(R.id.btn_ok);
			viewHolder.btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tv_apply_phone.setText("申请人:"+applyRecordVo.getMobile());
		viewHolder.tv_apply_reason.setText("申请理由:"+applyRecordVo.getReason());

		viewHolder.btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onItemSelected(position, true);
			}
		});

		viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onItemSelected(position, false);
			}
		});

		return view;
	}

	final static class ViewHolder {
		TextView tv_apply_phone;
		TextView tv_apply_reason;
		Button btn_ok;
		Button btn_cancel;
	}
}