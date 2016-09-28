package net.dunyun.framework.android.mainapp.util;

import net.dunyun.framework.android.mainapp.db.LockDbUtil;
import net.dunyun.framework.android.mainapp.db.NearKeyDb;
import net.dunyun.framework.android.mainapp.db.NearKeyDbUtil;
import net.dunyun.framework.android.mainapp.vo.KeyVo;
import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.NearKeyVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzp
 * @date 2016/5/25
 * @Copyright:重庆平软科技有限公司
 */
public class NearKeyUtil {

    public static void clear(String ower){
        List<NearKeyDb> nearKeyDbs =  NearKeyDbUtil.list(ower);
        List<LockVo> lockVos = LockDbUtil.query(ower);
        List<KeyVo> keyVos = new ArrayList<KeyVo>();

        if(lockVos != null && lockVos.size()>0){
            for (int i = 0; i < lockVos.size(); i++) {
                KeyVo keyVo = BluetoothUtil.getKeyVo(lockVos.get(i).getLockKeys(), ower);
                if(keyVo != null){
                    keyVos.add(keyVo);
                }
            }
        }

        if(nearKeyDbs != null && nearKeyDbs.size()>0){
            for (int i = 0; i < nearKeyDbs.size(); i++) {
                boolean isfind = false;
                if(keyVos != null && keyVos.size()>0){
                    for (int j = 0; j < keyVos.size(); j++) {
                        if(nearKeyDbs.get(i).macCode.equals(keyVos.get(j))){
                            isfind = true;
                            break;
                        }
                    }
                }else {
                    NearKeyDbUtil.clear(ower);
                    break;
                }

                if(!isfind){
                    nearKeyDbs.get(i).delete();
                }
            }
        }


    }
}
