package com.msu.qimagri;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.msu.qimagri.util.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnLogoutListener, LoginFragment.OnLoginSuccessListener {

    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;
    private final Map<Integer, Fragment> topLevelFragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar = findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        sessionManager = new SessionManager(getApplicationContext());
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        // Initialize top-level fragments
        topLevelFragments.put(R.id.nav_home, new HomeFragment());
        topLevelFragments.put(R.id.nav_pest_and_disease, new PestAndDiseaseFragment());
        topLevelFragments.put(R.id.nav_natural_treatment, new NaturalTreatmentFragment());
        topLevelFragments.put(R.id.nav_help, new HelpFragment());
        topLevelFragments.put(R.id.nav_profile, new ProfileFragment());

        // --- SETUP LISTENERS FIRST ---
        appBar.setNavigationOnClickListener(v -> getSupportFragmentManager().popBackStack());

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            // Show/hide the up arrow based on back stack
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
            }
            invalidateOptionsMenu(); // Sync the menu visibility
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = topLevelFragments.get(item.getItemId());
            if (selectedFragment != null) {
                // Pop all detail screens when switching tabs
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Show the top-level fragment for the selected tab
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame_layout, selectedFragment)
                    .commit();
                return true;
            }
            return false;
        });

        // --- THEN SET INITIAL STATE ---
        if (sessionManager.isLoggedIn()) {
            if (savedInstanceState == null) {
                // Set initial fragment
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
            showBottomNavigationBar(true);
        } else {
            performLogout();
        }

        // Sync UI on initial creation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        if (logoutItem != null) {
            logoutItem.setVisible(sessionManager.isLoggedIn());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            performLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showBottomNavigationBar(boolean show) {
        bottomNavigationView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void performLogout() {
        sessionManager.logoutUser();
        // Clear all fragments and go to Login
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // Replace with LoginFragment
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_frame_layout, new LoginFragment())
            .commit();
        showBottomNavigationBar(false);
    }

    @Override
    public void onLogout() {
        performLogout();
    }

    @Override
    public void onLoginSuccess() {
        // After a successful login, navigate to the HomeFragment
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        showBottomNavigationBar(true);
    }
}
