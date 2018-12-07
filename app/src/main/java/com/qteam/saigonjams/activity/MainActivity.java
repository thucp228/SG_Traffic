package com.qteam.saigonjams.activity;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.messaging.FirebaseMessaging;
import com.qteam.saigonjams.fragment.MapFragment;
import com.qteam.saigonjams.fragment.NotificationsFragment;
import com.qteam.saigonjams.fragment.SettingsFragment;
import com.qteam.saigonjams.fragment.SharingFragment;
import com.qteam.saigonjams.R;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView mainNav;
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(notificationsFragment);

        FirebaseMessaging.getInstance().subscribeToTopic("notifications");

        mainNav = findViewById(R.id.navigation_bar);
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_notifications:
                        if (!item.isChecked()) {
                            fragment = new NotificationsFragment();
                            setFragment(fragment);
                        }
                        return true;

                    case R.id.nav_sharing:
                        if (!item.isChecked()) {
                            fragment = new SharingFragment();
                            setFragment(fragment);
                        }
                        return true;

                    case R.id.nav_map:
                        if (!item.isChecked()) {
                            fragment = new MapFragment();
                            setFragment(fragment);
                        }
                        return true;

                    case R.id.nav_settings:
                        if (!item.isChecked()) {
                            fragment = new SettingsFragment();
                            setFragment(fragment);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

}
