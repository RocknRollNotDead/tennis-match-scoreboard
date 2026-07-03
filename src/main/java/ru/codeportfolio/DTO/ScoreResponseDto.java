package ru.codeportfolio.DTO;



public record ScoreResponseDto(
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,

        String winnerName) {

}

