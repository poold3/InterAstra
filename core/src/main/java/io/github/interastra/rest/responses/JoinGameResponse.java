package io.github.interastra.rest.responses;

import java.util.ArrayList;

public class JoinGameResponse extends Response {
    public String gameCode;
    public ArrayList<String> names;

    public JoinGameResponse(boolean success, String message, String gameCode, ArrayList<String> names) {
        super(success, message);
        this.gameCode = gameCode;
        this.names = names;
    }
}
