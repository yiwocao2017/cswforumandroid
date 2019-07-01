package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/4/19.
 */

public class AwardUserModel {


    /**
     * code : JL20170410402412432
     * type : 3
     * postCode : TZ20170410112062331
     * talker : U2017041414352181917
     * talkDatetime : Apr 14, 2017 2:41:24 PM
     * remark : 10000
     * nickname : 52181917
     * postTitle : Tuuu
     * postContent : Tukjhjjjd[黑线][挖鼻]@yeueuus@urujdjsj@usijdjd @udhdhsj hshjsjsjsjsjsjsjsjsjsj
     */

    private String code;
    private String type;
    private String postCode;
    private String talker;
    private String talkDatetime;
    private String remark;
    private String nickname;
    private String photo = "";
    private String postTitle;
    private String postContent;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getTalkDatetime() {
        return talkDatetime;
    }

    public void setTalkDatetime(String talkDatetime) {
        this.talkDatetime = talkDatetime;
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

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
