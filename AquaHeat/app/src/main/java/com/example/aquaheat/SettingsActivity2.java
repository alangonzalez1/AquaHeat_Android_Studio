package com.example.aquaheat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;


public class SettingsActivity2 extends AppCompatActivity {

    static int cFlag; // celsius flag
    private EditText uBndInput; // input for the upper bound value
    private EditText lBndInput; // input for the lower bound value
    private Switch Celsius; // switch to turn on the Celsius values
    private Button applyBtn; // button to apply the changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        // insert code here
        uBndInput = (EditText) findViewById(R.id.uBndInput);
        lBndInput = (EditText) findViewById(R.id.lBndInput);
        applyBtn = (Button) findViewById(R.id.applyBtn);
        Celsius = (Switch) findViewById(R.id.Celsius);


        // save switch state in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        Celsius.setChecked(sharedPreferences.getBoolean("value", false));

        Celsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what the switch will do
                if(Celsius.isChecked())
                {
                    // when the switch is checked
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    Celsius.setChecked(true);
                    cFlag = 1; // toggle the celsius flag
                }
                else
                {
                    // when the switch is not checked
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    Celsius.setChecked(false);
                    cFlag = 0; // turn off the celsius flag
                }
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code for what the "apply changes" button will do

                // update the upper and lower bound variables if there is any user input
                // do nothing if they are empty
                if(TextUtils.isEmpty(uBndInput.getText().toString())) {
                }
                else
                    MainActivity.upperBoundIntF = Integer.valueOf(uBndInput.getText().toString());

                if(TextUtils.isEmpty(lBndInput.getText().toString())){
                }
                else
                    MainActivity.lowerBoundIntF = Integer.valueOf(lBndInput.getText().toString());

                // done updating, now get back to the main screen
                openMain();

            }


        });
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
