package org.code.parentsplashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.code.parentsplashscreen.storage.SharedPreferencesManager;
import org.code.parentsplashscreen.ui.ChildrenFragment;
import org.code.parentsplashscreen.ui.DriverFragment;
import org.code.parentsplashscreen.ui.TrackingFragment;
import org.code.parentsplashscreen.ui.UserProfile;
import org.codeforiraq.parentsplashscreen.R;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation meowBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        meowBottomNavigation = findViewById(R.id.bottom_bar);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.tracking_location));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.children));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.driver));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.user_profile));


        meowBottomNavigation.show(1, true);
        replace(new TrackingFragment());
        meowBottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    replace(new TrackingFragment());
                    break;
                case 2:
                    replace(new ChildrenFragment());
                    break;
                case 3:
                    replace(new DriverFragment());
                    break;
                case 4:
                    replace(new UserProfile());
                    break;
            }
            return null;
        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LogIn.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}