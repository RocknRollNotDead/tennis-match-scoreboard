package ru.codeportfolio.models.score;

public class Points {

    private int score;

    public Points(){
        score = 0;
    }


    public String getCode() {
        String code = null;
        switch (score){
            case 0:
                code = "00";
                break;
            case 1:
                code = "15";
                break;
            case 2:
                code = "30";
                break;
            case 3:
                code = "40";
                break;
            case 4:
                code = "AD";
                break;
        }
        return code;
    }

    int getScore(){
        return score;
    }

    public void nextStudy(){
        score++;
    }

    public void lowerPoint(){
        if (score >= 4){ // AD
            score--;
        }
    }




}
