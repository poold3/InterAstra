package io.github.interastra.services;

import com.google.gson.Gson;
import io.github.interastra.responses.JoinGameResponse;
import io.github.interastra.screens.MainMenuScreen;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ApiService {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String SERVER_ERROR = "We ran into an error. Please try again later.";
    public static final OkHttpClient client = new OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .build();

    public static void shutdownClient() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }

    public static void newGame(final MainMenuScreen screen, final String name) {
        RequestBody requestBody = new FormBody.Builder()
            .add("name", name)
            .build();

        Request request = new Request.Builder()
            .url(BASE_URL + "/create-game")
            .post(requestBody)
            .addHeader("Accept", "application/json")
            .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                screen.notificationTable.setMessage(SERVER_ERROR);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    JoinGameResponse joinGameResponse = gson.fromJson(response.body().string(), JoinGameResponse.class);
                    if (joinGameResponse.success) {
                        screen.joinGameSuccess(joinGameResponse);
                    } else {
                        screen.notificationTable.setMessage(joinGameResponse.message);
                    }
                } else {
                    screen.notificationTable.setMessage(SERVER_ERROR);
                }
                response.close();
            }
        });
    }

    public static void joinGame(final MainMenuScreen screen, final String name, final String gameCode) {
        RequestBody requestBody = new FormBody.Builder()
            .add("name", name)
            .add("gameCode", gameCode)
            .build();

        Request request = new Request.Builder()
            .url(BASE_URL + "/join-game")
            .post(requestBody)
            .addHeader("Accept", "application/json")
            .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                screen.notificationTable.setMessage(SERVER_ERROR);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    JoinGameResponse joinGameResponse = gson.fromJson(response.body().string(), JoinGameResponse.class);
                    if (joinGameResponse.success) {
                        screen.joinGameSuccess(joinGameResponse);
                    } else {
                        screen.notificationTable.setMessage(joinGameResponse.message);
                    }
                } else {
                    screen.notificationTable.setMessage(SERVER_ERROR);
                }
                response.close();
            }
        });
    }
}
