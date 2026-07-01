package ru.codeportfolio.DTO;

public record ResponseDto(String firstPlayerName, String secondPlayerName, String firstPlayerPoints,
                          String secondPlayerPoints, int firstPlayerGames, int secondPlayerGames, int firstPlayerSets,
                          int secondPlayerSets, Integer firstPlayerTieBreakPoints, Integer secondPlayerTieBreakPoints,
                          String winnerName) {
}
