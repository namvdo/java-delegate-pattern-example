package com.example.websocketconsumer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseMessage {
    enum RESPONSE_STATUS {
        OK,
        ERROR
    }

    public static String buildSuccessRespMsg(String message) {
        var msg = new JsonParser().parse(message).getAsJsonObject();
        var username = msg.get("username").getAsString();
        var content = msg.get("message").getAsString();
        var timeSendMsg = LocalDateTime.parse(msg.get("time").getAsString());
        var resp = new JsonObject();
        var result = new JsonObject();
        result.addProperty("user", username);
        result.addProperty("time", timeSendMsg.format(DateTimeFormatter.ofPattern("MM-dd-yyyy, hh:mm:ss")));
        result.addProperty("message", content);
        resp.addProperty("status", String.valueOf(RESPONSE_STATUS.OK));
        resp.addProperty("result", result.toString());
        return resp.toString();
    }

    public static String buildFailRespMsg() {
        var resp = new JsonObject();
        var result = new JsonObject();
        resp.addProperty("status", String.valueOf(RESPONSE_STATUS.ERROR));
        result.addProperty("message", "You have submitted an empty text, try again!");
        resp.addProperty("result", result.toString());
        return resp.toString();
    }
}
