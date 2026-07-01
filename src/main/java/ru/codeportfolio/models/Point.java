package ru.codeportfolio.models;

public enum Point {
//    NULL_STADY
//    ONE_STADY("15")


    private final String code;

    Point(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
