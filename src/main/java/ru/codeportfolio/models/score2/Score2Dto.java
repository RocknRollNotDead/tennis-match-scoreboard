package ru.codeportfolio.models.score2;

public record Score2Dto(
        String points,
        int games,
        int sets,
        TieBreak2 tieBreak,
        boolean winner

) {
}
