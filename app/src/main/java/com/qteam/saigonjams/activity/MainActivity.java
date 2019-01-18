package com.qteam.saigonjams.activity;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.qteam.saigonjams.fragment.MapFragment;
import com.qteam.saigonjams.fragment.NotificationsFragment;
import com.qteam.saigonjams.fragment.SettingsFragment;
import com.qteam.saigonjams.fragment.SharingFragment;
import com.qteam.saigonjams.R;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView mainNav;
    public static Context context;
    public static LocationManager locationManager;
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(notificationsFragment);

        context = getApplicationContext();

        FirebaseMessaging.getInstance().subscribeToTopic("notifications")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Succeed";
                if (!task.isSuccessful())
                    msg = "Failed";

                Log.d("SUBSCRIBE", msg);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
