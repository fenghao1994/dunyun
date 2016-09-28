package net.dunyun.framework.android.mainapp.util;

import com.google.gson.reflect.TypeToken;
import com.psoft.framework.android.base.utils.JsonUtil;

import net.dunyun.framework.android.mainapp.vo.LockVo;
import net.dunyun.framework.android.mainapp.vo.PageVo;

import org.json.JSONObject;

import java.util.List;

/**
 * <DL>
 * <DD>类、接口说明.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/21
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class PageUtil {

    public static PageVo getPageVo(String result){
        PageVo pageVo = new PageVo();;
        try{
            JSONObject jsonObject = new JSONObject(result);
            pageVo.setNumPerPage(jsonObject.getInt("numPerPage"));
            pageVo.setTotalRows(jsonObject.getInt("totalRows"));
            pageVo.setTotalPages(jsonObject.getInt("totalPages"));
            pageVo.setCurrentPage(jsonObject.getInt("currentPage"));
            pageVo.setPermitId(jsonObject.getBoolean("permitId"));
            pageVo.setResult(jsonObject.getString("result"));
        }catch (Exception e){
            e.printStackTrace();
            pageVo = null;
        }
        return pageVo;
    }
}
