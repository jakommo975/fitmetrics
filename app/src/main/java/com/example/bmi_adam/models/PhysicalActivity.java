package com.example.bmi_adam.models;

public class PhysicalActivity {
    public int id = 1;
    public int userId = 1;

    public ActivityType activityType;

    public double duration;

    public PhysicalActivity(ActivityType activityType, double duration) {
        this.activityType = activityType;
        this.duration = duration;
    }
}
