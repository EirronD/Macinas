package com.example.bahi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Home_Bottom extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home__bottom, container, false);

        Bundle args = getArguments();

        if (args != null) {
            final String accountType = args.getString("accountType", "");

            ImageView apartmentClick = view.findViewById(R.id.apartment_click);

            apartmentClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Apartments.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            TextView apartmentText = view.findViewById(R.id.apartment_text);

            apartmentText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Apartments.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            ImageView condoClick = view.findViewById(R.id.condo_click);

            condoClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Condo.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            TextView condoText = view.findViewById(R.id.condo_text);

            condoText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Condo.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            ImageView houseClick = view.findViewById(R.id.house_click);

            houseClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), House.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            TextView houseText = view.findViewById(R.id.house_text);

            houseText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), House.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            ImageView bedspaceClick = view.findViewById(R.id.bedspace_click);

            bedspaceClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Bedspace.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });

            TextView bedspaceText = view.findViewById(R.id.bedsapce_text);

            bedspaceText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Bedspace.class);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            });
        }
        return view;

    }
}