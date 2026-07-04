package ru.codeportfolio.models.score;

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
        homePlayerPoints.nextStudy();
    }

    public Points getGuestPlayerPoints() {
        return guestPlayerPoints;
    }

    public void incGuestPlayerPoints() {
        guestPlayerPoints.nextStudy();
    }

    public void lowerGuestPlayerPoint() {
        guestPlayerPoints.lowerPoint();
    }

    public void lowerHomePlayerPoint() {
        homePlayerPoints.lowerPoint();
    }



}
