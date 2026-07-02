package ru.codeportfolio.validators;

import ru.codeportfolio.exceptions.ValidationException;

public class PlayerValidateUtil {
    private PlayerValidateUtil(){}

    public static String normalizeRequest(String playerName) {
        if (playerName == null || playerName.isBlank()){
            throw new ValidationException("request is empty");
        }

        return playerName.trim();
    }
}
