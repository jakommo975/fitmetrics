package com.example.bmi_adam.models;

import androidx.annotation.Nullable;

import com.example.bmi_adam.R;

public class BodyData {
    public int id = -1;
    public int userId = -1;
    public double weight = -1;
    public double height = -1;
    public int age = -1;
    public String gender = "";
    public String goal = "";

    public int getGenderRadioId() {
        switch (this.gender) {
            case "M":
                return R.id.radioMan;
            case "W":
                return R.id.radioWoman;
            default:
                return -1;
        }
    }

    public void setGenderByRadioId(int radioId) {
        if (radioId == R.id.radioMan) {
            this.gender = "M";
        }

        if (radioId == R.id.radioWoman) {
            this.gender = "W";
        }

        if (radioId == -1) {
            this.gender = "";
        }
    }

    public int getGoalRadioId() {
        switch (this.goal) {
            case "gain":
                return R.id.radionGainWeight;
            case "keep":
                return R.id.radioKeepWeight;
            case "loose":
                return R.id.radioLoseWeight;
            default:
                return -1;
        }
    }

    public void setGoalByRadioId(int radioId) {
        if (radioId == R.id.radionGainWeight) {
            this.goal = "gain";
        }

        if (radioId == R.id.radioKeepWeight) {
            this.goal = "keep";
        }

        if (radioId == R.id.radioLoseWeight) {
            this.goal = "loose";
        }

        if (radioId == -1) {
            this.goal = "";
        }
    }

    public String getHeightString() {
        if (this.height == -1) {
            return "";
        }

        return String.valueOf(this.height);
    }

    public String getWeightString() {
        if (this.weight == -1) {
            return "";
        }

        return String.valueOf(this.weight);
    }

    public String getAgeString() {
        if (this.age == -1) {
            return "";
        }

        return String.valueOf(this.age);
    }

    public double calculateBmi() {
        double heightInMeters = this.height / 100;

        if (this.weight <= 0 || heightInMeters <= 0) {
            return -1;
        }

        return this.weight / (heightInMeters * heightInMeters);
    }
}
