package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/4/24.
 */

public class AttentionModel {


    /**
     * userId : U2017041016353002169
     * loginName : 13868074590
     * nickname : 53002169
     * kind : f1
     * level : 2
     * mobile : 13868074590
     * status : 0
     * updater : U2017041016353002169
     * updateDatetime : Apr 10, 2017 4:35:30 PM
     * photo : IOS_1491901980372204_1280_950.jpg
     */

    private String userId;
    private String loginName;
    private String nickname;
    private String kind;
    private String level;
    private String mobile;
    private String status;
    private String updater;
    private String updateDatetime;
    private String photo = "";
    private int unRead = 0;

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
