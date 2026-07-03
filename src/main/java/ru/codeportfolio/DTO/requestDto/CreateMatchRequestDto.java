package ru.codeportfolio.DTO.requestDto;


public class CreateMatchRequestDto {
    private final String firstPlayerName;
    private final String secondPlayerName;

    public CreateMatchRequestDto(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }
}
