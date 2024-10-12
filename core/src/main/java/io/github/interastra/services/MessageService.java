package io.github.interastra.services;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class MessageService {
    public static final String BASE_URL = "ws://localhost:8080";
    public final String gameCode;
    public WebSocketClient webSocketClient;
    public WebSocketStompClient stompClient;
    public MyStompSessionHandler sessionHandler;

    public MessageService(final String gameCode) {
        this.gameCode = gameCode;
        this.webSocketClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(this.webSocketClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.sessionHandler = new MyStompSessionHandler();

        //StompHeaders headers = new StompHeaders();
        //headers.add("gameCode", gameCode);
        this.stompClient.connectAsync(BASE_URL + "/ia-ws-connection", this.sessionHandler);
    }

}
