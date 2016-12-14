package com.cleverm.smartpen.bean;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 3/9/2016.
 * Data:3/9/2016  上午 10:34
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class LuckyPrizeInfo implements Serializable {

    private String smallPic;
    private int id;
    private String bigPic;
    private String description;
    private String name;
    private int type;

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LuckyPrizeList{" +
                "smallPic='" + smallPic + '\'' +
                ", id=" + id +
                ", bigPic='" + bigPic + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
