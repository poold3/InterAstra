package io.github.interastra.message;

import io.github.interastra.screens.LobbyScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;


public class MessageSessionHandler extends StompSessionHandlerAdapter {
    private final LobbyScreen lobbyScreen;

    public MessageSessionHandler(final LobbyScreen lobbyScreen) {
        super();
        this.lobbyScreen = lobbyScreen;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("Stomp Error: " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.err.println("Transport Error: " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void afterConnected(@NotNull StompSession session, @NotNull StompHeaders connectedHeaders) {
        this.lobbyScreen.setMessageSession(session);
        this.lobbyScreen.subscribeToLobbyTopics();
    }
}
