package ru.codeportfolio.DTO;

import ru.codeportfolio.models.Score;
import ru.codeportfolio.models.entities.Match;

import java.util.List;
import java.util.UUID;

public class ToDtoUtil {

    public static List<OneMatchDto> toMatchDtoList(List<Match> matches){
        return matches.stream().map(match ->
                new OneMatchDto(match.getHomePlayer().getName(),
                        match.getGuestPlayer().getName(),
                        match.getWinner().getName())).toList();
    }

    public static ResponseDto toResponseDtoFromScore(Score score){
        ResponseDto responseDto = new ResponseDto(
                score.getHomePlayer().getName(),
                score.getGuestPlayer().getName(),
                score.getGame().getHomePlayerPoints().getCode(),
                score.getGame().getGuestPlayerPoints().getCode(),
                score.getSet().getHomePlayerGames(),
                score.getSet().getGuestPlayerGames(),
                score.getGeneralScoreHomePlayer(),
                score.getGeneralScoreGuestPlayer(),
                score.getTieBreak().getHomePlayerPoints(),
                score.getTieBreak().getGuestPlayerPoints(),
                score.getWinner().getName()
        );
        return responseDto;
    }

}
