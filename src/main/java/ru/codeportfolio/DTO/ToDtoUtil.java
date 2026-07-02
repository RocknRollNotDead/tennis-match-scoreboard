package ru.codeportfolio.DTO;

import ru.codeportfolio.models.entities.Match;

import java.util.List;

public class ToDtoUtil {

    public static List<MatchDto> toMatchDtoList(List<Match> matches){
        return matches.stream().map(match ->
                new MatchDto(match.getHomePlayer().getName(),
                        match.getGuestPlayer().getName(),
                        match.getWinner().getName())).toList();
    }
}
