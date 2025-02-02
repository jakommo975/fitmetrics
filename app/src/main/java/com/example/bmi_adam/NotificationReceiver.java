package com.example.bmi_adam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent to open MainActivity when the notification is clicked
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        Notification notification = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.fitmetrics_logo) // Notification icon
                .setContentTitle("Denná výzva") // Notification title
                .setContentText("Nezabudnite na svoje kliky, drepy a brušáky!") // Notification message
                .setAutoCancel(true) // Removes the notification when clicked
                .setContentIntent(pendingIntent) // Opens MainActivity on click
                .build();

        // Show the notification
        notificationManager.notify(1, notification);
    }
}
