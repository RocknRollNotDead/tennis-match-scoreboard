package ru.codeportfolio.models;

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

    public void nextStudy(){
        score++;
    }




}
