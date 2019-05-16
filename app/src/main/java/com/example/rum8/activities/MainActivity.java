package com.example.rum8.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rum8.R;
import com.example.rum8.controllers.MainController;
import com.example.rum8.listeners.MainControllerListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements MainControllerListener {

    private MainController controller;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initController();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        //Creates the menu inside of the toolbar
        getMenuInflater().inflate(R.menu.dropdown_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_go_to_profile_settings:
                controller.onGoToProfileSettingsButtonClicked();
                return true;
            case R.id.main_activity_log_out:
                controller.onLogOutButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void goToProfileSettings() {
        final Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToLogin() {
        final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {
    }

    private void initController() {
        controller = new MainController(this);
    }

}
