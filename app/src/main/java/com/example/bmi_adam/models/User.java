package com.example.bmi_adam.models;

import android.database.Cursor;

import androidx.annotation.NonNull;

public class User {
    private int id;
    private String name;
    private String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    // Constructor for creating a new user without an ID
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters and Setters (same as above)

    // Utility: Create a User from a database cursor
    public static User fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        return new User(id, name, password);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}