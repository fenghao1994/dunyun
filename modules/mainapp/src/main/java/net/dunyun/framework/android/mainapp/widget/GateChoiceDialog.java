package net.dunyun.framework.android.mainapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.dunyun.framework.android.mainapp.adapter.AuthKeysChoiceAdapter;
import net.dunyun.framework.android.mainapp.adapter.GateChoiceAdapter;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.lock.R;

import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/5
 * @Copyright:重庆平软科技有限公司
 */
public class GateChoiceDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener{
    private DialogListener dialogListener;
    private Button btn_ok;
    private Button btn_cancel;
    private ListView lv_list;
    GateChoiceAdapter authKeysChoiceAdapter;
    Context context;
    List<LockVo> lockVoList;

    public GateChoiceDialog(Context context) {
        super(context, R.style.dunyunDialogStyle);
    }

    public GateChoiceDialog(Context context, List<LockVo> lockVoList,
                            DialogListener dialogListener) {
        super(context, R.style.dunyunDialogStyle);
        this.context = context;
        this.dialogListener = dialogListener;
        this.lockVoList = lockVoList;
    }

    private GateChoiceDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choice_dialog);
        lv_list = (ListView)this.findViewById(R.id.lv_list);
        btn_ok = (Button)this.findViewById(R.id.btn_ok);
        btn_cancel = (Button)this.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        lv_list.setOnItemClickListener(this);

        authKeysChoiceAdapter = new GateChoiceAdapter(context);
        lv_list.setAdapter(authKeysChoiceAdapter);
        authKeysChoiceAdapter.refreshAdapter(lockVoList);
    }

    @Override
    public void onClick(View v) {
        dialogListener.onClick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialogListener.onItemClick(position);
        dialogListener.onClick(view);
    }
}