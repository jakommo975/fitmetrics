package com.example.bmi_adam;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bmi_adam.data.database.DatabaseHelper;
import com.example.bmi_adam.models.BodyData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BodyDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BodyDataFragment extends Fragment {
    LinearLayout bmiCardLayout;
    CardView bmiCardView;
    DatabaseHelper databaseHelper;

    EditText weightField;
    EditText heightField;
    EditText ageField;
    RadioGroup genderGroup;
    RadioGroup goalGroup;

    @Nullable
    BodyData bodyData;

    public BodyDataFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static BodyDataFragment newInstance(String param1, String param2) {
        BodyDataFragment fragment = new BodyDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.databaseHelper = new DatabaseHelper(this.getContext());
        this.bodyData = this.databaseHelper.getBodyData(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.bmiCardLayout = view.findViewById(R.id.bmiCardLayout);
        this.bmiCardView = view.findViewById(R.id.bmiCardView);

        this.weightField = view.findViewById(R.id.weightField);
        this.heightField = view.findViewById(R.id.heightField);
        this.ageField = view.findViewById(R.id.ageField);
        this.genderGroup = view.findViewById(R.id.genderGroup);
        this.goalGroup = view.findViewById(R.id.goalGroup);

        assert this.bodyData != null;

        this.setBmiCard(bodyData.calculateBmi());

        this.weightField.setText(String.valueOf(this.bodyData.getWeightString()));
        this.heightField.setText(String.valueOf(this.bodyData.getHeightString()));
        this.ageField.setText(String.valueOf(this.bodyData.getAgeString()));
        this.genderGroup.check(this.bodyData.getGenderRadioId());
        this.goalGroup.check(this.bodyData.getGoalRadioId());



        // Find the button by its ID
        Button button = view.findViewById(R.id.sendButton);

        // Set a click listener on the button
        button.setOnClickListener(v -> {
            int checkedGenderId = genderGroup.getCheckedRadioButtonId();
            int checkedGoalId = goalGroup.getCheckedRadioButtonId();

            String ageText = ageField.getText().toString();
            String weightText = weightField.getText().toString();
            String heightText = heightField.getText().toString();

            if (
                ageText.isEmpty()
                || heightText.isEmpty()
                || weightText.isEmpty()
                || checkedGenderId == -1
                || checkedGoalId == -1
            ) {

                return;
            }

            double weight = Double.parseDouble(weightText);
            double height = Double.parseDouble(heightText);
            int age = Integer.parseInt(ageText);


            bodyData.weight = weight;
            bodyData.height = height;
            bodyData.age = age;

            bodyData.setGenderByRadioId(checkedGenderId);
            bodyData.setGoalByRadioId(checkedGoalId);

            this.databaseHelper.setBodyData(bodyData);

            this.setBmiCard(bodyData.calculateBmi());
        });
    }

    @SuppressLint("DefaultLocale")
    private void setBmiCard(Double bmi) {
        if (bmi == -1) {
            this.bmiCardView.setVisibility(View.INVISIBLE);
        } else {
            this.bmiCardView.setVisibility(View.VISIBLE);
        }

        TextView bmiResult = this.bmiCardLayout.findViewById(R.id.bmiResult);
        // Set the text of the TextView. Format to two decimal places
        bmiResult.setText(String.format("%.2f", bmi));

        TextView bmiDescription = this.bmiCardLayout.findViewById(R.id.bmiDescription);

        if (bmi < 18.5) {
            this.changeTextViewColors(this.bmiCardLayout, Color.parseColor("#0000ff"));
            this.bmiCardLayout.setBackgroundColor(Color.parseColor("#cce5ff"));
            bmiDescription.setText("podváha");
        } else if (bmi < 24.9) {
            this.changeTextViewColors(this.bmiCardLayout, Color.parseColor("#008000"));
            this.bmiCardLayout.setBackgroundColor(Color.parseColor("#ccffcc"));// Overweight
            bmiDescription.setText("správna hmotnosť");
        } else {
            this.changeTextViewColors(this.bmiCardLayout, Color.parseColor("#ff0000"));
            this.bmiCardLayout.setBackgroundColor(Color.parseColor("#ffcccc"));
            bmiDescription.setText("nadváha");
        }
    }

    private void changeTextViewColors(ViewGroup root, int color) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);

            // If the child is a TextView, change its text color
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            } else if (child instanceof ViewGroup) {
                // If the child is a ViewGroup, recursively call this method
                changeTextViewColors((ViewGroup) child, color);
            }
        }
    }
}