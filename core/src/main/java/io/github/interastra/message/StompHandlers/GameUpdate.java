package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameUpdateMessage;
import io.github.interastra.screens.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class GameUpdate implements StompFrameHandler {
    public GameScreen gameScreen;

    public GameUpdate(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameUpdateMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GameUpdateMessage message = (GameUpdateMessage) payload;
    }
}
