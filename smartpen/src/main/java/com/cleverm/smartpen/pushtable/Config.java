package com.cleverm.smartpen.pushtable;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private String cannotConnectToServerTips = "Can't connet to server";
    private String serverDataParseErrorTips = "Server data parse error";
    private String serverReplyTimeOutTips = "Server reply time out";
    private long serverReplyTimeOut = 10000L;
    private String boardNumber;
    private List<String> tags = new ArrayList<String>(0);
    private Mode mode = Mode.REMOTE;
    private String httpUrl;
    private String websocketUrl;

    private Config() {
    }

    public static Config create(Mode mode, String boardNumber) {
        Config config = new Config();
        config.boardNumber = boardNumber;
        config.mode = mode;
        return config;
    }

    public static Config create(Mode mode, String boardNumber, List<String> tags) {
        Config config = new Config();
        config.boardNumber = boardNumber;
        config.mode = mode;
        config.tags = tags;
        return config;
    }

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setWebsocketUrl(String websocketUrl) {
        this.websocketUrl = websocketUrl;
    }

    public String getCannotConnectToServerTips() {
        return cannotConnectToServerTips;
    }

    public void setCannotConnectToServerTips(String cannotConnectToServerTips) {
        this.cannotConnectToServerTips = cannotConnectToServerTips;
    }

    public String getServerDataParseErrorTips() {
        return serverDataParseErrorTips;
    }

    public void setServerDataParseErrorTips(String serverDataParseErrorTips) {
        this.serverDataParseErrorTips = serverDataParseErrorTips;
    }

    public String getServerReplyTimeOutTips() {
        return serverReplyTimeOutTips;
    }

    public void setServerReplyTimeOutTips(String serverReplyTimeOutTips) {
        this.serverReplyTimeOutTips = serverReplyTimeOutTips;
    }

    public long getServerReplyTimeOut() {
        return serverReplyTimeOut;
    }

    public void setServerReplyTimeOut(long serverReplyTimeOut) {
        this.serverReplyTimeOut = serverReplyTimeOut;
    }

    public String getBoardNumber() {
        return boardNumber;
    }

    public Mode getMode() {
        return mode;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public enum Mode {
        LOCAL,
        REMOTE
    }
}
