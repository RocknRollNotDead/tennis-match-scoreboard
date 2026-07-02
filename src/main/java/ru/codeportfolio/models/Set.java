package ru.codeportfolio.models;

public class Set {
    private int homePlayerGames;
    private int guestPlayerGames;

    public Set() {
        homePlayerGames = 0;
        guestPlayerGames = 0;
    }

    public int getHomePlayerGames() {
        return homePlayerGames;
    }

    public int getGuestPlayerGames() {
        return guestPlayerGames;
    }

    public void incHomePlayerGames(){
        homePlayerGames++;
    }

    public void incGuestPlayerGames(){
        guestPlayerGames++;
    }

}
