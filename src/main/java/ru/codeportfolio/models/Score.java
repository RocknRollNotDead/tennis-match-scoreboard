package ru.codeportfolio.models;

import ru.codeportfolio.models.entities.Player;

public class Score {
    private final Player homePlayer;
    private final Player guestPlayer;
    private Game game;
    private Set set;
    private TieBreak tieBreak;
    private Player winner;
    private int generalScoreHomePlayer;
    private int generalScoreGuestPlayer;


    public Score(Player homePlayer, Player guestPlayer) {
        this.homePlayer = homePlayer;
        this.guestPlayer = guestPlayer;
    }



    public void incHomePlayerPoint(){
        game.incHomePlayerPoints();
    }

    public void incGuestPlayerPoint(){
        game.incGuestPlayerPoints();
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
