package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameEndMessage;
import io.github.interastra.screens.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class GameEnd implements StompFrameHandler {
    public GameScreen gameScreen;

    public GameEnd(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameEndMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GameEndMessage message = (GameEndMessage) payload;
        this.gameScreen.endGame = true;
    }
}
