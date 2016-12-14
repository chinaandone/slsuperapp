package com.cleverm.smartpen.pushtable;

import android.content.Context;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFactory {

    public static WebSocketClient local(String url, WebSocketListener listener) {
        try {
            return new AndroidAsyncClient(new URI(url), listener);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static WebSocketClient remote(Context context, String url, WebSocketListener listener) {
        try {
            return new AndroidAsyncClient(new URI(url), listener);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
