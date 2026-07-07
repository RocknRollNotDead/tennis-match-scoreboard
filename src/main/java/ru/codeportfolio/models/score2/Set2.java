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
        if (games <= 5) {
            games++;

            if (opponentScore.getSet().getGames() == 5) {
                games++;
            } else {
                super.incSets();
                games = 0;
            }
        } else if (games == 6) {
            if (opponentScore.getSet().getGames() == 6) {

                if (tieBreak == null) {
                    tieBreak = new TieBreak2();
                }

                if (((tieBreak.getInt() - opponentScore.getTieBreakPoints()) >= 2) && tieBreak.getInt() >= 6) {
                    super.incSets();
                } else {
                    tieBreak.inc();
                }


//                if () {
//                    super.incSets();
//                    tieBreak = null;
//                }

            }
        } else {
            super.incSets();
            games = 0;
        }

    }

    @Override
    protected Set2 getSet() {
        return this;
    }

    protected TieBreak2 getTieBreak() {
        return tieBreak;
    }

    protected int getGames() {
        return games;
    }
}
