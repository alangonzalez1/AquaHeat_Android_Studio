package com.example.aquaheat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button syncTempBtn;
    private Button setButton;
    private TextView currentTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton = (Button) findViewById(R.id.setButton);
        syncTempBtn = (Button) findViewById(R.id.syncTempBtn);
        currentTemp = (TextView) findViewById(R.id.currentTemp);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        syncTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // program the button to do something here
                int x = randGenerate(); // generate random number between 0 and 20
                currentTemp.setText(Integer.toString(x));

            }

        });
    }


    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public int randGenerate() {
        Random rand = new Random();
        int result;
        result = rand.nextInt(20);

        return result;
    }
}
