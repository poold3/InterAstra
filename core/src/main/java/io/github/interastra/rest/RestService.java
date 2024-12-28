package io.github.interastra.rest;

import io.github.interastra.rest.callbacks.AddBaseCallback;
import io.github.interastra.rest.callbacks.JoinGameCallback;
import io.github.interastra.rest.callbacks.SetReadyCallback;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.screens.MainMenuScreen;
import io.github.interastra.services.InterAstraLog;
import okhttp3.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RestService {
    public static String BASE_URL = "http://localhost:8090";
    public static final String SERVER_ERROR = "We ran into an error. Please try again later.";
    public static final OkHttpClient client = new OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .build();

    public static void shutdownClient() {
        try {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void newGame(final MainMenuScreen screen, final String name) {
        try {
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
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void joinGame(final MainMenuScreen screen, final String name, final String gameCode) {
        try {
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
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void setReady(final LobbyScreen screen, final String gameCode, final String name, final boolean ready) {
        try {
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
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void addBase(final GameScreen screen, final String planetName, final String base, final int currentNumBases, final String rocketId) {
        try {
            RequestBody requestBody = new FormBody.Builder()
                .add("gameCode", screen.lobbyScreen.gameCode)
                .add("planetName", planetName)
                .add("base", base)
                .add("currentNumBases", String.valueOf(currentNumBases))
                .add("rocketId", rocketId)
                .build();

            Request request = new Request.Builder()
                .url(BASE_URL + "/add-base")
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .build();

            Call call = client.newCall(request);
            call.enqueue(new AddBaseCallback(screen));
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
