package ru.codeportfolio.models.score;

import ru.codeportfolio.DTO.ScoreResponseDto;
import ru.codeportfolio.DTO.mapper.ToDtoUtil;
import ru.codeportfolio.models.score2.Game2;
import ru.codeportfolio.models.score2.Score2;

public class TennisMatch {
    private final String player1;
    private final String player2;
    private final Score2 score2;
    private final Score2 score1;


    public TennisMatch(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;

        score1 = new Game2();
        score2 = new Game2();
    }

    public void incPoint(String player){
        if(player.equals(this.player1)){
            score1.incPoint(score2);
        } else if (player.equals(this.player2)) {
            score2.incPoint(score1);
        }

    }

    public String getWinner(){
        if (score1.isWin()){
            return player1;
        } else if (score2.isWin()) {
            return player2;
        } else {
            return null;
        }
    }


    public ScoreResponseDto getScore(){
        return ToDtoUtil.toResponseDtoFromTennisMatch(
                score1.getScore(), score2.getScore(),
                player1, player2,
                getWinner());
    }
}
