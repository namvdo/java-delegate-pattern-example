package com.example.websocket_client;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;

import okhttp3.WebSocket;

public class WebSocketConsumer extends AppCompatActivity {
    private final String username = "namvdo";
    private final String TAG = WebSocketConsumer.class.getName();
    private WebSocket websocket;
    private Button sendBtn;
    private TextView editableText;
    private TextView serverResponseView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverResponseView = findViewById(R.id.server_response_view);
        sendBtn = findViewById(R.id.send);
        editableText = findViewById(R.id.textarea);
        sendBtn = findViewById(R.id.send);
        sendBtn.setOnClickListener((View v) -> sendMessage());
        WebSocketListener webSocketListener = new WebSocketListener();
        webSocketListener.setMsgConsumer(this::consumeMessage);
        webSocketListener.setMsgConsumer(msg -> consumeMessage(msg));
        String url = "http://10.0.2.2:8081/ws";
        this.websocket = new WebSocketClient(url, webSocketListener).getWebSocket();
    }

    public void consumeMessage(String msg) {
        Log.d(TAG, "Consume message sent from the server: " + msg);
        JsonObject resp = JsonParser.parseString(msg).getAsJsonObject();
        StringBuilder displayedMsg = new StringBuilder();
        String textMsgFromServer = JsonParser.parseString(resp.get("result").getAsString()).getAsJsonObject().get("message").getAsString();
        runOnUiThread(() -> serverResponseView.setTextSize(28));
        if (!"OK".equals(resp.get("status").getAsString())) {
            String text = "<font color='#EE0000'>" + textMsgFromServer + "</font>";
            serverResponseView.append(Html.fromHtml(text));
        } else {
            String time = JsonParser.parseString(resp.get("result").getAsString()).getAsJsonObject().get("time").getAsString();
            displayedMsg.append(username).append(" ");
            displayedMsg.append("(").append(time).append("): ");
            displayedMsg.append(textMsgFromServer);
            String text = "<font color='#382bf1'>" + displayedMsg + "</font>";
            serverResponseView.append(Html.fromHtml(text));
        }
        serverResponseView.append("\n");
        Log.d(TAG, "text: " + serverResponseView.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage() {
        sendBtn.setOnClickListener((View v) -> {
            String text = editableText.getText().toString();
            JsonObject message = new JsonObject();
            message.addProperty("username", username);
            message.addProperty("time", LocalDateTime.now().toString());
            message.addProperty("message", text);
            Log.d(TAG, "Send request message: " + message.toString());
            websocket.send(message.toString());
        });
    }

}
