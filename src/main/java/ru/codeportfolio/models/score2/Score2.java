package ru.codeportfolio.models.score2;

import ru.codeportfolio.DTO.PlayerDto;

public abstract class Score2 {

    private int sets = 0;

    public Score2() {

    }

    public boolean isWin(){
        return sets >= 2;
    }

    public Score2Dto getScore(){
        return new Score2Dto(
                getGame().getPoint().getCode(),
                getGame().getGames(),
                sets,
                getSet().getTieBreak(),
                isWin());
    }

    protected abstract Game2 getGame();

    protected abstract Set2 getSet();

    protected void incSets(){
        sets++;
    }

    protected int getTieBreakPoints(){
        return getSet().getTieBreak().getInt();
    }

    public abstract void incPoint(Score2 opponentScore);





}
