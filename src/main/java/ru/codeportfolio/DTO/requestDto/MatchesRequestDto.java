package ru.codeportfolio.DTO.requestDto;

public class MatchesRequestDto {
    private final Integer page;
    private final String playerName;

    public MatchesRequestDto(Integer page, String playerName) {
        this.page = page;
        this.playerName = playerName;
    }

    public Integer getPage() {
        return page;
    }

    public String getPlayerName() {
        return playerName;
    }
}
