package ru.codeportfolio.models.score2;

public class TieBreak2 {
    private int score;

    public TieBreak2() {
        this.score = 0;
    }

    public void inc() {
        score++;
    }

    public int getInt(){
        return score;
    }

}
