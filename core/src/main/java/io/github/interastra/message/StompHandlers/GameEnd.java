package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameEndMessage;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.InterAstraLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.logging.Level;

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
        try {
            GameEndMessage message = (GameEndMessage) payload;
            for (PlayerMessageModel player : message.players()) {
                System.out.println(player.name());
                System.out.println(player.bases());
                if (player.bases() == this.gameScreen.basesToWin) {
                    this.gameScreen.lobbyScreen.notificationTable.setMessage(player.name() + " won!");
                    break;
                }
            }
            this.gameScreen.endGame = true;
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
