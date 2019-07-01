package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class ModuleModel {

    /**
     * code : SPK201704911043043461
     * name : 通天塔
     * bplateCode : BMK201704911042439602
     * pic : xiwu_1491014731059.jpg
     * orderNo : 1
     * moderator : U2017033121141854178
     * companyCode : GS2017033013320557598
     * status : 1
     * updater : 余杭
     * updateDatetime : Apr 1, 2017 10:43:04 AM
     * remark :
     * nickname : 柯
     * mobile : 15057527017
     * allCommentNum : 4
     * allLikeNum : 0
     */

    private String code;
    private String name;
    private String bplateCode;
    private String pic = "";
    private String orderNo;
    private String isDefault;
    private String moderator;
    private String companyCode;
    private String status;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String nickname;
    private String mobile;
    private int allCommentNum;
    private int allLikeNum;

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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

    public String getBplateCode() {
        return bplateCode;
    }

    public void setBplateCode(String bplateCode) {
        this.bplateCode = bplateCode;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAllCommentNum() {
        return allCommentNum;
    }

    public void setAllCommentNum(int allCommentNum) {
        this.allCommentNum = allCommentNum;
    }

    public int getAllLikeNum() {
        return allLikeNum;
    }

    public void setAllLikeNum(int allLikeNum) {
        this.allLikeNum = allLikeNum;
    }
}
