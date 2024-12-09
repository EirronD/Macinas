package com.example.bahi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class Terms_Conditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        // Initialize the TextView
        TextView textView = findViewById(R.id.termsConditions);

        try {
            // Open the text file from the assets folder
            InputStream inputStream = getAssets().open("terms_and_conditions.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convert the buffer into a String and set it to the TextView
            String longText = new String(buffer, "UTF-8");
            textView.setText(longText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
