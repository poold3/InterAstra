package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.LobbyUpdateMessage;
import io.github.interastra.screens.LobbyScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class LobbyUpdate implements StompFrameHandler {
    public LobbyScreen lobbyScreen;

    public LobbyUpdate(final LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return LobbyUpdateMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LobbyUpdateMessage message = (LobbyUpdateMessage) payload;
        this.lobbyScreen.players.lock();
        this.lobbyScreen.players.setData(message.players());
        this.lobbyScreen.players.unlock();
        this.lobbyScreen.lobbyTable.updatePlayers();
    }
}
