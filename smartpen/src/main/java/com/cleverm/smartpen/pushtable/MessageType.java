package com.cleverm.smartpen.pushtable;

/**
 * (c)2005 Sean Russell
 */
public enum MessageType {
    HELLO,
    REGISTER,
    NOTIFICATION,
    UNREGISTER,
    ACK,
    PING;

    public boolean is(String msgType) {
        return name().equalsIgnoreCase(msgType);
    }
}
