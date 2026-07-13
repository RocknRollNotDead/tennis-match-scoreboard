package ru.codeportfolio.models.score2;

public abstract class Score2 {

    private int sets = 0;

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

    public abstract void incPoint(Score2 opponentScore);

    protected void incSets(){
        sets++;
    }

    protected int getTieBreakPoints(){
        return getSet().getTieBreak().getInt();
    }

    protected abstract Game2 getGame();

    protected abstract int getGames();

    protected abstract Set2 getSet();

    protected abstract void reset();

    protected abstract void clearGames();

    protected abstract void lowerPoint();

    protected abstract void createTiebreak();

    protected abstract void clearTieBreaks();

    protected abstract boolean hasTieBreak();
}
