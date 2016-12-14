package com.cleverm.smartpen.pushtable;

public class RTableZoneVo {
    private long zoneId;
    private String zoneName;
    private String description;

    public RTableZoneVo() {

    }

    public RTableZoneVo(long typeId, String typeName, String description) {
        super();
        this.zoneId = typeId;
        this.zoneName = typeName;
        this.description = description;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}