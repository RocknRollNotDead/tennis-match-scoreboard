package ru.codeportfolio.models;

import ru.codeportfolio.models.entities.Player;

public class Score {
    private final Player homePlayer;
    private final Player guestPlayer;
    private Game game;
    private Set set;
    private TieBreak tieBreak;
    private Player winner;
    private int generalScoreHomePlayer = 0;
    private int generalScoreGuestPlayer = 0;


    public Score(Player homePlayer, Player guestPlayer) {
        this.homePlayer = homePlayer;
        this.guestPlayer = guestPlayer;
        game = new Game();
        set = new Set();
    }

    // play new game
    // play new set
    // play new match


    public void incHomePlayerPoint() {

        if (tieBreak != null) {
            tieBreak.incHomePlayerPoints();
            if ((tieBreak.getHomePlayerPoints() >= 7) &&
                    checkDifferencyTo2balls(
                            tieBreak.getHomePlayerPoints(),
                            tieBreak.getGuestPlayerPoints()
                    )) {
                set.incHomePlayerGames();
                game = new Game();
                tieBreak = null;
            }
        }


        game.incHomePlayerPoints();

        if (game.getHomePlayerPoints().getScore() >= 4) {
            if (checkDifferencyTo2balls(
                    game.getHomePlayerPoints().getScore(),
                    game.getGuestPlayerPoints().getScore())) {

                set.incHomePlayerGames();
                game = new Game();


                if (set.getHomePlayerGames() >= 6) {

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
                    } else {
                        generalScoreHomePlayer++;
                    }

                }
            } else if (game.getGuestPlayerPoints().getScore() >= 4) {
                game.lowerHomePlayerPoint();
                game.lowerGuestPlayerPoint();
            }

        }
    }

    public void incGuestPlayerPoint() {

        if (tieBreak != null) {
            tieBreak.incGuestPlayerPoints();
            if ((tieBreak.getGuestPlayerPoints() >= 7) &&
                    checkDifferencyTo2balls(
                            tieBreak.getGuestPlayerPoints(),
                            tieBreak.getHomePlayerPoints()
                    )) {
                set.incGuestPlayerGames();
                game = new Game();
                tieBreak = null;
            }
        }

        game.incGuestPlayerPoints();

        if (game.getGuestPlayerPoints().getScore() >= 4) {
            if (checkDifferencyTo2balls(
                    game.getHomePlayerPoints().getScore(),
                    game.getGuestPlayerPoints().getScore())) {

                set.incGuestPlayerGames();
                game = new Game();

                if (set.getGuestPlayerGames() >= 6) {
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
                    } else {
                        generalScoreGuestPlayer++;
                    }
                }
            } else if (game.getHomePlayerPoints().getScore() >= 4) {
                game.lowerHomePlayerPoint();
                game.lowerGuestPlayerPoint();

            }
        }


    }

    private void turnOnTieBreak() {
        tieBreak = new TieBreak();
        game = null;
    }

    private boolean checkDifferencyTo2balls(int first, int second) {
        return Math.abs(first - second) >= 2;

    }

    public Player getGuestPlayer() {
        return guestPlayer;
    }

    public Player getHomePlayer() {
        return homePlayer;
    }

    public Game getGame() {
        return game;
    }

    public Set getSet() {
        return set;
    }

    //    public Set getSet(Player player) {
//        if (player.equals(homePlayer)){
//            return set.getGuestPlayerGames();
//        }
//        return set;
//    }
    public TieBreak getTieBreak() {
        return tieBreak;
    }

    public Player getWinner() {
        return winner;
    }

    public int getGeneralScoreHomePlayer() {
        return generalScoreHomePlayer;
    }

    public int getGeneralScoreGuestPlayer() {
        return generalScoreGuestPlayer;
    }
}
