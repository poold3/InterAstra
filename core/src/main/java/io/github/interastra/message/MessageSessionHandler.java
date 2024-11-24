package io.github.interastra.message;

import io.github.interastra.screens.LobbyScreen;
import org.jetbrains.annotations.NotNull;
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
    public void afterConnected(@NotNull StompSession session, @NotNull StompHeaders connectedHeaders) {
        this.lobbyScreen.setMessageSession(session);
        this.lobbyScreen.subscribeToLobbyTopics();
    }
}
