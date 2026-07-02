package ru.codeportfolio.models;

public enum Point {
    NULL_STUDY("00"),
    ONE_STUDY("15"),
    SECOND_STUDY("30"),
    THRID_STUDY("40"),
    LAST_STUDY("AD");


    private final String code;

    Point(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
