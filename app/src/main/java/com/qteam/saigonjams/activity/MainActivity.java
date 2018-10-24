package com.qteam.saigonjams.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.qteam.saigonjams.fragment.AboutFragment;
import com.qteam.saigonjams.fragment.AlertFragment;
import com.qteam.saigonjams.fragment.CarFragment;
import com.qteam.saigonjams.fragment.MapFragment;
import com.qteam.saigonjams.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private AlertFragment alertFragment;
    private CarFragment carFragment;
    private MapFragment mapFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainNav = findViewById(R.id.main_nav);

        alertFragment = new AlertFragment();
        carFragment = new CarFragment();
        mapFragment = new MapFragment();
        aboutFragment = new AboutFragment();

        setFragment(alertFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_notifications:
                        mainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(alertFragment);
                        return true;

                    case R.id.nav_car:
                        mainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(carFragment);
                        return true;

                    case R.id.nav_map:
                        mainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(mapFragment);
                        return true;

                    case R.id.nav_settings:
                        mainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(aboutFragment);
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
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
