package ru.codeportfolio.DTO;


public record PlayerDto(
        String name,
        String points,
        int games,
        int sets,
        Integer tieBreakPoints
) {

}
