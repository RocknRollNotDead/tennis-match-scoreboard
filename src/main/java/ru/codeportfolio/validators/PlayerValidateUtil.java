package ru.codeportfolio.validators;

import ru.codeportfolio.exceptions.ValidationException;

public final class PlayerValidateUtil {
    private PlayerValidateUtil(){}

    public static String normalizeRequest(String playerName) {
        if (playerName == null || playerName.isBlank()){
            throw new ValidationException("request is empty");
        }
        if (playerName.length() > 100){
            throw new ValidationException("name must be less 100 symbols");
        }

        return playerName.trim();
    }
}
