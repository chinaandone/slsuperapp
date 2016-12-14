package com.cleverm.smartpen.bean;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 3/9/2016.
 * Data:3/9/2016  上午 11:16
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LuckyPrizeIdInfo implements Serializable {

    private int id;

    private String smallPic;

    private String bigPic;

    private String description;


    private String name;

    private int prizeGetId;

    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrizeGetId() {
        return prizeGetId;
    }

    public void setPrizeGetId(int prizeGetId) {
        this.prizeGetId = prizeGetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
