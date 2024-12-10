package io.github.interastra.rest.callbacks;

import com.google.gson.Gson;
import io.github.interastra.models.Planet;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.GameScreen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

public class AddBaseCallback implements Callback {
    private final GameScreen screen;

    public AddBaseCallback(final GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        System.out.println(Arrays.toString(e.getStackTrace()));
        screen.notificationTable.setMessage(RestService.SERVER_ERROR);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        screen.notificationTable.clearNotification();
        if (response.isSuccessful()) {
            Gson gson = new Gson();
            io.github.interastra.rest.responses.Response addBaseResponse = gson.fromJson(response.body().string(), io.github.interastra.rest.responses.Response.class);
            if (addBaseResponse.success) {
                Planet.BASE_PRICE.purchase(screen);
                screen.myPlayer.bases += 1;
            } else {
                screen.badSound.play(0.5f);
                screen.notificationTable.setMessage(addBaseResponse.message);
            }
        } else {
            screen.notificationTable.setMessage(RestService.SERVER_ERROR);
        }
        response.close();
    }
}
