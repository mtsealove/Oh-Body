package com.mtsealove.github.ohbody.Database;

public class Nutrient {
    double Protein;
    double Carbohydrate;
    double Fat;
    double Natrium;
    double Sugar;
    double Trans;
    double SFA;
    double Cholesterol;

    //칼로리를 인자로 받아 영양소 설정
    public Nutrient(double Kcal) {
        Protein = Kcal * 0.2/4;
        Carbohydrate = Kcal * 0.6/4;
        Fat = Kcal * 0.2/9;
        Natrium = 2;
        Sugar = Kcal * 0.05;
        Trans = Kcal * 0.01;
        SFA = Kcal * 0.05;
        Cholesterol = 0.3;
    }

    public double getProtein() {
        return Protein;
    }

    public void setProtein(double protein) {
        Protein = protein;
    }

    public double getCarbohydrate() {
        return Carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        Carbohydrate = carbohydrate;
    }

    public double getFat() {
        return Fat;
    }

    public void setFat(double fat) {
        Fat = fat;
    }

    public double getNatrium() {
        return Natrium;
    }

    public void setNatrium(double natrium) {
        Natrium = natrium;
    }

    public double getSugar() {
        return Sugar;
    }

    public void setSugar(double sugar) {
        Sugar = sugar;
    }

    public double getTrans() {
        return Trans;
    }

    public void setTrans(double trans) {
        Trans = trans;
    }

    public double getSFA() {
        return SFA;
    }

    public void setSFA(double SFA) {
        this.SFA = SFA;
    }

    public double getCholesterol() {
        return Cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        Cholesterol = cholesterol;
    }
}
