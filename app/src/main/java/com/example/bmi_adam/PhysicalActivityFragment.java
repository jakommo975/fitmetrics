package com.example.bmi_adam;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmi_adam.data.database.DatabaseHelper;
import com.example.bmi_adam.models.ActivityType;
import com.example.bmi_adam.models.BodyData;
import com.example.bmi_adam.models.PhysicalActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhysicalActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhysicalActivityFragment extends Fragment {

    DatabaseHelper databaseHelper;
    TextView caloriesBurnedTextView;
    TextView recommendedCaloriesTextView;
    View view;


    public PhysicalActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhysicalActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhysicalActivityFragment newInstance(String param1, String param2) {
        PhysicalActivityFragment fragment = new PhysicalActivityFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.databaseHelper = new DatabaseHelper(this.getContext());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        Spinner activitySpinner = view.findViewById(R.id.activitySpinner);
        EditText durationField = view.findViewById(R.id.durationField);
        LinearLayout caloriesLayout = view.findViewById(R.id.caloriesLayout);

        caloriesLayout.setVisibility(View.INVISIBLE);

        ArrayList<ActivityType> activities = new ArrayList<>();

        activities.add(ActivityType.NONE);
        activities.add(ActivityType.SWIMMING);
        activities.add(ActivityType.RUNNING);
        activities.add(ActivityType.EXERCISING);
        activities.add(ActivityType.CYCLING);

        ArrayAdapter<ActivityType> adapter = new ArrayAdapter<>(
                this.getContext(),
                R.layout.spinner_layout,
                activities
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);

        Button button = view.findViewById(R.id.sendButton);
        this.caloriesBurnedTextView = view.findViewById(R.id.caloriesBurned);
        this.recommendedCaloriesTextView = view.findViewById(R.id.recommendedCalories);

        BodyData bodyData = this.databaseHelper.getBodyData(1);
        PhysicalActivity physicalActivity = this.databaseHelper.getPhysicalActivity(1);

        if (physicalActivity != null) {
            activitySpinner.setSelection(activities.indexOf(physicalActivity.activityType));
            durationField.setText(String.valueOf(physicalActivity.duration));


            this.calculateRecommendedCalories(bodyData, physicalActivity.activityType, physicalActivity.duration);

            this.setCaloriesTexts(
                    this.calculateCaloriesBurned(physicalActivity.activityType, bodyData.weight, physicalActivity.duration),
                    this.calculateRecommendedCalories(bodyData, physicalActivity.activityType, physicalActivity.duration)
            );

            caloriesLayout.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(v -> {
            ActivityType activity = activities.get(activitySpinner.getSelectedItemPosition());
            String durationText = durationField.getText().toString();

            if (durationText.isEmpty()) {
                Toast.makeText(getContext(), "Vyplňte trvanie", Toast.LENGTH_SHORT).show();
                return;
            }

            double duration = Double.parseDouble(durationField.getText().toString());

            double caloriesBurned = this.calculateCaloriesBurned(activity, bodyData.weight, duration);
            double recommendedCalories = this.calculateRecommendedCalories(bodyData, activity, duration);

            this.setCaloriesTexts(caloriesBurned, recommendedCalories);

            PhysicalActivity newPhysicalActivity = new PhysicalActivity(activity, duration);

            this.databaseHelper.setPhysicalActivity(newPhysicalActivity);
            caloriesLayout.setVisibility(View.VISIBLE);
        });
    }

    private void setCaloriesTexts(double caloriesBurned, double recommendedCalories) {
        String caloriesBurnedString = ((int) caloriesBurned)  + " kcal";
        String recommendedCaloriesString = ((int) recommendedCalories) + " kcal";

        this.caloriesBurnedTextView.setText(caloriesBurnedString);
        this.recommendedCaloriesTextView.setText(recommendedCaloriesString);
    }

    private double calculateRecommendedCalories(BodyData bodyData, ActivityType activityType, double minutes) {
        double caloriesBurned = this.calculateCaloriesBurned(activityType, bodyData.weight, minutes);

        double tdee = this.calculateTDEE(
                bodyData,
                caloriesBurned
        );

        if (bodyData.goal.equals("gain")) return tdee + 300;
        if (bodyData.goal.equals("loose")) return tdee - 300;

        return tdee;
    }
    private double calculateTDEE(BodyData bodyData, double caloriesBurned) {
        double bmr = bodyData.gender.equals("M")
                ? 10 * bodyData.weight + 6.25 * bodyData.height - 5 * bodyData.age + 5
                : 10 * bodyData.weight + 6.25 * bodyData.height - 5 * bodyData.age - 161;

        double baseTDEE = bmr * 1.55;

        return baseTDEE + caloriesBurned;
    }
    private double calculateCaloriesBurned(ActivityType activityType, double userWeight, double minutes) {
        if (activityType.equals(ActivityType.NONE)) {
            return 0; // No calories burned for "No activity"
        }

        return activityType.met * userWeight * (minutes / 60); // Use user's weight for calculation
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_physical_activity, container, false);
    }
}