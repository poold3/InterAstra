package io.github.interastra.rest;

import io.github.interastra.rest.callbacks.JoinGameCallback;
import io.github.interastra.rest.callbacks.SetReadyCallback;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.screens.MainMenuScreen;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class RestService {
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
        call.enqueue(new JoinGameCallback(screen));
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
        call.enqueue(new JoinGameCallback(screen));
    }

    public static void setReady(final LobbyScreen screen, final String gameCode, final String name, final boolean ready) {
        RequestBody requestBody = new FormBody.Builder()
            .add("name", name)
            .add("gameCode", gameCode)
            .add("ready", String.valueOf(ready))
            .build();

        Request request = new Request.Builder()
            .url(BASE_URL + "/set-ready")
            .post(requestBody)
            .addHeader("Accept", "application/json")
            .build();

        Call call = client.newCall(request);
        call.enqueue(new SetReadyCallback(screen));
    }
}
