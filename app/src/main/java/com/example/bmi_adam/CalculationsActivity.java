package com.example.bmi_adam;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bmi_adam.databinding.ActivityCalculationsBinding;
import com.google.android.material.tabs.TabLayout;

public class CalculationsActivity extends AppCompatActivity {

    private ActivityCalculationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalculationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Load the default fragment (Tab 1)
        loadFragment(new BodyDataFragment());

        tabLayout.addTab(
                tabLayout.newTab().setText("Telesné údaje").setIcon(R.drawable.body_data)
        );
        tabLayout.addTab(
                tabLayout.newTab().setText("Fyzická activita").setIcon(R.drawable.physical_activity)
        );
        tabLayout.addTab(
                tabLayout.newTab().setText("Denná výzva").setIcon(R.drawable.challenge)
        );

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new BodyDataFragment();
                        break;
                    case 1:
                        selectedFragment = new PhysicalActivityFragment();
                        break;
                    case 2:
                        selectedFragment = new ChallengeFragment();
                        break;
                }

                // Load the selected fragment
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Handle unselected state
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle reselected state
            }
        });

    }

    // Helper method to load a fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }


}