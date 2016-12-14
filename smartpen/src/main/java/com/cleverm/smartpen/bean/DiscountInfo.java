package com.cleverm.smartpen.bean;

import com.cleverm.smartpen.util.ServiceUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiong,An android project Engineer,on 2016/2/21.
 * Data:2016-02-21  12:24
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DiscountInfo extends BaseInfoBean{

    private int type;
    private int orgId;
    private int orderSeq;
    private String pictruePath;

    private String qiniuPath;

    private String rollMainId;

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }

    private String title;
    private String descriptionText;



    private List<DiscountAdInfo> advertisementList;
    private List<DiscountRollInfo> rollDetailList;

    private Long startTime;
    private Long endTime;

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<DiscountAdInfo> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<DiscountAdInfo> advertisementList) {
        this.advertisementList = advertisementList;
    }

    public List<DiscountRollInfo> getRollDetailList() {
        return rollDetailList;
    }

    public void setRollDetailList(List<DiscountRollInfo> rollDetailList) {
        this.rollDetailList = rollDetailList;
    }

    public String getPictruePath() {
        return pictruePath;
    }

    public void setPictruePath(String pictruePath) {
        this.pictruePath = pictruePath;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getRollMainId() {
        return rollMainId;
    }

    public void setRollMainId(String rollMainId) {
        this.rollMainId = rollMainId;
    }
}
