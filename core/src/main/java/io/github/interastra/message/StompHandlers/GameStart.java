package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameStartMessage;
import io.github.interastra.screens.LobbyScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

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
        this.lobbyScreen.gameData = (GameStartMessage) payload;
    }
}
