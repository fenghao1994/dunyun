package net.dunyun.framework.android.mainapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psoft.framework.android.base.adapter.AngmaBaseAdapter;
import com.psoft.framework.android.base.utils.JsonUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.SharedUtil;

import net.dunyun.framework.android.mainapp.util.BluetoothUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.UserVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;

/**
 *
 * @author chenzp
 * @date 2015/11/19
 */
public class KeysAdapter extends AngmaBaseAdapter<LockVo> {
    String phone;
    Context mContext;
	public KeysAdapter(Context mContext) {
		super(mContext);
        this.mContext = mContext;
        phone = SharedUtil.getString(mContext, "phone");
	}

	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		LockVo lockVo = list.get(position);
//        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_key, null);
            viewHolder.ItemImage = (ImageView) view.findViewById(R.id.ItemImage);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }

        phone = SharedUtil.getString(mContext, "phone");

        if(position == (list.size() - 1)){
            viewHolder.ItemImage.setBackgroundResource(R.drawable.add_key);
            viewHolder.ItemImage.setImageDrawable(null);
            viewHolder.name.setText("添加钥匙");
        }else{
            KeyVo keyVo = BluetoothUtil.getKeyVo(lockVo.getLockKeys(), phone);
            if(keyVo != null){
                if(keyVo.getKeyType() != null && "2".equals(keyVo.getKeyType())){
                    viewHolder.name.setText("[远]"+keyVo.getKeyName());
                }else{
                    viewHolder.name.setText(keyVo.getKeyName());
                }
            }

            int power = 100;
            try{
                power = Integer.parseInt(lockVo.getPower());
            }catch (Exception e){
//                e.printStackTrace();
                power = 100;
            }
            lockVo.getPower();
            if(0 <= power && power <= 12.5){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_0);
            }else if(12.5 < power && power <= 25){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_1);
            }else if(25 < power && power <= 37.5){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_2);
            }else if(37.5 < power && power <= 50){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_3);
            }else if(50 < power && power <= 62.5){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_4);
            }else if(62.5 < power && power <= 75){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_5);
            }else if(75 < power && power <= 87.5){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_6);
            }else if(power > 87.5){
                viewHolder.ItemImage.setBackgroundResource(R.drawable.key_power_7);
            }

            boolean isLocked = true;
            if("1".equals(lockVo.getTdState())){
                isLocked = false;
            }
            if(isLocked){
                viewHolder.ItemImage.setImageResource(R.drawable.key_status_1);
            }else{
                viewHolder.ItemImage.setImageResource(R.drawable.key_status_2);
            }
        }

		return view;
	}

	final static class ViewHolder {
		ImageView ItemImage;
		TextView name;
	}
}