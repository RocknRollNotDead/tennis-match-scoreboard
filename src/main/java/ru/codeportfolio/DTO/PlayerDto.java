package ru.codeportfolio.DTO;


public record PlayerDto(
        String name,
        String points,
        Integer games,
        int sets,
        Integer tieBreakPoints
) {

}
