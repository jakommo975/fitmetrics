package com.example.bmi_adam.models;

public enum Exercise {
    PUSHUPS("pushups"),
    SQUATS("squats"),
    SITUPS("situps");

    private final String name;

    Exercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
