package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.vo.FunctionModuleUpdateVo;
import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;
import com.psoft.framework.android.base.utils.AppUtil;

/**
 * 联系人适配器
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class FunctionModuleUpdateAdapter extends AngmaBaseAdapter<FunctionModuleUpdateVo> {

	public FunctionModuleUpdateAdapter(Context mContext) {
		super(mContext);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final FunctionModuleUpdateVo functionModuleVo = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(net.dunyun.framework.lock.R.layout.item_function_module, null);
			viewHolder.install_label_tv = (TextView) view.findViewById(net.dunyun.framework.lock.R.id.install_label_tv);
			viewHolder.itemsIcon = (ImageView) view.findViewById(net.dunyun.framework.lock.R.id.itemsIcon);

			viewHolder.itemsName = (TextView) view.findViewById(net.dunyun.framework.lock.R.id.itemsName);
			viewHolder.version_label_tv = (TextView) view.findViewById(net.dunyun.framework.lock.R.id.version_label_tv);
			viewHolder.uninstall_btn = (Button) view.findViewById(net.dunyun.framework.lock.R.id.uninstall_btn);
			viewHolder.install_update_btn = (Button) view.findViewById(net.dunyun.framework.lock.R.id.install_update_btn);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if(functionModuleVo.isInstall()){
			viewHolder.install_label_tv.setText("已安装");
			viewHolder.uninstall_btn.setVisibility(View.VISIBLE);

			if(functionModuleVo.isNeedUpdateVersion()){
				viewHolder.version_label_tv.setText("版本:"+functionModuleVo.getInstallVersion()+"---->"+functionModuleVo.getServerVersion());
				viewHolder.install_update_btn.setText("更新");
			}else{
				viewHolder.version_label_tv.setText("版本:"+functionModuleVo.getServerVersion());
				viewHolder.install_update_btn.setText("安装");
			}
		}else{
			viewHolder.install_label_tv.setText("未安装");
			viewHolder.uninstall_btn.setVisibility(View.INVISIBLE);

			viewHolder.version_label_tv.setText("版本:"+functionModuleVo.getServerVersion());
			viewHolder.install_update_btn.setText("安装");

		}
		viewHolder.itemsIcon.setImageDrawable(functionModuleVo.getIcon());
		viewHolder.itemsName.setText(functionModuleVo.getName());

		viewHolder.uninstall_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.uninstallApk(mContext, functionModuleVo.getPackageName());
			}
		});

		return view;
	}

	final static class ViewHolder {
		TextView install_label_tv;
		ImageView itemsIcon;
		TextView itemsName;
		TextView version_label_tv;
		Button uninstall_btn;
		Button install_update_btn;
	}
}