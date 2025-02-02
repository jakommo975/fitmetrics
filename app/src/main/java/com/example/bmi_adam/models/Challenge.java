package com.example.bmi_adam.models;

public class Challenge {
    public int id = 1;
    public int  userId = 1;

    public int pushUps;
    public int sitUps;
    public int squats;

    public boolean completedPushups = false;
    public boolean completedSquats = false;
    public boolean completedSitups = false;

    public Challenge(
            int pushUps,
            int squats,
            int sitUps,
            boolean completedPushups,
            boolean completedSquats,
            boolean completedSitups
    ) {
        this.pushUps = pushUps;
        this.sitUps = sitUps;
        this.squats = squats;

        this.completedPushups = completedPushups;
        this.completedSquats = completedSquats;
        this.completedSitups = completedSitups;
    }

    public boolean getIsCompleted() {
        return this.completedPushups && this.completedSquats && this.completedSitups;
    }

    public void setCompletedState(Exercise exercise, boolean completed) {
        if (exercise.equals(Exercise.PUSHUPS)) {
            this.completedPushups = completed;
        }

        if (exercise.equals(Exercise.SQUATS)) {
            this.completedSquats = completed;
        }

        if (exercise.equals(Exercise.SITUPS)) {
            this.completedSitups = completed;
        }
    }

    public String toText(Exercise exercise) {
        if (exercise.equals(Exercise.PUSHUPS)) {
            return "Kliky:" + this.pushUps;
        } else if (exercise.equals(Exercise.SQUATS)) {
            return "Drepy:" + this.squats;
        } else {
            return "Brušáky:" + this.sitUps;
        }
    }

}
