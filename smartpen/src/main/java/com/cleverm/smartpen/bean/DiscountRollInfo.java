package com.cleverm.smartpen.bean;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 2016/2/21.
 * Data:2016-02-21  14:01
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class DiscountRollInfo extends BaseInfoBean {

    private String pictruePath;

    private String qiniuPath;

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }

    public String getPictruePath() {
        return pictruePath;
    }

    public void setPictruePath(String pictruePath) {
        this.pictruePath = pictruePath;
    }
}
