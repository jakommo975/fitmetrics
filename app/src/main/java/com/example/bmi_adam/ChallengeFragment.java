package com.example.bmi_adam;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmi_adam.data.database.DatabaseHelper;
import com.example.bmi_adam.models.Challenge;
import com.example.bmi_adam.models.Exercise;



import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeFragment extends Fragment {
    DatabaseHelper databaseHelper;
    View view;

    public ChallengeFragment() {
        // Required empty public constructor
    }
    public static ChallengeFragment newInstance(String param1, String param2) {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.databaseHelper = new DatabaseHelper(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        LinearLayout challengeLayout = view.findViewById(R.id.challengeLayout);
        CardView challengeCardView = view.findViewById(R.id.challengeCompletedCard);

        challengeCardView.setVisibility(View.GONE);
        challengeLayout.setVisibility(View.GONE);

        EditText pushupsField = view.findViewById(R.id.pushupsField);
        EditText squatsField = view.findViewById(R.id.squatsField);
        EditText situpsField = view.findViewById(R.id.situpsField);

        TextView pushupsTextView = view.findViewById(R.id.pushupsText);
        TextView squatsTextView = view.findViewById(R.id.squatsText);
        TextView situpsTextView = view.findViewById(R.id.situpsText);

        Button saveButton = view.findViewById(R.id.setChallengeButton);

        Challenge challenge = databaseHelper.getChallenge(1);

        if (challenge != null) {
            pushupsField.setText(String.valueOf(challenge.pushUps));
            squatsField.setText(String.valueOf(challenge.squats));
            situpsField.setText(String.valueOf(challenge.sitUps));

            pushupsTextView.setText(challenge.toText(Exercise.PUSHUPS));
            squatsTextView.setText(challenge.toText(Exercise.SQUATS));
            situpsTextView.setText(challenge.toText(Exercise.SITUPS));

            challengeLayout.setVisibility(View.VISIBLE);

            if (challenge.getIsCompleted()) {
                challengeCardView.setVisibility(View.VISIBLE);
            }
        }

        saveButton.setOnClickListener(v -> {
            String pushupsString = pushupsField.getText().toString();
            String squatsString = squatsField.getText().toString();
            String situpsString = situpsField.getText().toString();

            if (pushupsString.isEmpty() || squatsString.isEmpty() || situpsString.isEmpty()) {
                Toast.makeText(getContext(), "Vyplňte všetky polia.", Toast.LENGTH_SHORT).show();
                return;
            }

            int pushups = Integer.parseInt(pushupsString);
            int squats = Integer.parseInt(squatsString);
            int situps = Integer.parseInt(situpsString);

            Challenge newChallenge = new Challenge(pushups, squats, situps, false, false, false);

            pushupsTextView.setText(newChallenge.toText(Exercise.PUSHUPS));
            squatsTextView.setText(newChallenge.toText(Exercise.SQUATS));
            situpsTextView.setText(newChallenge.toText(Exercise.SITUPS));

            this.resetCheckBoxes();

            databaseHelper.setChallenge(newChallenge);

            challengeLayout.setVisibility(View.VISIBLE);
            challengeCardView.setVisibility(View.GONE);

            this.scheduleNotification();
        });



        CheckBox pushupsCheckbox = view.findViewById(R.id.pushupsCheckBox);
        CheckBox squatsCheckbox = view.findViewById(R.id.squatsCheckBox);
        CheckBox situpsCheckbox = view.findViewById(R.id.situpsCheckBox);

        pushupsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.onCheckboxChange(Exercise.PUSHUPS, isChecked);
        });

        squatsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.onCheckboxChange(Exercise.SQUATS, isChecked);
        });

        situpsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
           this.onCheckboxChange(Exercise.SITUPS, isChecked);
        });
    }

    private void resetCheckBoxes() {
        CheckBox pushupsCheckbox = view.findViewById(R.id.pushupsCheckBox);
        CheckBox squatsCheckbox = view.findViewById(R.id.squatsCheckBox);
        CheckBox situpsCheckbox = view.findViewById(R.id.situpsCheckBox);

        pushupsCheckbox.setChecked(false);
        squatsCheckbox.setChecked(false);
        situpsCheckbox.setChecked(false);
    }

    private void onCheckboxChange(Exercise exercise, boolean isChecked) {
        Challenge challenge = databaseHelper.getChallenge(1);
        assert challenge != null;

        challenge.setCompletedState(exercise, isChecked);

        databaseHelper.setChallenge(challenge);

        if (challenge.getIsCompleted()) {
            this.view.findViewById(R.id.challengeCompletedCard).setVisibility(View.VISIBLE);
        } else {
            this.view.findViewById(R.id.challengeCompletedCard).setVisibility(View.GONE);
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification() {
        Context context = this.requireContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();

        Calendar tenAM = Calendar.getInstance();
        Calendar sixPM = Calendar.getInstance();

        tenAM.set(Calendar.HOUR_OF_DAY, 10);
        tenAM.set(Calendar.MINUTE, 0);
        tenAM.set(Calendar.SECOND, 0);

        sixPM.set(Calendar.HOUR_OF_DAY, 18);
        sixPM.set(Calendar.MINUTE, 0);
        sixPM.set(Calendar.SECOND, 0);


        if (calendar.after(tenAM)) {
            tenAM.add(Calendar.DATE, 1);
        }

        if (calendar.after(sixPM)) {
            sixPM.add(Calendar.DATE, 1);
        }


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, tenAM.getTimeInMillis(), pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, sixPM.getTimeInMillis(), pendingIntent);

        // set notification 10s from now for testing
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
    }
}