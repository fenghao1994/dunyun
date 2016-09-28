package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.squareup.picasso.Picasso;

import net.dunyun.framework.android.mainapp.common.WebUrl;
import net.dunyun.framework.android.mainapp.vo.LockRecordVo;
import net.dunyun.framework.android.mainapp.vo.NearKeyVo;
import net.dunyun.framework.lock.R;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class NearKeyAdapter extends AngmaBaseAdapter<NearKeyVo> {

    public interface ButtonCallback {
        void onItemSelected(int index);
    }
    ButtonCallback buttonCallback;
    KeysUserManageAdapterCallback callback;
    boolean flag;

	public NearKeyAdapter(Context mContext, KeysUserManageAdapterCallback callback, ButtonCallback buttonCallback, boolean flag) {
		super(mContext);
        this.callback = callback;
        this.buttonCallback = buttonCallback;
        this.flag = flag;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final NearKeyVo nearKeyVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.item_near_key, null);
			viewHolder.tv_key_name = (TextView) view.findViewById(R.id.tv_key_name);
			viewHolder.cb_check = (CheckBox) view.findViewById(R.id.cb_check);
			viewHolder.btn_set = (Button) view.findViewById(R.id.btn_set);
			viewHolder.tv_rssi = (TextView) view.findViewById(R.id.tv_rssi);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
        if(flag){
            viewHolder.btn_set.setVisibility(View.INVISIBLE);
            viewHolder.tv_rssi.setVisibility(View.INVISIBLE);
        }

        viewHolder.tv_key_name.setText(nearKeyVo.getKeyName());
        viewHolder.tv_rssi.setText(nearKeyVo.getRssi()+"");

        if(nearKeyVo.getRssi() != 0){
            viewHolder.cb_check.setChecked(true);
        }else {
            viewHolder.cb_check.setChecked(false);
        }

        viewHolder.btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCallback.onItemSelected(position);
            }
        });

        final ViewHolder viewHolders = viewHolder;

        viewHolders.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag){
                    if(!viewHolders.cb_check.isChecked()){
                        callback.onItemSelected(position);
                    }else{
                        buttonCallback.onItemSelected(position);
                    }
                }else {
                    if(!viewHolders.cb_check.isChecked()){
                        callback.onItemSelected(position);
                    }else{
                        ToastUtil.showToast(mContext, "请设置感应距离");
                        viewHolders.cb_check.setChecked(false);
                    }
                }

            }
        });
		return view;
	}

	final static class ViewHolder {
		TextView tv_key_name;
		CheckBox cb_check;
		Button btn_set;
		TextView tv_rssi;
	}
}