package com.qteam.saigonjams;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private AlertFragment alertFragment;
    private CarFragment carFragment;
    private MapFragment mapFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainNav = findViewById(R.id.main_nav);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        disableShiftMode(mainNav);

        mainFrame = findViewById(R.id.main_frame);

        alertFragment = new AlertFragment();
        carFragment = new CarFragment();
        mapFragment = new MapFragment();
        settingFragment = new SettingFragment();

        setFragment(alertFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_alert:
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

                    case R.id.nav_setting:
                        mainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(settingFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to change value of shift mode");
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
