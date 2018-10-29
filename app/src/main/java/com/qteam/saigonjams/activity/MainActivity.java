package com.qteam.saigonjams.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.qteam.saigonjams.fragment.SettingsFragment;
import com.qteam.saigonjams.fragment.NotificationsFragment;
import com.qteam.saigonjams.fragment.SharingFragment;
import com.qteam.saigonjams.fragment.MapFragment;
import com.qteam.saigonjams.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private NotificationsFragment notificationsFragment;
    private SharingFragment sharingFragment;
    private MapFragment mapFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainNav = findViewById(R.id.navigation_bar);

        notificationsFragment = new NotificationsFragment();
        sharingFragment = new SharingFragment();
        mapFragment = new MapFragment();
        settingsFragment = new SettingsFragment();

        setFragment(notificationsFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_notifications:
                        setFragment(notificationsFragment);
                        return true;

                    case R.id.nav_car:
                        setFragment(sharingFragment);
                        return true;

                    case R.id.nav_map:
                        setFragment(mapFragment);
                        return true;

                    case R.id.nav_settings:
                        setFragment(settingsFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}
