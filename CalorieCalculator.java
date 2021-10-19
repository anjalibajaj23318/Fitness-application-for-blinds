package com.fitness.tracker;

/**
 * Created by inspirin on 11/15/2017.
 */

public class CalorieCalculator {
    public double bmrCalculatorMale(int WEIGHT_IN_KILOGRAM, int HEIGHT_IN_CENTIMETER, int AGE_IN_YEAR) {

        double BMR = 66 + (13.7 * WEIGHT_IN_KILOGRAM) + (5 * HEIGHT_IN_CENTIMETER) - (6.8 * AGE_IN_YEAR);//
//BMR = 66 + (6.23 x WEIGHT_IN_POUNDS) + (12.7 x HEIGHT_IN_INCH) - (6.8 x AGE_IN_YEAR)
        return BMR;
    }

    public double bmrCalculatorFeMale(int WEIGHT_IN_KILOGRAM, int HEIGHT_IN_CENTIMETER, int AGE_IN_YEAR) {

        double BMR = 655 + (9.6 * WEIGHT_IN_KILOGRAM) + (1.8 * HEIGHT_IN_CENTIMETER) - (4.7 * AGE_IN_YEAR);

        return BMR;
    }

    public double calculateBMI(double weightInKG, double feet, double inches) {
        double weightInKilos = weightInKG;
        double heightInMeters = (((feet * 12) + inches) * .0254);
        double bmi = weightInKilos / Math.pow(heightInMeters, 2.0);
        return bmi;
    }

    public double calorieCalculatorMale(double EXERCISE_LEVEL_FECTOR, double WEIGHT_IN_KILOGRAM, double HEIGHT_IN_CENTIMETER, double AGE_IN_YEAR) {

        double Daily_Calories = EXERCISE_LEVEL_FECTOR * (66 + (13.7 * WEIGHT_IN_KILOGRAM) + (5 * HEIGHT_IN_CENTIMETER) - (6.8 * AGE_IN_YEAR));

        return Daily_Calories;
    }

    public double calorieCalculatorFeMale(double EXERCISE_LEVEL_FECTOR, double WEIGHT_IN_KILOGRAM, double HEIGHT_IN_CENTIMETER, double AGE_IN_YEAR) {

        //double Daily_Calories = EXERCISE_LEVEL_FECTOR * (66 + (13.7 x WEIGHT_IN_KILOGRAM) + (5 x HEIGHT_IN_CENTIMETER) - (6.8 x AGE_IN_YEAR));
        double Daily_Calories = EXERCISE_LEVEL_FECTOR * (655 + (9.6 * WEIGHT_IN_KILOGRAM) + (1.8 * HEIGHT_IN_CENTIMETER) - (4.7 * AGE_IN_YEAR));
        return Daily_Calories;
    }

    public double exerciseLevelCalcMale(String exerciseLevel) {
        double value = 0.0;
        switch (exerciseLevel) {
            case "Sedentary":
                value = 1.0;
                break;
            case "Lightly Active":
                value = 1.12;
                break;
            case "Moderately Active":
                value = 1.27;
                break;
            case "Very Active":
                value = 1.54;
                break;
            case "Exteremly Active":
                value = 1.56;
                break;
            default:
                value = 1.0;
        }
        return value;
    }

    public double exerciseLevelCalcFeMale(String exerciseLevel) {
        double value = 0.0;
        switch (exerciseLevel) {
            case "Sedentary":
                value = 1.0;
                break;
            case "Lightly Active":
                value = 1.14;
                break;
            case "Moderately Active":
                value = 1.27;
                break;

            case "Very Active":
                value = 1.45;
                break;
            case "Exteremly Active":
                value = 1.49;
                break;
            default:
                value = 1.0;
        }
        return value;
    }

    public double fatCalculator(double total_caloreis) {
        return Math.floor((total_caloreis * 0.3) / 9);
    }

    public double protienCalculator(double total_weight) {
        return Math.round(total_weight * 2.2046);
    }

    public double carboHydrateCalculator(double total_caloreis, double total_protein, double total_fat) {
        return Math.floor((total_caloreis - total_protein * 4 - total_fat * 9) / 4);
    }
}
