package com.fitness.tracker;

/**
 * Created by inspirin on 11/15/2017.
 */

public class UserProfile {
    public UserProfile(){

    }
String userid;

    public String getUserid() {
        return userid;
    }
double calorie;
double bmi;
int  steps;
int  heart_rate;

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UserProfile(String userid, double calorie, double bmi, int steps, int heart_rate, String height_Ft, String height_In, String age, String gender, String activeness, String body_fat, String weight) {
        this.userid = userid;
        this.calorie = calorie;
        this.bmi = bmi;
        this.steps = steps;
        this.heart_rate = heart_rate;
        this.height_Ft = height_Ft;
        this.height_In = height_In;
        this.age = age;
        this.gender = gender;
        this.activeness = activeness;
        this.body_fat = body_fat;
        this.weight = weight;
    }

    public String getHeight_Ft() {
        return height_Ft;
    }

    public void setHeight_Ft(String height_Ft) {
        this.height_Ft = height_Ft;
    }

    public String getHeight_In() {
        return height_In;
    }

    public void setHeight_In(String height_In) {
        this.height_In = height_In;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getActiveness() {
        return activeness;
    }

    public void setActiveness(String activeness) {
        this.activeness = activeness;
    }

    public String getBody_fat() {
        return body_fat;
    }

    public void setBody_fat(String body_fat) {
        this.body_fat = body_fat;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }



    String height_Ft,height_In,age,gender,activeness,body_fat,weight;
}
