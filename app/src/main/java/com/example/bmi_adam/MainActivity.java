package com.example.bmi_adam;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bmi_adam.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    ArrayList<HomePageItem> homePageItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.createNotificationChannel();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        homePageItemArrayList = new ArrayList<>();

        String text2 = "Najprv zadáš do aplikácie svoje údaje. "
                + "Na ich základe sa vypočíta tvoje BMI. "
                + "Potom si nastavíš svoje ciele a môžeš pravidelne merať svoju dennú aktivitu.";

        String text3 = "Môžeš si nastaviť aj svoju dennú výzvu, "
                + "na ktorej splnenie ťa aplikácia pravidelne upozorní.";

        homePageItemArrayList.add(new HomePageItem("Vitaj v aplikácii Fitmetrics",false, null));
        homePageItemArrayList.add(new HomePageItem(text2, false, null));
        homePageItemArrayList.add(new HomePageItem(text3, true, v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }));

        // Set up the adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(homePageItemArrayList);
        viewPager.setAdapter(adapter);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "my_channel_id", // Unique ID for the channel
                    "Scheduled Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}