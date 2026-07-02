package ru.codeportfolio.models;

public class Game {
    private final Points homePlayerPoints;
    private final Points guestPlayerPoints;

    public Game() {
        homePlayerPoints = new Points();
        guestPlayerPoints = new Points();
    }

    public Points getHomePlayerPoints() {
        return homePlayerPoints;
    }

    public void incHomePlayerPoints() {
        this.homePlayerPoints.nextStudy();
    }

    public Points getGuestPlayerPoints() {
        return guestPlayerPoints;
    }

    public void incGuestPlayerPoints() {
        this.guestPlayerPoints.nextStudy();
    }
}
