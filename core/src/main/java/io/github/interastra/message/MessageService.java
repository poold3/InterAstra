package io.github.interastra.message;

import io.github.interastra.screens.LobbyScreen;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class MessageService {
    public static final String BASE_URL = "ws://localhost:8080";
    public WebSocketClient webSocketClient;
    public WebSocketStompClient stompClient;
    public MessageSessionHandler sessionHandler;

    public MessageService(final LobbyScreen screen) {
        this.webSocketClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(this.webSocketClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.sessionHandler = new MessageSessionHandler(screen);

        //StompHeaders headers = new StompHeaders();
        //headers.add("gameCode", gameCode);
        this.stompClient.connectAsync(BASE_URL + "/ia-ws-connection", this.sessionHandler);
    }

}
