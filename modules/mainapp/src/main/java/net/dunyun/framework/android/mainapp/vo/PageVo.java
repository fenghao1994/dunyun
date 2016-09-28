package net.dunyun.framework.android.mainapp.vo;

import java.io.Serializable;

/**
 * <DL>
 * <DD>页码实体.</DD><BR>
 * </DL>
 *
 * @author cqpsoft <Chenzp>
 * @date 2016/4/1
 * 修改记录:
 * 初始化
 * @Copyright 重庆平软科技有限公司 2015
 */
public class PageVo implements Serializable{

    private int numPerPage;
    private int totalRows;
    private int totalPages;
    private int currentPage;
    private boolean permitId;
    private String result;

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isPermitId() {
        return permitId;
    }

    public void setPermitId(boolean permitId) {
        this.permitId = permitId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
