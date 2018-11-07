package com.qteam.saigonjams.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.qteam.saigonjams.fragment.MapFragment;
import com.qteam.saigonjams.fragment.NotificationsFragment;
import com.qteam.saigonjams.fragment.SettingsFragment;
import com.qteam.saigonjams.fragment.SharingFragment;
import com.qteam.saigonjams.R;

public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new NotificationsFragment();
    final Fragment fragment2 = new SharingFragment();
    final Fragment fragment3 = new MapFragment();
    final Fragment fragment4 = new SettingsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    public static BottomNavigationView mainNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainNav = findViewById(R.id.navigation_bar);
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_notifications:
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .hide(active)
                                .show(fragment1)
                                .commit();
                        active = fragment1;
                        return true;

                    case R.id.nav_sharing:
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .hide(active)
                                .show(fragment2)
                                .commit();
                        active = fragment2;
                        return true;

                    case R.id.nav_map:
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .hide(active)
                                .show(fragment3)
                                .commit();
                        active = fragment3;
                        return true;

                    case R.id.nav_settings:
                        fm.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .hide(active)
                                .show(fragment4)
                                .commit();
                        active = fragment4;
                        return true;
                }
                return false;
            }
        });

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
    }

}
