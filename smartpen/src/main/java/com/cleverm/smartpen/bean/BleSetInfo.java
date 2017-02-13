package com.cleverm.smartpen.bean;

/**
 * Created by Randy on 2016/12/15.
 */

public class BleSetInfo extends BaseInfoBean {
    private String dongleadd;
    private String watchadd;
    private String mac_address;

    public String getDongleadd() {
        return dongleadd;
    }

    public void setDongleadd(String dongleadd) {
        this.dongleadd = dongleadd;
    }

    public String getWatchadd() {
        return watchadd;
    }

    public void setWatchadd(String watchadd) {
        this.watchadd = watchadd;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
}
