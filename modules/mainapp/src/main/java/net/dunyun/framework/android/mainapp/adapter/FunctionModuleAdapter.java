package net.dunyun.framework.android.mainapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.vo.FunctionModuleVo;
import net.dunyun.framework.lock.R;

/**
 * 名称：FunctionModuleAdapter
 *
 * @author chenzp
 * @date 2011-12-10
 * @version
 */
public class FunctionModuleAdapter extends BaseAdapter{
  
	private Context mContext;
	//xml转View对象
    private LayoutInflater mInflater;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List<FunctionModuleVo> mData;
    //Map中的key
    private String[] mFrom;
    //view的id
    private int[] mTo;

   /**
    * 构造方法
    * @param context
    * @param data 列表展现的数据
    * @param resource 单行的布局
    * @param from Map中的key
    * @param to view的id
    */
    public FunctionModuleAdapter(Context context, List<FunctionModuleVo> data,
                                 int resource, String[] from, int[] to){
    	this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           convertView = mInflater.inflate(mResource, parent, false);
			   holder = new ViewHolder();
			   holder.itemsIcon = ((ImageView) convertView.findViewById(R.id.itemsIcon));
			   holder.itemsName = ((TextView) convertView.findViewById(R.id.itemsName));
			   convertView.setTag(holder);
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          final FunctionModuleVo mFunctionModule = (FunctionModuleVo)mData.get(position);
          holder.itemsIcon.setImageDrawable(mFunctionModule.getIcon());
          holder.itemsName.setText(mFunctionModule.getName());

          return convertView;
    }
    
    /**
	 * View元素
	 */
	static class ViewHolder {
		ImageView itemsIcon;//图标
		TextView itemsName;//名称
	}
    
}
