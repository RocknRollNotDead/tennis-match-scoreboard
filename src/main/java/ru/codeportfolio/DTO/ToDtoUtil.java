package ru.codeportfolio.DTO;

import ru.codeportfolio.models.Score;
import ru.codeportfolio.models.entities.Match;

import java.util.List;

public class ToDtoUtil {

    public static List<OneMatchDto> toMatchDtoList(List<Match> matches){
        return matches.stream().map(match ->
                new OneMatchDto(match.getHomePlayer().getName(),
                        match.getGuestPlayer().getName(),
                        match.getWinner().getName())).toList();
    }

    public static ScoreResponseDto2legacy toResponseDtoFromScore2legacy(Score score){

        Integer tieBreakHomePlayerPoints = null;
        Integer tieBreakGuestPlayerPoints = null;
        String winnerName = null;

        if(score.getTieBreak() != null){
            tieBreakHomePlayerPoints = score.getTieBreak().getHomePlayerPoints();
            tieBreakGuestPlayerPoints = score.getTieBreak().getGuestPlayerPoints();
        }

        if (score.getWinner() != null){
            winnerName = score.getWinner().getName();
        }

        ScoreResponseDto2legacy scoreResponseDto = new ScoreResponseDto2legacy(
                score.getHomePlayer().getName(),
                score.getGuestPlayer().getName(),
                score.getGame().getHomePlayerPoints().getCode(),
                score.getGame().getGuestPlayerPoints().getCode(),
                score.getSet().getHomePlayerGames(),
                score.getSet().getGuestPlayerGames(),
                score.getGeneralScoreHomePlayer(),
                score.getGeneralScoreGuestPlayer(),
                tieBreakHomePlayerPoints,
                tieBreakGuestPlayerPoints,
                winnerName
        );
        return scoreResponseDto;
    }

    public static ScoreResponseDto toResponseDtoFromScore(Score score){

        Integer tieBreakHomePlayerPoints = null;
        Integer tieBreakGuestPlayerPoints = null;
        String winnerName = null;

        if(score.getTieBreak() != null){
            tieBreakHomePlayerPoints = score.getTieBreak().getHomePlayerPoints();
            tieBreakGuestPlayerPoints = score.getTieBreak().getGuestPlayerPoints();
        }

        if (score.getWinner() != null){
            winnerName = score.getWinner().getName();
        }

        PlayerDto firstPlayer = new PlayerDto(
                score.getHomePlayer().getName(),
                score.getGame().getHomePlayerPoints().getCode(),
                score.getSet().getHomePlayerGames(),
                score.getGeneralScoreHomePlayer(),
                tieBreakHomePlayerPoints
                );
        PlayerDto secondPlayer = new PlayerDto(
                score.getGuestPlayer().getName(),
                score.getGame().getGuestPlayerPoints().getCode(),
                score.getSet().getGuestPlayerGames(),
                score.getGeneralScoreGuestPlayer(),
                tieBreakGuestPlayerPoints
                );


        ScoreResponseDto scoreResponseDto = new ScoreResponseDto(
                firstPlayer, secondPlayer, winnerName
        );
        return scoreResponseDto;
    }



}
