package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.TransferMessage;
import io.github.interastra.models.Price;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.InterAstraLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.logging.Level;

public class Transfer implements StompFrameHandler {
    public GameScreen gameScreen;

    public Transfer(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }


    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TransferMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            TransferMessage message = (TransferMessage) payload;

            if (gameScreen.myPlayer.name.equals(message.from())) {
                gameScreen.moneySound.play();
                Price transferAmount = new Price(message.amount());
                transferAmount.purchase(gameScreen);
                gameScreen.notificationTable.setMessage(String.format("You sent %s %s.", message.to(), transferAmount));
            } else if (gameScreen.myPlayer.name.equals(message.to())) {
                gameScreen.moneySound.play();
                Price transferAmount = new Price(message.amount());
                transferAmount.sell(gameScreen.myPlayer);
                gameScreen.notificationTable.setMessage(String.format("%s sent you %s.", message.from(), transferAmount));
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
