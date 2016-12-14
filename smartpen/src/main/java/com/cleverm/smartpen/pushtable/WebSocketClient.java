package com.cleverm.smartpen.pushtable;

public interface WebSocketClient {

    boolean send(String data);

    void connect();

    boolean isDisconnected();

    void close();
}
