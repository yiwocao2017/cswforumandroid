package com.zhejiangshegndian.csw.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell1 on 2016/12/15.
 */

public class GoodsModel implements Serializable {


    /**
     * code : CP201704052113595563
     * category : FL201700000000000001
     * type : FL201700000000000003
     * name : 商品001
     * slogan : *广告语:*广告语:*广告语:*广告语:*广告语:*广告语:
     * advPic : OSS_1491398022176_318_176.png
     * pic : OSS_1491398033529_750_8876.jpg
     * description : *商品详述:*商品详述:*商品详述:*商品详述:*商品详述:*商品详述:*商品详述:
     * originalPrice : 0
     * price1 : 100
     * price2 : 2000
     * price3 : 3000
     * location : 1
     * orderNo : 1
     * status : 3
     * updater : admin
     * updateDatetime : Apr 5, 2017 9:16:06 PM
     * boughtCount : 0
     * companyCode : U2017040520504605437
     * systemCode : CD-CZH000001
     * productSpecs : [{"code":"PS201704052114303313","productCode":"CP201704052113595563","dkey":"颜色","dvalue":"红色","orderNo":1,"companyCode":"U2017040520504605437","systemCode":"CD-CZH000001"},{"code":"PS201704052114423194","productCode":"CP201704052113595563","dkey":"尺寸","dvalue":"35码","orderNo":2,"companyCode":"U2017040520504605437","systemCode":"CD-CZH000001"}]
     */

    private String code;
    private String category;
    private String type;
    private String name;
    private String slogan;
    private String advPic;
    private String pic;
    private String description;
    private int originalPrice;
    private double price1;
    private int price2;
    private int price3;
    private String location;
    private int orderNo;
    private String status;
    private String updater;
    private String updateDatetime;
    private int boughtCount;
    private String companyCode;
    private String systemCode;
    /**
     * code : PS201704052114303313
     * productCode : CP201704052113595563
     * dkey : 颜色
     * dvalue : 红色
     * orderNo : 1
     * companyCode : U2017040520504605437
     * systemCode : CD-CZH000001
     */

    private List<ProductSpecsBean> productSpecs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public int getPrice3() {
        return price3;
    }

    public void setPrice3(int price3) {
        this.price3 = price3;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
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

    public int getBoughtCount() {
        return boughtCount;
    }

    public void setBoughtCount(int boughtCount) {
        this.boughtCount = boughtCount;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public List<ProductSpecsBean> getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(List<ProductSpecsBean> productSpecs) {
        this.productSpecs = productSpecs;
    }

    public static class ProductSpecsBean implements Serializable {
        private String code;
        private String productCode;
        private String dkey;
        private String dvalue;
        private int orderNo;
        private String companyCode;
        private String systemCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getDkey() {
            return dkey;
        }

        public void setDkey(String dkey) {
            this.dkey = dkey;
        }

        public String getDvalue() {
            return dvalue;
        }

        public void setDvalue(String dvalue) {
            this.dvalue = dvalue;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }
    }
}
