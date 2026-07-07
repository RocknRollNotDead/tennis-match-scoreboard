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

        }

        else if (games == 5) {
            if (opponentScore.getSet().getGames() == 5) {
                games++;
            } else if(opponentScore.getSet().getGames() == 6) {

                System.out.println(tieBreak);

                if (tieBreak == null) {

                    opponentScore.createTiebreak();
                    tieBreak = new TieBreak2();
                }

                if (((tieBreak.getInt() - opponentScore.getTieBreakPoints()) >= 2) && tieBreak.getInt() >= 6) {
                    super.incSets();
                    opponentScore.obnulitGames();
                    games = 0;
                } else {
                    tieBreak.inc();
                }
            } else {
                super.incSets();
                opponentScore.obnulitGames();
                games = 0;
            }
        } else if (games == 6) {

            super.incSets();
            opponentScore.obnulitGames();
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

    protected void obnulitGames(){
        games = 0;
    }

    protected void createTiebreak(){
        System.out.println("create tie");
        tieBreak = new TieBreak2();
    }
}
