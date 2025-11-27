// Package Name
package com.msu.qimagri;
// Import libraries
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Main class
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity main is the parent of all layout
        setContentView(R.layout.activity_main);
        Toolbar appBar = findViewById(R.id.app_bar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Home");

        // Set bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        // Different layouts using Fragment
        HomeFragment homeFragment = new HomeFragment();
        PestAndDiseaseFragment pestAndDiseaseFragment = new PestAndDiseaseFragment();
        NaturalTreatmentFragment naturalTreatmentFragment = new NaturalTreatmentFragment();
        HelpFragment helpFragment = new HelpFragment();
        setCurrentFragment(homeFragment);

        appBar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                int selectedItemId = bottomNavigationView.getSelectedItemId();
                if (selectedItemId == R.id.nav_home) {
                    getSupportActionBar().setTitle("Home");
                } else if (selectedItemId == R.id.nav_pest_and_disease) {
                    getSupportActionBar().setTitle("Pests and Diseases");
                } else if (selectedItemId == R.id.nav_natural_treatment) {
                    getSupportActionBar().setTitle("Natural Treatments");
                } else if (selectedItemId == R.id.nav_help) {
                    getSupportActionBar().setTitle("Help");
                }
            }
        });
        // Will detect every item that users click
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_home) {
                setCurrentFragment(homeFragment);
                getSupportActionBar().setTitle("Home");
            } else if (itemId == R.id.nav_pest_and_disease) {
                setCurrentFragment(pestAndDiseaseFragment);
                getSupportActionBar().setTitle("Pests and Diseases");
            } else if (itemId == R.id.nav_natural_treatment) {
                setCurrentFragment(naturalTreatmentFragment);
                getSupportActionBar().setTitle("Natural Treatments");
            } else if (itemId == R.id.nav_help) {
                setCurrentFragment(helpFragment);
                getSupportActionBar().setTitle("Help");
            } else {
                System.out.println("ERROR!");
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
    }
    // Set current fragment of the time
    private void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame_layout, fragment).commit();
    }
}
