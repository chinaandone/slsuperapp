package com.cleverm.smartpen.net;

public class RestaurantVo {

    private Long clientID;
    private Long orgID;
    private String orgName;

    public RestaurantVo(Long clientID, Long orgID, String orgName) {
        this.clientID = clientID;
        this.orgID = orgID;
        this.orgName = orgName;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
