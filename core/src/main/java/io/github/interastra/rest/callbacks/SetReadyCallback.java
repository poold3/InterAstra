package io.github.interastra.rest.callbacks;

import com.google.gson.Gson;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.LobbyScreen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

public class SetReadyCallback implements Callback {
    private final LobbyScreen screen;

    public SetReadyCallback(final LobbyScreen screen) {
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
            io.github.interastra.rest.responses.Response setReadyResponse = gson.fromJson(response.body().string(), io.github.interastra.rest.responses.Response.class);
            if (!setReadyResponse.success) {
                screen.notificationTable.setMessage(setReadyResponse.message);
            }
        } else {
            screen.notificationTable.setMessage(RestService.SERVER_ERROR);
        }
        response.close();
    }
}
