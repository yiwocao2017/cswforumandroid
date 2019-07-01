package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/4/19.
 */

public class PraiseUserModel {


    /**
     * code : JL20170410412515685
     * type : 1
     * postCode : TZ20170410411262283
     * talker : U2017041411203368918
     * talkDatetime : Apr 14, 2017 12:51:56 PM
     * remark : 点赞
     * nickname : 03368918
     * postTitle : dsfa
     * postContent : fdsfasf
     * photo : 4d5cbee2-fe8b-4d2b-a7c0-1d6656bb3edf.png
     */

    private String code;
    private String type;
    private String postCode;
    private String talker;
    private String talkDatetime;
    private String remark;
    private String nickname;
    private String postTitle;
    private String postContent;
    private String photo = "";

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
