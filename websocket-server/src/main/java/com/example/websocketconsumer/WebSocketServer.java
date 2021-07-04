package com.example.websocketconsumer;

import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@Slf4j
public class WebSocketServer extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        log.info("Receive message {}", message.getPayload());
        var msg = new JsonParser().parse(message.getPayload()).getAsJsonObject();
        String resp;
        if ("".equals(msg.get("message").getAsString())) {
           resp = ResponseMessage.buildFailRespMsg();
        } else {
            resp = ResponseMessage.buildSuccessRespMsg(message.getPayload());
        }
        log.info("Response msg {}", resp);
        session.sendMessage(new TextMessage(resp));
    }

}
