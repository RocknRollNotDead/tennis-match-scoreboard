package ru.codeportfolio.DTO.mapper;

import ru.codeportfolio.DTO.OneMatchDto;
import ru.codeportfolio.DTO.PlayerDto;
import ru.codeportfolio.DTO.ScoreResponseDto;
import ru.codeportfolio.DTO.ScoreResponseDto2legacy;
import ru.codeportfolio.models.score.Score;
import ru.codeportfolio.models.entities.Match;

import java.util.List;

public class ToDtoUtil {

    public static List<OneMatchDto> toMatchDtoList(List<Match> matches){
        return matches.stream().map(match ->
                new OneMatchDto(match.getHomePlayer().getName(),
                        match.getGuestPlayer().getName(),
                        match.getWinner() == null ? null : match.getWinner().getName()
                        )).toList();
    }

    // это формат ответа по тз
    public static ScoreResponseDto2legacy toResponseDtoFromScore2legacy(Score score){


        Integer tieBreakHomePlayerPoints = null;
        Integer tieBreakGuestPlayerPoints = null;


        if(score.getTieBreak() != null){
            tieBreakHomePlayerPoints = score.getTieBreak().getHomePlayerPoints();
            tieBreakGuestPlayerPoints = score.getTieBreak().getGuestPlayerPoints();
        }


        String winnerName = score.getWinnerName();


        ScoreResponseDto2legacy scoreResponseDto = new ScoreResponseDto2legacy(
                score.getHomePlayerName(),
                score.getGuestPlayerName(),
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

    // это формат ответа по запросу app.js
    public static ScoreResponseDto toResponseDtoFromScore(Score score){

        String homePlayerPoints = null;
        String guestPlayerPoints = null;
        if (score.getGame() != null){
            homePlayerPoints = score.getGame().getHomePlayerPoints().getCode();
            guestPlayerPoints = score.getGame().getGuestPlayerPoints().getCode();
        }

        Integer tieBreakHomePlayerPoints = null;
        Integer tieBreakGuestPlayerPoints = null;


        if(score.getTieBreak() != null){
            tieBreakHomePlayerPoints = score.getTieBreak().getHomePlayerPoints();
            tieBreakGuestPlayerPoints = score.getTieBreak().getGuestPlayerPoints();
        }

        String winnerName = score.getWinnerName();

        PlayerDto firstPlayer = new PlayerDto(
                score.getHomePlayerName(),
                homePlayerPoints,
                score.getSet().getHomePlayerGames(),
                score.getGeneralScoreHomePlayer(),
                tieBreakHomePlayerPoints
                );
        PlayerDto secondPlayer = new PlayerDto(
                score.getGuestPlayerName(),
                guestPlayerPoints,
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
