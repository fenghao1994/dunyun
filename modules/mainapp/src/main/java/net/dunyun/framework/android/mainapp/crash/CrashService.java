package net.dunyun.framework.android.mainapp.crash;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/***
 *
 */
public class CrashService extends Service{

	private Context context;
	@Override
	public void onCreate() {
		context = this;

	}

	@Override
	public void onStart(final Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
//		Common.systemPrint("----------onStart------------");
		new Thread(){
			public void run() {
				
				try{
					String pn = intent.getExtras().getString("pn");
					String software_verson = intent.getExtras().getString("software_verson");
					String fingerprint = intent.getExtras().getString("fingerprint");
					String hardware = intent.getExtras().getString("hardware");
					String product = intent.getExtras().getString("product");
					String model = intent.getExtras().getString("model");
					String cpu_abi = intent.getExtras().getString("cpu_abi");
					String serial = intent.getExtras().getString("serial");
					String manufacturer = intent.getExtras().getString("manufacturer");
					String brand = intent.getExtras().getString("brand");
					String exception_content = intent.getExtras().getString("exception_content");
//					 (new WebService(context)).AddException(pn, software_verson,
//							 fingerprint, hardware, product, model, cpu_abi, serial,
//							 manufacturer, brand, exception_content,BaseActivity.SERVICE_TICKETS);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {

		
		return null;
	}

}
