package net.dunyun.framework.android.mainapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.lock.R;

/**
 * 消息
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class NoticeFragment extends BaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view =  inflater.inflate(R.layout.fragment_notice, null, false);
            initViews(view);
        }
        return view;
    }

    private void initViews(View view){
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(getTitle());
        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(view, getResources().getString(R.string.main_tab_3), null, null, null);
    }
}
