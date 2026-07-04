package ru.codeportfolio.models.score;

public class Score {
    private final String homePlayer;
    private final String guestPlayer;
    private Game game;
    private Set set;
    private TieBreak tieBreak;
    private String winner;
    private int generalScoreHomePlayer = 0;
    private int generalScoreGuestPlayer = 0;

    // поля по тз

    // согласно принципам программирования джава, класс таким и должен быть. Счёт не должен знать,
    // какой там класс player, не должен знать о том, что существует такой класс.
    // Поэтому единственный вариант - прописывать это в классах.


    public Score(String homePlayer, String guestPlayer) {
        this.homePlayer = homePlayer;
        this.guestPlayer = guestPlayer;
        game = new Game();
        set = new Set();
    }


    public void incHomePlayerPoint() {

        if (tieBreak != null) {
            incHomeInPlayTieBreak();
        } else {
            game.incHomePlayerPoints();
            if (game.getHomePlayerPoints().getScore() >= 4) {
                incHomeGame();
            }
        }
    }

    public void incGuestPlayerPoint() {

        if (tieBreak != null) {
            incGuestInPlayTieBreak();
        } else {
            game.incGuestPlayerPoints();

            if (game.getGuestPlayerPoints().getScore() >= 4) {
                incGuestGame();
            }
        }
    }



    private void incHomeGame(){
        if (checkDifferencyTo2balls(
                game.getHomePlayerPoints().getScore(),
                game.getGuestPlayerPoints().getScore())) {

            set.incHomePlayerGames();
            game = new Game();

            if (set.getHomePlayerGames() >= 6) {

                incHomeSetOrTurnOnTiebreak();

            }
        } else if (game.getGuestPlayerPoints().getScore() >= 4) {
            game.lowerHomePlayerPoint();
            game.lowerGuestPlayerPoint();
        }
    }

    private void incHomeSetOrTurnOnTiebreak(){
        if (checkDifferencyTo2balls(
                set.getHomePlayerGames(),
                set.getGuestPlayerGames()
        )) {
            generalScoreHomePlayer++;
            set = new Set();

            if (generalScoreHomePlayer >= 2) {
                winner = homePlayer;
            }

        } else if (set.getGuestPlayerGames() >= 6) {
            turnOnTieBreak();
        }
    }

    private void incHomeInPlayTieBreak(){
        tieBreak.incHomePlayerPoints();
        if ((tieBreak.getHomePlayerPoints() >= 7) &&
                checkDifferencyTo2balls(
                        tieBreak.getHomePlayerPoints(),
                        tieBreak.getGuestPlayerPoints()
                )) {
            generalScoreHomePlayer++;
            game = new Game();
            set = new Set();
            tieBreak = null;
        }
    }



    private void incGuestGame(){
        if (checkDifferencyTo2balls(
                game.getHomePlayerPoints().getScore(),
                game.getGuestPlayerPoints().getScore())) {

            set.incGuestPlayerGames();
            game = new Game();

            if (set.getGuestPlayerGames() >= 6) {
                incGuestSetOrTurnOnTiebreak();
            }
        } else if (game.getHomePlayerPoints().getScore() >= 4) {
            game.lowerHomePlayerPoint();
            game.lowerGuestPlayerPoint();
        }
    }

    private void incGuestSetOrTurnOnTiebreak(){
        if (checkDifferencyTo2balls(
                set.getHomePlayerGames(),
                set.getGuestPlayerGames()
        )) {
            generalScoreGuestPlayer++;
            set = new Set();

            if (generalScoreGuestPlayer >= 2) {
                winner = guestPlayer;
            }
        } else if (set.getHomePlayerGames() >= 6) {
            turnOnTieBreak();
        }
    }

    private void incGuestInPlayTieBreak(){
        tieBreak.incGuestPlayerPoints();
        if ((tieBreak.getGuestPlayerPoints() >= 7) &&
                checkDifferencyTo2balls(
                        tieBreak.getHomePlayerPoints(),
                        tieBreak.getGuestPlayerPoints()
                )) {
            generalScoreGuestPlayer++;
            game = new Game();
            set = new Set();
            tieBreak = null;
        }
    }



    private void turnOnTieBreak() {
        tieBreak = new TieBreak();
        game = null;
    }

    private boolean checkDifferencyTo2balls(int first, int second) {
        return Math.abs(first - second) >= 2;

    }


    // getters

    public String getGuestPlayerName() {
        return guestPlayer;
    }

    public String getHomePlayerName() {
        return homePlayer;
    }

    public Game getGame() {
        return game;
    }

    public Set getSet() {
        return set;
    }

    public TieBreak getTieBreak() {
        return tieBreak;
    }

    public String getWinnerName() {
        return winner;
    }

    public int getGeneralScoreHomePlayer() {
        return generalScoreHomePlayer;
    }

    public int getGeneralScoreGuestPlayer() {
        return generalScoreGuestPlayer;
    }
}
