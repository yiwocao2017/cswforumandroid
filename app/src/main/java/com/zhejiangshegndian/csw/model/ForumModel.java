package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/5/4.
 */

public class ForumModel {


    /**
     * code : BMK201704971129109334
     * name : 同城活动
     * orderNo : 0
     * companyCode : GS2017040711291011156
     * status : 1
     * updater : admin
     * updateDatetime : Apr 7, 2017 11:29:10 AM
     */

    private String code;
    private String name;
    private String orderNo;
    private String companyCode;
    private String status;
    private String updater;
    private String updateDatetime;
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
