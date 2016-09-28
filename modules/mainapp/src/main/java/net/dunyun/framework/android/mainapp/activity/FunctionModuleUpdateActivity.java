package net.dunyun.framework.android.mainapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.psoft.framework.android.base.ui.view.DialogUtil;
import com.psoft.framework.android.base.ui.view.ToastUtil;
import com.psoft.framework.android.base.utils.LogUtil;
import com.psoft.framework.android.base.utils.PackageUtil;
import com.psoft.framework.android.base.utils.VersionUpgraderCallback;
import com.psoft.framework.android.base.utils.VersionUpgraderUtil;
import com.psoft.framework.android.base.vo.VersionUpgraderVo;
import net.dunyun.framework.android.mainapp.adapter.FunctionModuleUpdateAdapter;
import net.dunyun.framework.android.mainapp.util.ActivityTitleUtil;
import net.dunyun.framework.android.mainapp.vo.ConfigVo;
import net.dunyun.framework.android.mainapp.vo.FunctionModuleUpdateVo;
import net.dunyun.framework.android.mainapp.vo.FunctionModuleVo;
import net.dunyun.framework.lock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 *
 * @author chenzp
 * @date 2015/8/17
 */
public class FunctionModuleUpdateActivity extends BaseActivity {

    private ListView listView;//模块图标
    private List<FunctionModuleVo> mFunctionModuleList;
    private FunctionModuleUpdateAdapter functionModuleUpdateAdapter;
    private PackageManager packageManager;

    private View view;
    private Context context;

    private ApkInstallReceiver apkInstallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_function_module_update);
        view = getWindow().getDecorView();

        ActivityTitleUtil activityTitleUtil = new ActivityTitleUtil();
        activityTitleUtil.initTitle(view, getResources().getString(R.string.module_manager), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionModuleUpdateActivity.this.finish();
            }
        }, null, null);
        listView = (ListView) view.findViewById(R.id.listView);

        initViews();

        apkInstallReceiver = new ApkInstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(apkInstallReceiver, filter);
       // update();
    }

    private void initViews(){
        packageManager = this.getPackageManager();
        // ListView数据
        mFunctionModuleList = new ArrayList<FunctionModuleVo>();
        List<PackageInfo> packageInfos = PackageUtil.getAllApps(this, ConfigVo.MODEL_APP_PACKAGE_PREFIX);//获取所有已安装的模块APP，模块包名以com.angma为前缀
        for (int i = 0; i < packageInfos.size(); i++) {//填充包名，启动图标，应用名称等数据
            String appName = (String) packageManager.getApplicationLabel(packageInfos.get(i).applicationInfo);
            Drawable icon = packageManager.getApplicationIcon(packageInfos.get(i).applicationInfo);
            String versionName = packageInfos.get(i).versionName;

            FunctionModuleVo functionModule = new FunctionModuleVo();
            functionModule.setName(appName);
            functionModule.setIcon(icon);
            functionModule.setInstallVersion(versionName);
            functionModule.setPackageName(packageInfos.get(i).packageName);
            ActivityInfo[] activityInfos = packageInfos.get(i).activities;
            if (activityInfos != null && activityInfos.length > 0) {
                functionModule.setMainActivity(activityInfos[0].name);
            } else {
                functionModule.setMainActivity(null);
            }
            mFunctionModuleList.add(functionModule);
        }

        List<FunctionModuleUpdateVo> functionModuleUpdateVos = getFromServer();
        //比对本地安装模块与服务器模块的差异
        for (int i = 0; i < functionModuleUpdateVos.size(); i++) {
            for(int j = 0; j < mFunctionModuleList.size(); j++){
                if(functionModuleUpdateVos.get(i).getName().equals(mFunctionModuleList.get(j).getName())){
                    functionModuleUpdateVos.get(i).setInstallVersion(mFunctionModuleList.get(j).getInstallVersion());
                    functionModuleUpdateVos.get(i).setIcon(mFunctionModuleList.get(j).getIcon());
                    functionModuleUpdateVos.get(i).setPackageName(mFunctionModuleList.get(j).getPackageName());
                }
            }
        }
        functionModuleUpdateAdapter = new FunctionModuleUpdateAdapter(context);
        listView.setAdapter(functionModuleUpdateAdapter);
        functionModuleUpdateAdapter.refreshAdapter(functionModuleUpdateVos);
    }

    private List<FunctionModuleUpdateVo> getFromServer(){
        List<FunctionModuleUpdateVo> functionModuleUpdateVos  = new ArrayList<FunctionModuleUpdateVo>();

        FunctionModuleUpdateVo functionModuleUpdateVo = new FunctionModuleUpdateVo();
        functionModuleUpdateVo.setServerVersion("1.1");
        functionModuleUpdateVo.setName("BASE测试模块");
        functionModuleUpdateVo.setPackageName("com.angma.mainapp");
        functionModuleUpdateVo.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        functionModuleUpdateVos.add(functionModuleUpdateVo);

        FunctionModuleUpdateVo functionModuleUpdateVo1 = new FunctionModuleUpdateVo();
        functionModuleUpdateVo1.setServerVersion("1.0");
        functionModuleUpdateVo1.setName("main");
        functionModuleUpdateVo1.setPackageName("com.angma.test");
        functionModuleUpdateVo.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        functionModuleUpdateVos.add(functionModuleUpdateVo1);

        return functionModuleUpdateVos;
    }

    /***
     * 获取更新描述文件，异步返回VersionUpgraderVo
     */
    private void update(){
        mLoadingDialog = DialogUtil.showWaitDialog(context, getResources().getString(R.string.about_update));
        VersionUpgraderUtil.updateAll(this, mainApplication.httpUtils, ConfigVo.VERSION_UPGRADER_POST_URL, new VersionUpgraderCallback() {

            @Override
            public void onFailure(int statusCode, String responseString) {
                DialogUtil.cancelWaitDialog(mLoadingDialog);
                ToastUtil.showToastInThread(context, responseString);
            }

            @Override
            public void onSuccess(int statusCode, final VersionUpgraderVo versionUpgraderVo) {
                FunctionModuleUpdateActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.cancelWaitDialog(mLoadingDialog);
                        if (versionUpgraderVo == null) {
                            ToastUtil.showToastInThread(context, getResources().getString(R.string.network_error));
                            return;
                        }

                        if (VersionUpgraderVo.CODE_NEW_VERSION.equals(versionUpgraderVo.getUpdateCode())) {
                            DialogUtil.showDialog(context, getResources().getString(R.string.update_title) + "\n" + versionUpgraderVo.getUpdateMessage(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //download(versionUpgraderVo);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else if (VersionUpgraderVo.CODE_NO_NEED_UPDATE.equals(versionUpgraderVo.getUpdateCode())) {
                            ToastUtil.showToastInThread(context, getResources().getString(R.string.update_no_need_update));
                        } else {
                            ToastUtil.showToastInThread(context, getResources().getString(R.string.update_title_error) + versionUpgraderVo.getUpdateCode());
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(apkInstallReceiver);
    }

    class ApkInstallReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("-----unInstall apk-------");
            initViews();
        }
    }
}
