package com.example.aquaheat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SettingsActivity2 extends AppCompatActivity {

    static int lowerBoundInt = 45, upperBoundInt = 60;
    private EditText uBndInput;
    private EditText lBndInput;
    private Button applyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        // insert code here
        uBndInput = (EditText) findViewById(R.id.uBndInput);
        lBndInput = (EditText) findViewById(R.id.lBndInput);
        applyBtn = (Button) findViewById(R.id.applyBtn);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code for what the button will do
                lowerBoundInt = Integer.valueOf(lBndInput.getText().toString());
                upperBoundInt = Integer.valueOf(uBndInput.getText().toString());

                openMain();

            }
        });
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
