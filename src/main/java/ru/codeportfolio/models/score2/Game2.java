package ru.codeportfolio.models.score2;

import ru.codeportfolio.models.score.Point;

public class Game2 extends Set2 {

    private Point point;

    public Game2() {
        super();
        this.point = Point.NULL;
    }

    @Override
    public void incPoint(Score2 opponentScore){

        if (hasTieBreak()){

            getTieBreakPoints();
            super.incGame(opponentScore);
        } else {

            String opponentPoints = opponentScore
                    .getGame()
                    .getPoint()
                    .getCode();

            if (point.getCode().equals("AD")){

                super.incGame(opponentScore);
                point = Point.NULL;
                opponentScore.reset();

            } else if (point.getCode().equals("40")){

                if (opponentPoints.equals("40")){
                    point = point.getNext();
                } else if (!opponentPoints.equals("AD")){
                    super.incGame(opponentScore);
                    point = Point.NULL;
                    opponentScore.reset();
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

    protected void reset(){
        point = Point.NULL;
    }

    protected void lowerPoint(){
        point = point.getLower();
    }


}
