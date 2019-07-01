package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/4/21.
 */

public class CommentModel {


    /**
     * code : PL20170512202195885
     * content : 评论😄
     * parentCode : TZ20170410112062331
     * status : B
     * commer : U2017041315320727521
     * commDatetime : May 2, 2017 2:19:58 PM
     * postCode : TZ20170410112062331
     * nickname : Shashashasha
     * loginName : CSW18984955240
     * photo : http://wx.qlogo.cn/mmopen/ajNVdqHZLLAn2icWgmiaghBYico7SgQQdtX1AbyibshgRZC1VN8abFAffd202umwWiajMsA37GfXDrDIR4YHFXBRktxTRf4eiaF3c2IbibTewnniaqw/0
     * post : {"code":"TZ20170410112062331","title":"Tuuu","content":"Tukjhjjjd[黑线][挖鼻]@yeueuus@urujdjsj@usijdjd @udhdhsj hshjsjsjsjsjsjsjsjsjsj","pic":"iOS_1491840411919866_1280_950.jpg||iOS_1491840411920021_1280_950.jpg","status":"E","publisher":"U2017041016353002169","publishDatetime":"Apr 11, 2017 12:06:23 AM","approver":"西湖管理","approveDatetime":"Apr 13, 2017 4:59:15 PM","approveNote":"","location":"A,B","plateCode":"SPK201704911043043461","isLock":"0","sumComment":1,"sumLike":2,"sumRead":0,"sumReward":2,"loginName":"13868074590","nickname":"53002169","photo":"IOS_1491901980372204_1280_950.jpg"}
     * splateName : 通天塔
     */

    private String code;
    private String content;
    private String parentCode;
    private String status;
    private String commer;
    private String commDatetime;
    private String postCode;
    private String nickname;
    private String loginName;
    private String photo;
    /**
     * code : TZ20170410112062331
     * title : Tuuu
     * content : Tukjhjjjd[黑线][挖鼻]@yeueuus@urujdjsj@usijdjd @udhdhsj hshjsjsjsjsjsjsjsjsjsj
     * pic : iOS_1491840411919866_1280_950.jpg||iOS_1491840411920021_1280_950.jpg
     * status : E
     * publisher : U2017041016353002169
     * publishDatetime : Apr 11, 2017 12:06:23 AM
     * approver : 西湖管理
     * approveDatetime : Apr 13, 2017 4:59:15 PM
     * approveNote :
     * location : A,B
     * plateCode : SPK201704911043043461
     * isLock : 0
     * sumComment : 1
     * sumLike : 2
     * sumRead : 0
     * sumReward : 2
     * loginName : 13868074590
     * nickname : 53002169
     * photo : IOS_1491901980372204_1280_950.jpg
     */

    private PostBean post;
    private String splateName;

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

    public PostBean getPost() {
        return post;
    }

    public void setPost(PostBean post) {
        this.post = post;
    }

    public String getSplateName() {
        return splateName;
    }

    public void setSplateName(String splateName) {
        this.splateName = splateName;
    }

    public static class PostBean {
        private String code;
        private String title;
        private String content;
        private String pic;
        private String status;
        private String publisher;
        private String publishDatetime;
        private String approver;
        private String approveDatetime;
        private String approveNote;
        private String location;
        private String plateCode;
        private String isLock;
        private int sumComment;
        private int sumLike;
        private int sumRead;
        private int sumReward;
        private String loginName;
        private String nickname;
        private String photo;

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

        public String getApproveNote() {
            return approveNote;
        }

        public void setApproveNote(String approveNote) {
            this.approveNote = approveNote;
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
    }
}
