package com.zhejiangshegndian.csw.model;

/**
 * Created by LeiQ on 2017/5/15.
 */

public class ConversationModel {

    private String nickname;
    private String conversationId;
    private String photo = "";
    private int unRead = 0;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }
}
