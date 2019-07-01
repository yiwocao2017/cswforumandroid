package com.zhejiangshegndian.csw.model;

import java.io.Serializable;

/**
 * Created by dell1 on 2016/12/16.
 */

public class PersonalModel implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;


    /**
     * userId : U2017011718493035352
     * loginName : 15268501481
     * nickname : 小海哥
     * loginPwdStrength : 2
     * kind : f1
     * level : 0
     * userReferee : U2017010713451027748
     * userRefereeName : 18767101909
     * mobile : 15268501481
     * idKind : 1
     * idNo : 33028119890802331X
     * realName : 郑海清
     * status : 0
     * updater : U2017011718493035352
     * updateDatetime : Jan 17, 2017 6:49:30 PM
     * amount : 0
     * ljAmount : 0
     * userExt : {"userId":"U2017011718493035352","province":"浙江省","city":"杭州市","area":"余杭区","systemCode":"CD-CZH000001","loginName":"15268501481","mobile":"15268501481"}
     * totalFollowNum : 0
     * totalFansNum : 0
     */

    private String userId;
    private String loginName;
    private String nickname;
    private String loginPwdStrength;
    private String kind;
    private String level;
    private String userReferee;
    private String userRefereeName;
    private String mobile;
    private String idKind;
    private String idNo;
    private String realName;
    private String status;
    private String updater;
    private String updateDatetime;
    private String amount;
    private String ljAmount;
    /**
     * userId : U2017011718493035352
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * systemCode : CD-CZH000001
     * loginName : 15268501481
     * mobile : 15268501481
     */

    private UserExtBean userExt;
    private String identityFlag;
    private String tradepwdFlag;
    private String totalFollowNum;
    private String totalFansNum;

    public String getTradepwdFlag() {
        return tradepwdFlag;
    }

    public void setTradepwdFlag(String tradepwdFlag) {
        this.tradepwdFlag = tradepwdFlag;
    }

    public String getIdentityFlag() {
        return identityFlag;
    }

    public void setIdentityFlag(String identityFlag) {
        this.identityFlag = identityFlag;
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

    public String getLoginPwdStrength() {
        return loginPwdStrength;
    }

    public void setLoginPwdStrength(String loginPwdStrength) {
        this.loginPwdStrength = loginPwdStrength;
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

    public String getUserReferee() {
        return userReferee;
    }

    public void setUserReferee(String userReferee) {
        this.userReferee = userReferee;
    }

    public String getUserRefereeName() {
        return userRefereeName;
    }

    public void setUserRefereeName(String userRefereeName) {
        this.userRefereeName = userRefereeName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdKind() {
        return idKind;
    }

    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLjAmount() {
        return ljAmount;
    }

    public void setLjAmount(String ljAmount) {
        this.ljAmount = ljAmount;
    }

    public UserExtBean getUserExt() {
        return userExt;
    }

    public void setUserExt(UserExtBean userExt) {
        this.userExt = userExt;
    }

    public String getTotalFollowNum() {
        return totalFollowNum;
    }

    public void setTotalFollowNum(String totalFollowNum) {
        this.totalFollowNum = totalFollowNum;
    }

    public String getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(String totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    public static class UserExtBean implements Serializable {
        private String userId;
        private String province;
        private String city;
        private String area;
        private String systemCode;
        private String loginName;
        private String mobile;
        private String gender;
        private String birthday;
        private String introduce;
        private String email;
        private String photo = "";

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
