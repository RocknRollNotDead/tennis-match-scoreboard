package ru.codeportfolio.models;

import ru.codeportfolio.models.entities.Player;

public class TieBreak {

    private int homePlayerPoints;
    private int guestPlayerPoints;

    public TieBreak() {
        homePlayerPoints = 0;
        guestPlayerPoints = 0;
    }

    public int getHomePlayerPoints() {
        return homePlayerPoints;
    }

    public int getGuestPlayerPoints() {
        return guestPlayerPoints;
    }

    public void incHomePlayerPoints(){
        homePlayerPoints++;
    }

    public void incGuestPlayerPoints(){
        guestPlayerPoints++;
    }

}
