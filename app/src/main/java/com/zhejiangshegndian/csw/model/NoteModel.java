package com.zhejiangshegndian.csw.model;

import java.util.List;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class NoteModel {


    /**
     * code : TZ2017041020706005
     * title : 干
     * content : 干
     * pic :
     * status : B
     * publisher : U201704121824505394
     * publishDatetime : Apr 12, 2017 7:06:00 PM
     * approver : 余杭城市网
     * approveDatetime : Apr 12, 2017 9:57:09 PM
     * location : A,B,C
     * plateCode : SPK201704971129108042
     * isLock : 0
     * sumComment : 6
     * sumLike : 2
     * sumRead : 20
     * sumReward : 1
     * loginName : 13110992819CSW13110992819
     * nickname : 如梦初醒
     * photo : 8684520a-f337-485a-9525-d8a3f4f7ac47.PNG
     * isDZ : 1
     * isSC : 1
     * likeList : [{"code":"JL20170410409453686","type":"1","postCode":"TZ2017041020706005","talker":"U2017033120533194265","talkDatetime":"Apr 14, 2017 9:45:36 AM","remark":"点赞","nickname":"吴联请","postTitle":"干","postContent":"干","photo":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0"},{"code":"JL20170410210114597","type":"1","postCode":"TZ2017041020706005","talker":"U2017041216571997117","talkDatetime":"Apr 12, 2017 10:11:45 PM","remark":"点赞","nickname":"哈哈，我叫宋小宋","postTitle":"干","postContent":"干","photo":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELMvYY9ia7FuzvtSmlD5YxlAySL1R0vb96A2xvWWs7xbwxaic9yPljpJko2mgPsKzWeibsbib5wpsYoyg/0"}]
     * picarr : ["56df7f6a-b373-4518-9b38-c09625c07448.png"]
     * commentList : [{"code":"PL20170410409441293","content":"dsfafds[偷笑][闭嘴]","parentCode":"PL20170410210125383","status":"B","commer":"U2017033120533194265","commDatetime":"Apr 14, 2017 9:44:12 AM","postCode":"TZ2017041020706005","parentCommer":"U2017041216571997117","nickname":"吴联请","loginName":"18868824532CSW18868824532","photo":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0"},{"code":"PL20170410409432345","content":"sdfaa","parentCode":"TZ2017041020706005","status":"B","commer":"U2017033120533194265","commDatetime":"Apr 14, 2017 9:43:23 AM","postCode":"TZ2017041020706005","nickname":"吴联请","loginName":"18868824532CSW18868824532","photo":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0"},{"code":"PL20170410303520336","content":"ff","parentCode":"PL20170410210125383","status":"B","commer":"U2017033120533194265","commDatetime":"Apr 13, 2017 3:52:03 PM","postCode":"TZ2017041020706005","parentCommer":"U2017041216571997117","nickname":"吴联请","loginName":"18868824532CSW18868824532","photo":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0"},{"code":"PL20170410210125383","content":"啦啦啦","parentCode":"TZ2017041020706005","status":"B","commer":"U2017041216571997117","commDatetime":"Apr 12, 2017 10:12:53 PM","postCode":"TZ2017041020706005","nickname":"哈哈，我叫宋小宋","loginName":"15738777150CSW15738777150","photo":"http://wx.qlogo.cn/mmopen/PiajxSqBRaELMvYY9ia7FuzvtSmlD5YxlAySL1R0vb96A2xvWWs7xbwxaic9yPljpJko2mgPsKzWeibsbib5wpsYoyg/0"},{"code":"PL20170410208211526","content":"fff","parentCode":"TZ2017041020706005","status":"B","commer":"U2017041220204926473","commDatetime":"Apr 12, 2017 8:21:15 PM","postCode":"TZ2017041020706005","nickname":"04926473","loginName":"CSW17777773333"}]
     * plateName : 活动
     */

    private String code;
    private String title;
    private String content;
    private String pic;
    private String status;
    private String publisher;
    private String publishDatetime;
    private String approver;
    private String approveDatetime;
    private String location;
    private String plateCode;
    private String isLock;
    private int sumComment;
    private int sumLike;
    private int sumRead;
    private int sumReward;
    private String loginName;
    private String nickname;
    private String photo = "";
    private String isDZ;
    private String isSC;
    private String plateName;
    /**
     * code : JL20170410409453686
     * type : 1
     * postCode : TZ2017041020706005
     * talker : U2017033120533194265
     * talkDatetime : Apr 14, 2017 9:45:36 AM
     * remark : 点赞
     * nickname : 吴联请
     * postTitle : 干
     * postContent : 干
     * photo : http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0
     */

    private List<LikeListBean> likeList;
    /**
     * code : PL20170410409441293
     * content : dsfafds[偷笑][闭嘴]
     * parentCode : PL20170410210125383
     * status : B
     * commer : U2017033120533194265
     * commDatetime : Apr 14, 2017 9:44:12 AM
     * postCode : TZ2017041020706005
     * parentCommer : U2017041216571997117
     * nickname : 吴联请
     * loginName : 18868824532CSW18868824532
     * photo : http://wx.qlogo.cn/mmopen/ajNVdqHZLLCgmQKCoYiaz04XxcqYVRkFU6fEehlVW4FauvjSV9U4mVRT6LzPBA7yHbqGkbKhW1gq0TZ5CBnbB3w/0
     */

    private List<String> picArr;
    /**
     * "56df7f6a-b373-4518-9b38-c09625c07448.png"
     */

    private List<CommentListBean> commentList;

    public List<String> getPicArr() {
        return picArr;
    }

    public void setPicArr(List<String> picArr) {
        this.picArr = picArr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDatetime() {
        return publishDatetime;
    }

    public void setPublishDatetime(String publishDatetime) {
        this.publishDatetime = publishDatetime;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public int getSumComment() {
        return sumComment;
    }

    public void setSumComment(int sumComment) {
        this.sumComment = sumComment;
    }

    public int getSumLike() {
        return sumLike;
    }

    public void setSumLike(int sumLike) {
        this.sumLike = sumLike;
    }

    public int getSumRead() {
        return sumRead;
    }

    public void setSumRead(int sumRead) {
        this.sumRead = sumRead;
    }

    public int getSumReward() {
        return sumReward;
    }

    public void setSumReward(int sumReward) {
        this.sumReward = sumReward;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIsDZ() {
        return isDZ;
    }

    public void setIsDZ(String isDZ) {
        this.isDZ = isDZ;
    }

    public String getIsSC() {
        return isSC;
    }

    public void setIsSC(String isSC) {
        this.isSC = isSC;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public List<LikeListBean> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeListBean> likeList) {
        this.likeList = likeList;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class LikeListBean {
        private String code;
        private String type;
        private String postCode;
        private String talker;
        private String talkDatetime;
        private String remark;
        private String nickname;
        private String postTitle;
        private String postContent;
        private String photo;

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

    public static class CommentListBean {
        private String code;
        private String content;
        private String parentCode;
        private String status;
        private String commer;
        private String commDatetime;
        private String postCode;
        private String parentCommer;
        private String nickname;
        private String loginName;
        private String photo;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCommer() {
            return commer;
        }

        public void setCommer(String commer) {
            this.commer = commer;
        }

        public String getCommDatetime() {
            return commDatetime;
        }

        public void setCommDatetime(String commDatetime) {
            this.commDatetime = commDatetime;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getParentCommer() {
            return parentCommer;
        }

        public void setParentCommer(String parentCommer) {
            this.parentCommer = parentCommer;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
