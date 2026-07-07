package ru.codeportfolio.models.score;

public class Points {

    private int score;
//    private Point point;

    public Points(){
        score = 0;
//        point = Point.NULL;
    }


    public String getCode() {
        String code = switch (score) {
            case 0 -> "00";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            case 4 -> "AD";
            default -> null;
        };
        return code;
    }

    int getScore(){
        return score;
    }

    public void nextStudy(){
        score++;
    }

    public void nextPoint(){
//        if (point != null){
//            point = point.getNext();
//        }

    }

    public void lowerPoint(){
        if (score >= 4){ // AD
            score--;
        }
//        point = point.getLower();


    }




}
