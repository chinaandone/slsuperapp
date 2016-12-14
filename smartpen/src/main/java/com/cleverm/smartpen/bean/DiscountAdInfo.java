package com.cleverm.smartpen.bean;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 2016/2/21.
 * Data:2016-02-21  13:59
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DiscountAdInfo extends BaseInfoBean {


    private String title;
    private String description;
    private String pictruePath;

    private String qiniuPath;

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictruePath() {
        return pictruePath;
    }

    public void setPictruePath(String pictruePath) {
        this.pictruePath = pictruePath;
    }
}
