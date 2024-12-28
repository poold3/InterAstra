package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameStartMessage;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.InterAstraLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.logging.Level;

public class GameStart implements StompFrameHandler {
    public LobbyScreen lobbyScreen;

    public GameStart(final LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameStartMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            this.lobbyScreen.gameData = (GameStartMessage) payload;
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
