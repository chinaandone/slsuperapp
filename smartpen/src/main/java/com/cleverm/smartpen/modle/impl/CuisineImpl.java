package com.cleverm.smartpen.modle.impl;


import com.cleverm.smartpen.modle.Cuisine;

/**
 * Created by Jimmy on 2015/9/7.
 */
public class CuisineImpl implements Cuisine {

    @SuppressWarnings("unused")
    public static final String TAG = CuisineImpl.class.getSimpleName();

    private long id;
    private String name;
    private String namePinyin;
    private int category;
    private int pungency;
    private int price;
    private int vipPrice;
    private String unit;
    private String description;

    public CuisineImpl() {
    }

    public CuisineImpl(long id, String name, String namePinyin, int category, int pungency, int price, int vipPrice,
                       String unit, String description) {
        this.id = id;
        this.name = name;
        this.namePinyin = namePinyin;
        this.category = category;
        this.pungency = pungency;
        this.price = price;
        this.vipPrice = vipPrice;
        this.unit = unit;
        this.description = description;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    @Override
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public int getPungency() {
        return pungency;
    }

    public void setPungency(int pungency) {
        this.pungency = pungency;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(int vipPrice) {
        this.vipPrice = vipPrice;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}