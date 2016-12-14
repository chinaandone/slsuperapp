package com.cleverm.smartpen.util.online;

import java.io.Serializable;

/**
 * Created by xiong,An android project Engineer,on 24/8/2016.
 * Data:24/8/2016  下午 02:34
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class JsonStaus implements Serializable{

    private String orgId;
    private String clientId;
    private String tableId;
    private String boardNo;
    private String productPower;
    private String productGlobalIp;
    private String productLocalIp;
    private String productStatusTypeId;
    private String softVersionCode;
    private String softVersionName;
    private String chargeStatus;
    private String ramStatus;
    private String romStatus;
    private String volume;
    private String wifiStatus;
    private String qiniuPath;
    private String macAddress;
    private String padMode;
    private String OSVersion;
    private String timestamp;
    private String detail;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getProductPower() {
        return productPower;
    }

    public void setProductPower(String productPower) {
        this.productPower = productPower;
    }

    public String getProductGlobalIp() {
        return productGlobalIp;
    }

    public void setProductGlobalIp(String productGlobalIp) {
        this.productGlobalIp = productGlobalIp;
    }

    public String getProductLocalIp() {
        return productLocalIp;
    }

    public void setProductLocalIp(String productLocalIp) {
        this.productLocalIp = productLocalIp;
    }

    public String getProductStatusTypeId() {
        return productStatusTypeId;
    }

    public void setProductStatusTypeId(String productStatusTypeId) {
        this.productStatusTypeId = productStatusTypeId;
    }

    public String getSoftVersionCode() {
        return softVersionCode;
    }

    public void setSoftVersionCode(String softVersionCode) {
        this.softVersionCode = softVersionCode;
    }

    public String getSoftVersionName() {
        return softVersionName;
    }

    public void setSoftVersionName(String softVersionName) {
        this.softVersionName = softVersionName;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getRamStatus() {
        return ramStatus;
    }

    public void setRamStatus(String ramStatus) {
        this.ramStatus = ramStatus;
    }

    public String getRomStatus() {
        return romStatus;
    }

    public void setRomStatus(String romStatus) {
        this.romStatus = romStatus;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(String wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public String getQiniuPath() {
        return qiniuPath;
    }

    public void setQiniuPath(String qiniuPath) {
        this.qiniuPath = qiniuPath;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPadMode() {
        return padMode;
    }

    public void setPadMode(String padMode) {
        this.padMode = padMode;
    }

    public String getOSVersion() {
        return OSVersion;
    }

    public void setOSVersion(String OSVersion) {
        this.OSVersion = OSVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "{" +
                "orgId='" + orgId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", boardNo='" + boardNo + '\'' +
                ", productPower='" + productPower + '\'' +
                ", productGlobalIp='" + productGlobalIp + '\'' +
                ", productLocalIp='" + productLocalIp + '\'' +
                ", productStatusTypeId='" + productStatusTypeId + '\'' +
                ", softVersionCode='" + softVersionCode + '\'' +
                ", softVersionName='" + softVersionName + '\'' +
                ", chargeStatus='" + chargeStatus + '\'' +
                ", ramStatus='" + ramStatus + '\'' +
                ", romStatus='" + romStatus + '\'' +
                ", volume='" + volume + '\'' +
                ", wifiStatus='" + wifiStatus + '\'' +
                ", qiniuPath='" + qiniuPath + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", padMode='" + padMode + '\'' +
                ", OSVersion='" + OSVersion + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
