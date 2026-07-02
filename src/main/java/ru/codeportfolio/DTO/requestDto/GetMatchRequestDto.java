package ru.codeportfolio.DTO.requestDto;

public class GetMatchRequestDto {
    private final String playerName;

    public GetMatchRequestDto(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
