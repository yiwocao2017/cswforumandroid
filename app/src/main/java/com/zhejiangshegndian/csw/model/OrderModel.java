package com.zhejiangshegndian.csw.model;

import java.util.List;

/**
 * Created by dell1 on 2016/12/17.
 */

public class OrderModel {


    /**
     * code : DD201702091615365268
     * type : 1
     * receiver : 雷黔
     * reMobile : 18984955240
     * reAddress : 浙江省 杭州市 余杭区 梦想小镇啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦流量了
     * receiptType :
     * receiptTitle :
     * applyUser : U2017011704412811088
     * applyNote :
     * applyDatetime : Feb 9, 2017 4:15:36 PM
     * amount1 : 10000
     * amount2 : 10000
     * amount3 : 0
     * payAmount1 : 10000
     * payAmount2 : 10000
     * payAmount3 : 0
     * yunfei : 0
     * payDatetime : Feb 9, 2017 4:15:38 PM
     * status : 4
     * updater : U2017011704412811088
     * updateDatetime : Feb 9, 2017 4:16:49 PM
     * remark : 确认收货
     * logisticsCode : 1881818
     * logisticsCompany : 随便通
     * deliverer : U2017011704381767538
     * deliveryDatetime : Feb 9, 2017 4:16:06 PM
     * pdf :
     * promptTimes : 0
     * companyCode : U2017011704381767538
     * systemCode : CD-CZH000001
     * mobile : 18984955240
     * productOrderList : [{"code":"CD201702091615365282","orderCode":"DD201702091615365268","productCode":"CP201701170138164781","quantity":1,"price1":10000,"price2":10000,"price3":0,"productName":"安卓商品","advPic":"ANDROID_1484588258621_1080_1920.jpg","isComment":"0"}]
     */

    private String code;
    private String type;
    private String receiver;
    private String reMobile;
    private String reAddress;
    private String receiptType;
    private String receiptTitle;
    private String applyUser;
    private String applyNote;
    private String applyDatetime;
    private double amount1;
    private double amount2;
    private double amount3;
    private double payAmount1;
    private double payAmount2;
    private double payAmount3;
    private int yunfei;
    private String payDatetime;
    private String status;
    private String updater;
    private String updateDatetime;
    private String remark;
    private String logisticsCode = "";
    private String logisticsCompany = "";
    private String deliverer;
    private String deliveryDatetime;
    private String pdf;
    private int promptTimes;
    private String companyCode;
    private String systemCode;
    private String mobile;
    /**
     * code : CD201702091615365282
     * orderCode : DD201702091615365268
     * productCode : CP201701170138164781
     * quantity : 1
     * price1 : 10000
     * price2 : 10000
     * price3 : 0
     * productName : 安卓商品
     * advPic : ANDROID_1484588258621_1080_1920.jpg
     * isComment : 0
     */

    private List<ProductOrderListBean> productOrderList;

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReMobile() {
        return reMobile;
    }

    public void setReMobile(String reMobile) {
        this.reMobile = reMobile;
    }

    public String getReAddress() {
        return reAddress;
    }

    public void setReAddress(String reAddress) {
        this.reAddress = reAddress;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public double getAmount1() {
        return amount1;
    }

    public void setAmount1(double amount1) {
        this.amount1 = amount1;
    }

    public double getAmount2() {
        return amount2;
    }

    public void setAmount2(double amount2) {
        this.amount2 = amount2;
    }

    public double getAmount3() {
        return amount3;
    }

    public void setAmount3(double amount3) {
        this.amount3 = amount3;
    }

    public double getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(double payAmount1) {
        this.payAmount1 = payAmount1;
    }

    public double getPayAmount2() {
        return payAmount2;
    }

    public void setPayAmount2(double payAmount2) {
        this.payAmount2 = payAmount2;
    }

    public double getPayAmount3() {
        return payAmount3;
    }

    public void setPayAmount3(double payAmount3) {
        this.payAmount3 = payAmount3;
    }

    public int getYunfei() {
        return yunfei;
    }

    public void setYunfei(int yunfei) {
        this.yunfei = yunfei;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
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

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public String getDeliveryDatetime() {
        return deliveryDatetime;
    }

    public void setDeliveryDatetime(String deliveryDatetime) {
        this.deliveryDatetime = deliveryDatetime;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public int getPromptTimes() {
        return promptTimes;
    }

    public void setPromptTimes(int promptTimes) {
        this.promptTimes = promptTimes;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ProductOrderListBean> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrderListBean> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public static class ProductOrderListBean {
        private String code;
        private String orderCode;
        private String productCode;
        private int quantity;
        private int price1;
        private int price2;
        private int price3;
        private String isComment = "0";
        private ProductBean product;

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getPrice1() {
            return price1;
        }

        public void setPrice1(int price1) {
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



        public String getIsComment() {
            return isComment;
        }

        public void setIsComment(String isComment) {
            this.isComment = isComment;
        }
    }

    public static class ProductBean {
        private String name;
        private String advPic;

        public String getName() {
            return name;
        }

        public void setName(String productName) {
            this.name = productName;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }
    }
}
