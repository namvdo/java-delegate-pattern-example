package com.example.websocket_client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public final class WebSocketClient {
    private final WebSocket webSocket;
    public WebSocketClient(String url, WebSocketListener socketListener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        webSocket = client.newWebSocket(request, socketListener);
    }

    public WebSocket getWebSocket() {
        return this.webSocket;
    }

}
