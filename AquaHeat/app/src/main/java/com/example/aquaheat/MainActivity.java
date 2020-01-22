package com.example.aquaheat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button syncTempBtn;
    private Button setButton;
    private TextView currentTemp;
    private TextView lowerBound, upperBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton = (Button) findViewById(R.id.setButton);
        syncTempBtn = (Button) findViewById(R.id.syncTempBtn);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        lowerBound = (TextView) findViewById(R.id.lowerBound);
        upperBound = (TextView) findViewById(R.id.upperBound);

        // Initialize  the bounds text to whatever is the default in the settings activity
        lowerBound.setText(Integer.toString(SettingsActivity2.lowerBoundInt));
        upperBound.setText(Integer.toString(SettingsActivity2.upperBoundInt));

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
                float x = (float) randGenerate(); // generate random number between 0 and 20
                currentTemp.setText(Float.toString(x));

            }

        });
    }


    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity2.class);
        startActivity(intent);
    }

    public int randGenerate() {
        Random rand = new Random();
        int result;
        result = rand.nextInt(20);

        return result;
    }
}
