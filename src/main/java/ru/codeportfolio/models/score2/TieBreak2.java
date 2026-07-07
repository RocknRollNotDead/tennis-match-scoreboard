package ru.codeportfolio.models.score2;

public class TieBreak2 {
    private int score = 0;

    public void inc() {
        score++;
//        return score >= 7;
    }

    public int getInt(){
        return score;
    }

}
