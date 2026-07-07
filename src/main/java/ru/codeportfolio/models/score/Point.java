package ru.codeportfolio.models.score;

public enum Point {
    NULL("00"),
    FIFTEEN("15"),
    THIRTY("30"),
    FOURTY("40"),
    AD("AD");

    // не соответствует принципу KISS
    private final String code;

    Point(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Point getNext(){
        if (this == AD){
            throw new RuntimeException();
        }
        return Point.values()[this.ordinal() + 1];
    }

    public Point getLower(){
        if (this == NULL){
            throw new RuntimeException();
        }
        return Point.values()[this.ordinal() - 1];
    }


}
