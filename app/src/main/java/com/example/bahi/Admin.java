package com.example.bahi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bahi.databinding.ActivityAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin extends AppCompatActivity {

    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String accountType = getIntent().getStringExtra("accountType");

        Bundle bundle = new Bundle();
        bundle.putString("accountType", accountType);

        Home_Bottom homeBottomFragment = new Home_Bottom();
        homeBottomFragment.setArguments(bundle);

        replaceFragment(homeBottomFragment);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.bottom_home_landlord) {
                selectedFragment = new Home_Bottom();
                selectedFragment.setArguments(bundle);
            } else if (item.getItemId() == R.id.bottom_listing_landlord) {
                selectedFragment = new Listing();
            } else if (item.getItemId() == R.id.bottom_profile_landlord) {
                selectedFragment = new Profile();
            } else if (item.getItemId() == R.id.bottom_menu_landlord) {
                selectedFragment = new Menu();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, fragment);
        fragmentTransaction.commit();
    }
}
