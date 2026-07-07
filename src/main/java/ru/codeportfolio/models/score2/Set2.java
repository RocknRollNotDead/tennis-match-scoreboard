package ru.codeportfolio.models.score2;

public abstract class Set2 extends Score2 {
    // 6-0
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

                if (tieBreak == null) {

                    opponentScore.createTiebreak();
                    createTiebreak();
                    tieBreak = new TieBreak2();


                }
                games++;

//                incTieBreakPoint(opponentScore);
            } else {
                super.incSets();
                opponentScore.obnulitGames();
                games = 0;
            }
        } else if (games == 6) {

            if (tieBreak != null || opponentScore.getGames() == 6) {

                incTieBreakPoint(opponentScore);
            } else {

                super.incSets();
                opponentScore.obnulitGames();
                games = 0;
            }
        }
    }

    private void incTieBreakPoint(Score2 opponentScore) {
        tieBreak.inc();

        if (((tieBreak.getInt() - opponentScore.getTieBreakPoints()) >= 2) && tieBreak.getInt() >= 7) {
            opponentScore.obnulitGames();
            games = 0;
            tieBreak = null;
            opponentScore.obnulitTieBreaks();
            super.incSets();

        }
    }

    @Override
    protected Set2 getSet() {
        return this;
    }

    protected void obnulitTieBreaks() {
        tieBreak = null;
    }

    protected TieBreak2 getTieBreak() {
        return tieBreak;
    }

    protected int getGames() {
        return games;
    }

    protected void obnulitGames() {
        games = 0;
    }

    protected void createTiebreak() {
        tieBreak = new TieBreak2();
    }
}
