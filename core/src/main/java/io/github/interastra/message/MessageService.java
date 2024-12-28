package io.github.interastra.message;

import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.InterAstraLog;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.logging.Level;

public class MessageService {
    public static String BASE_URL = "ws://localhost:8090";
    public WebSocketClient webSocketClient;
    public WebSocketStompClient stompClient;
    public MessageSessionHandler sessionHandler;

    public MessageService(final LobbyScreen screen) {
        try {
            this.webSocketClient = new StandardWebSocketClient();
            this.stompClient = new WebSocketStompClient(this.webSocketClient);
            this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            this.sessionHandler = new MessageSessionHandler(screen);

            StompHeaders headers = new StompHeaders();
            headers.add("gameCode", screen.gameCode);
            headers.add("name", screen.myName);
            this.stompClient.connectAsync(BASE_URL + "/ia-ws-connection", new WebSocketHttpHeaders(), headers, this.sessionHandler);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
