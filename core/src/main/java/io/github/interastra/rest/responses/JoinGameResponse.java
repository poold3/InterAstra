package io.github.interastra.rest.responses;

public class JoinGameResponse extends Response {
    public String gameCode;

    public JoinGameResponse(boolean success, String message, String gameCode) {
        super(success, message);
        this.gameCode = gameCode;
    }
}
