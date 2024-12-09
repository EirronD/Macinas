package com.example.bahi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.bahi.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String accountType = getIntent().getStringExtra("accountType");

        Bundle bundle = new Bundle();
        bundle.putString("accountType", accountType);

        Home_Bottom homeBottomFragment = new Home_Bottom();
        homeBottomFragment.setArguments(bundle);

        replaceFragment(homeBottomFragment);

        // Set up navigation item selection listener
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            if (item.getItemId() == R.id.bottom_home) {
                selectedFragment = new Home_Bottom();
                selectedFragment.setArguments(bundle);
            } else if (item.getItemId() == R.id.bottom_favorite) {
                replaceFragment(new Favorite());
            } else if (item.getItemId() == R.id.bottom_profile) {
                replaceFragment(new Profile());
            } else if (item.getItemId() == R.id.bottom_menu) {
                replaceFragment(new Menu());
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, fragment);
        fragmentTransaction.commit();
    }
}


