package io.github.interastra.message;

import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.InterAstraLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.ArrayList;
import java.util.logging.Level;


public class MessageSessionHandler extends StompSessionHandlerAdapter {
    private final LobbyScreen lobbyScreen;

    public MessageSessionHandler(final LobbyScreen lobbyScreen) {
        super();
        this.lobbyScreen = lobbyScreen;
    }

    @Override
    public void afterConnected(@NotNull StompSession session, @NotNull StompHeaders connectedHeaders) {
        try {
            this.lobbyScreen.setMessageSession(session);
            this.lobbyScreen.subscribeToLobbyTopics();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
