package com.cleverm.smartpen.pushtable;

/**
 * Created by Martin on 2015/7/25.
 */
public class OrgProfileVo {
    private Long   orgID;
    private String orgName;
    private String profileMd5;

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

    public String getProfileMd5() {
        return profileMd5;
    }

    public void setProfileMd5(String profileMd5) {
        this.profileMd5 = profileMd5;
    }
}
