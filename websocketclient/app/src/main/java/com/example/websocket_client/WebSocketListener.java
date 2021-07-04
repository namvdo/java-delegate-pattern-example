package com.example.websocket_client;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import okhttp3.Response;
import okhttp3.WebSocket;

public class WebSocketListener extends okhttp3.WebSocketListener {
    private final String TAG = WebSocketListener.class.getName();
    private Consumer<String> msgConsumer;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        Log.d(TAG, "Receive msg from the server " + text);
        consumeMessage(text);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMsgConsumer(Consumer<String> msgConsumer) {
        this.msgConsumer = msgConsumer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void consumeMessage(String msg) {
        if (this.msgConsumer != null)  {
            this.msgConsumer.accept(msg);
        } else {
            throw new IllegalStateException("No handler");
        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        Log.d(TAG, "Open web socket");
        super.onOpen(webSocket, response);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        Log.d(TAG, Objects.requireNonNull(t.getMessage()));
        try {
            throw t;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.onFailure(webSocket, t, response);
    }

}
