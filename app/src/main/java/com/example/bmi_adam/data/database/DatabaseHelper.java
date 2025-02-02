package com.example.bmi_adam.data.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bmi_adam.models.ActivityType;
import com.example.bmi_adam.models.BodyData;
import com.example.bmi_adam.models.Challenge;
import com.example.bmi_adam.models.PhysicalActivity;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitmetrics_db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "password TEXT" +
                ");");

        // Create the user_data table
        db.execSQL("CREATE TABLE IF NOT EXISTS user_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "weight REAL, " +
                "height REAL, " +
                "age INTEGER, " +
                "gender TEXT, " +
                "goal TEXT, " +
                "bmi REAL, " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");");

        // Create the activities table
        db.execSQL("CREATE TABLE IF NOT EXISTS activities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER UNIQUE, " + // Ensures only one row per user
                "activity_type TEXT, " +
                "duration_minutes INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");");

        // Create the challenges table
        db.execSQL("CREATE TABLE IF NOT EXISTS challenges (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER UNIQUE, " +
                "pushups INTEGER, " +
                "squats INTEGER, " +
                "situps INTEGER, " +
                "completedPushups INTEGER DEFAULT 0, " + // Toggle for pushups
                "completedSquats INTEGER DEFAULT 0, " +  // Toggle for squats
                "completedSitups INTEGER DEFAULT 0, " +  // Toggle for situps
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");");

        // Insert a predefined user
        db.execSQL("INSERT OR IGNORE INTO users (name, password) VALUES ('admin', 'password');");
        db.execSQL("INSERT INTO user_data (user_id) VALUES (1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes (e.g., migrations) here
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS user_data");
        db.execSQL("DROP TABLE IF EXISTS activities");
        db.execSQL("DROP TABLE IF EXISTS challenges");
        onCreate(db);
    }

    // Method to verify user credentials
    public boolean verifyUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE name = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public void setBodyData(BodyData bodyData) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE user_data SET weight = ?, height = ?, age = ?, gender = ?, goal = ? WHERE user_id = ?";

        db.execSQL(query, new Object[]{
            bodyData.weight,
            bodyData.height,
            bodyData.age,
            bodyData.gender,
            bodyData.goal,
            bodyData.userId
        });
    }

    @SuppressLint("Range")
    public BodyData getBodyData(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user_data WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        BodyData bodyData = new BodyData();
        bodyData.id = cursor.isNull(cursor.getColumnIndex("id")) ? -1 : cursor.getInt(cursor.getColumnIndex("id"));
        bodyData.userId = cursor.isNull(cursor.getColumnIndex("user_id")) ? -1 : cursor.getInt(cursor.getColumnIndex("user_id"));
        bodyData.weight = cursor.isNull(cursor.getColumnIndex("weight")) ? -1 : cursor.getDouble(cursor.getColumnIndex("weight"));
        bodyData.height = cursor.isNull(cursor.getColumnIndex("height")) ? -1 : cursor.getDouble(cursor.getColumnIndex("height"));
        bodyData.age = cursor.isNull(cursor.getColumnIndex("age")) ? -1 : cursor.getInt(cursor.getColumnIndex("age"));
        bodyData.gender = cursor.isNull(cursor.getColumnIndex("gender")) ? "" : cursor.getString(cursor.getColumnIndex("gender"));
        bodyData.goal = cursor.isNull(cursor.getColumnIndex("goal")) ? "" : cursor.getString(cursor.getColumnIndex("goal"));

        cursor.close();

        return bodyData;
    }

    public PhysicalActivity getPhysicalActivity(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM activities WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        @SuppressLint("Range") PhysicalActivity physicalActivity = new PhysicalActivity(
            ActivityType.fromValue(cursor.getString(cursor.getColumnIndex("activity_type"))),
            cursor.getDouble(cursor.getColumnIndex("duration_minutes"))
        );

        cursor.close();

        return physicalActivity;
    }

    public void setPhysicalActivity(PhysicalActivity physicalActivity) {
        SQLiteDatabase db = this.getWritableDatabase();
        PhysicalActivity existingActivity = this.getPhysicalActivity(physicalActivity.userId);

        if (existingActivity == null) {
            String query = "INSERT INTO activities (user_id, activity_type, duration_minutes) VALUES (?, ?, ?)";
            db.execSQL(query, new Object[]{
                physicalActivity.userId,
                physicalActivity.activityType.value,
                physicalActivity.duration
            });
        } else {
            String query = "UPDATE activities SET activity_type = ?, duration_minutes = ? WHERE user_id = ?";

            db.execSQL(query, new Object[]{
                    physicalActivity.activityType.value,
                    physicalActivity.duration,
                    physicalActivity.userId
            });
        }
    }

    public Challenge getChallenge(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM challenges WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        @SuppressLint("Range") Challenge challenge = new Challenge(
            cursor.getInt(cursor.getColumnIndex("pushups")),
            cursor.getInt(cursor.getColumnIndex("squats")),
            cursor.getInt(cursor.getColumnIndex("situps")),
            cursor.getInt(cursor.getColumnIndex("completedPushups")) == 1,
            cursor.getInt(cursor.getColumnIndex("completedSquats")) == 1,
            cursor.getInt(cursor.getColumnIndex("completedSitups")) == 1
        );

        cursor.close();

        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        Challenge existingChallenge = this.getChallenge(challenge.userId);

        if (existingChallenge == null) {
            String query = "INSERT INTO challenges (user_id, pushups, squats, situps, completedPushups, completedSquats, completedSitups) VALUES (?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(query, new Object[]{
                challenge.userId,
                challenge.pushUps,
                challenge.squats,
                challenge.sitUps,
                challenge.completedPushups ? 1 : 0,
                challenge.completedSquats ? 1 : 0,
                challenge.completedSitups ? 1 : 0
            });
        } else {
            String query = "UPDATE challenges SET pushups = ?, squats = ?, situps = ?, completedPushups = ?, completedSquats = ?, completedSitups = ? WHERE user_id = ?";

            db.execSQL(query, new Object[]{
                challenge.pushUps,
                challenge.squats,
                challenge.sitUps,
                challenge.completedPushups ? 1 : 0,
                challenge.completedSquats ? 1 : 0,
                challenge.completedSitups ? 1 : 0,
                challenge.userId
            });
        }
    }
}
