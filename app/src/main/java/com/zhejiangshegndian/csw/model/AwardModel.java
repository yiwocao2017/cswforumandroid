package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2016/12/30.
 */

public class AwardModel {


    /**
     * code : AJ2017010621334975089
     * userId : U2017010615181802379
     * realName : 田磊
     * accountNumber : A2017010615181821641
     * channelType : 01
     * bizType : -11
     * bizNote : 取现:银行卡号[11]划转金额
     * transAmount : 1000
     * status : 0
     * createDatetime : Jan 6, 2017 9:33:49 PM
     * workDate : 20170106
     * systemCode : CD-CZH000001
     */

    private String code;
    private String userId;
    private String realName;
    private String accountNumber;
    private String channelType;
    private String bizType;
    private String bizNote;
    private double transAmount;
    private String status;
    private String createDatetime;
    private String workDate;
    private String systemCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizNote() {
        return bizNote;
    }

    public void setBizNote(String bizNote) {
        this.bizNote = bizNote;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
