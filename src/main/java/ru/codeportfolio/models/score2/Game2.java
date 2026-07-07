package ru.codeportfolio.models.score2;

import ru.codeportfolio.models.score.Point;

public class Game2 extends Set2 {
    // 40-00
    private Point point;

    public Game2() {
        super();
        this.point = Point.NULL;
    }

    @Override
    public void incPoint(Score2 opponentScore){
        String opponentPoints = opponentScore
                .getGame()
                .getPoint()
                .getCode();
        if (point.getCode().equals("AD")){
            if (!opponentPoints.equals("40")){
                super.incGame(opponentScore);
                point = Point.NULL;
            } else {
                point = point.getLower();
            }
        } else if (point.getCode().equals("40")){
            if (opponentPoints.equals("40")){
                point = point.getNext();
            } else if (!opponentPoints.equals("AD")){
                super.incGame(opponentScore);
                point = Point.NULL;
            }


            // else do nothing;
        } else {
            point = point.getNext();
        }
    }

    @Override
    protected Game2 getGame() {
        return this;
    }

    public Point getPoint() {
        return point;
    }
}
