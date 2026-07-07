package ru.codeportfolio.models.score2;

import ru.codeportfolio.models.score.Game;
import ru.codeportfolio.models.score.Point;

public class Game2 extends Set2 {

    private Point point;

    public Game2() {
        super();
        this.point = Point.NULL;
    }

    @Override
    public void incPoint(Score2 opponentScore){

        // tieBreak



        try {

            getTieBreakPoints();
            super.incGame(opponentScore);
        } catch (NullPointerException e) {

            String opponentPoints = opponentScore
                    .getGame()
                    .getPoint()
                    .getCode();

            if (point.getCode().equals("AD")){

                if (opponentPoints.equals("AD")){
                    point = point.getLower();
                } else {
                    super.incGame(opponentScore);
                    point = Point.NULL;
                    opponentScore.obnulit();

                }
            } else if (point.getCode().equals("40")){

                if (opponentPoints.equals("40")){
                    point = point.getNext();
                } else if (!opponentPoints.equals("AD")){
                    super.incGame(opponentScore);
                    point = Point.NULL;
                    opponentScore.obnulit();
                } else {
                    opponentScore.lowerPoint();
                }
            } else {
                point = point.getNext();
            }

        }





    }

    @Override
    protected Game2 getGame() {
        return this;
    }

    public Point getPoint() {
        return point;
    }

    protected void obnulit(){
        point = Point.NULL;
    }

    protected void lowerPoint(){
        point = point.getLower();
    }


}
