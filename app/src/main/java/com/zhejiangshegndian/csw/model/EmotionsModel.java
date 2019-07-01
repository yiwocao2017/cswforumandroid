package com.zhejiangshegndian.csw.model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/5/22.
 */

public class EmotionsModel implements Serializable {

    private String chs; // 简体中文
    private String cht; // 繁体中文
    private String gif;
    private String png; // 图片名
    private String type;

    public String getChs() {
        return chs;
    }

    public void setChs(String chs) {
        this.chs = chs;
    }

    public String getCht() {
        return cht;
    }

    public void setCht(String cht) {
        this.cht = cht;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Emotions{" +
                "chs='" + chs + '\'' +
                ", png='" + png + '\'' +
                '}';
    }
}
