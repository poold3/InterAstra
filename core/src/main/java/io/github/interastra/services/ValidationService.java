package io.github.interastra.services;

public class ValidationService {
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 15;
    public static final String NAME_VALIDATION_MESSAGE = String.format(
        "Names must be between %d and %d characters.",
        MIN_NAME_LENGTH,
        MAX_NAME_LENGTH
    );

    public static boolean validateName(final String name) {
        int length = name.length();
        return (length >= MIN_NAME_LENGTH && length <= MAX_NAME_LENGTH && !name.contains(" "));
    }

    public static boolean validateGameCode(final String gameCode) {
        for (char c : gameCode.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return (gameCode.length() == 6);
    }
}
