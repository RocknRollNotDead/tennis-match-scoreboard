package ru.codeportfolio.models.score2;

public abstract class Set2 extends Score2 {

    private int games;
    private TieBreak2 tieBreak = null;

    public Set2() {
        super();
        this.games = 0;
    }

    protected void incGame(Score2 opponentScore) {
        if (games <= 4) {
            games++;

        } else if (games == 5) {
            if (opponentScore.getSet().getGames() == 5) {
                games++;
            } else if (opponentScore.getSet().getGames() == 6) {


                opponentScore.createTiebreak();
                createTiebreak();

                games++;

            } else {
                super.incSets();
                opponentScore.clearGames();
                games = 0;
            }
        } else if (games == 6) {

            if (tieBreak != null || opponentScore.getGames() == 6) {

                incTieBreakPoint(opponentScore);
            } else {

                super.incSets();
                opponentScore.clearGames();
                games = 0;
            }
        }
    }

    private void incTieBreakPoint(Score2 opponentScore) {
        tieBreak.inc();

        if (hasDifferenceInTieBreak(opponentScore) && tieBreak.getInt() >= 7) {
            opponentScore.clearGames();
            games = 0;
            tieBreak = null;
            opponentScore.clearTieBreaks();
            super.incSets();

        }
    }

    protected TieBreak2 getTieBreak() {
        return tieBreak;
    }

    @Override
    protected Set2 getSet() {
        return this;
    }

    @Override
    protected int getGames() {
        return games;
    }


    @Override
    protected void clearTieBreaks() {
        tieBreak = null;
    }

    @Override
    protected void createTiebreak() {
        tieBreak = new TieBreak2();
    }

    @Override
    protected boolean hasTieBreak() {
        return tieBreak != null;
    }

    @Override
    protected void clearGames() {
        games = 0;
    }

    private boolean hasDifferenceInTieBreak(Score2 opponentScore) {
        return (tieBreak.getInt() - opponentScore.getTieBreakPoints()) >= 2;
    }
}