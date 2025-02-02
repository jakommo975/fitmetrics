package com.example.bmi_adam.models;

import androidx.annotation.NonNull;

public enum ActivityType {
    NONE(0.0, "Žiadna aktivita", "none"),
    SWIMMING(7.0, "Plávanie", "swimming"),
    RUNNING(8.0, "Beh", "running"),
    EXERCISING(5.0, "Cvičenie", "exercising"),
    CYCLING(6.0, "Bicyklovanie", "cycling");

    public final double met;
    public final String name;
    public final String value;

    ActivityType(double met, String name, String value) {
        this.met = met;
        this.name = name;
        this.value = value;

    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public static ActivityType fromValue(String value) {
        for (ActivityType activity : ActivityType.values()) {
            if (activity.value.equals(value)) {
                return activity;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
