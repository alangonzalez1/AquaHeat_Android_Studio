package com.example.aquaheat;

import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    static int lowerBoundIntF = 65, upperBoundIntF = 85; // in Fahrenheit
    static int lowerBoundIntC, upperBoundIntC; // in Celsius
    private Button setButton;
    private TextView currentTemp;
    private TextView lowerBound, upperBound;
    private TextView tempUnits1, tempUnits2, tempUnits3;
    static Dweet dweet;
    static String thingName = "AquaHeat";
    float avrF; // the average of the Fahrenheit temperature bounds
    float avrC; // the average of the Celsius temperature bounds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton = (Button) findViewById(R.id.setButton);
        //syncTempBtn = (Button) findViewById(R.id.syncTempBtn);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        lowerBound = (TextView) findViewById(R.id.lowerBound);
        upperBound = (TextView) findViewById(R.id.upperBound);
        tempUnits1 = (TextView) findViewById(R.id.tempUnits1);
        tempUnits2 = (TextView) findViewById(R.id.tempUnits2);
        tempUnits3 = (TextView) findViewById(R.id.tempUnits3);
        avrF = average(lowerBoundIntF, upperBoundIntF);
        avrC = average(lowerBoundIntC, upperBoundIntC);

        // sync the app upon opening it - to be added later


        // get the Celsius equivalent values of the upper and lower bounds
        lowerBoundIntC = (int) FtoC((float)lowerBoundIntF);
        upperBoundIntC = (int) FtoC((float)upperBoundIntF);

        // convert the value if it needs to be converted
        if(SettingsActivity2.cFlag == 1) {
            // the Celsius flag is set

            // update the main screen to reflect the Celsius upper and lower bounds
            lowerBound.setText(Integer.toString(lowerBoundIntC));
            upperBound.setText(Integer.toString(upperBoundIntC));
            tempUnits1.setText("\u2103");
            tempUnits2.setText("\u2103");
            tempUnits3.setText("\u2103");

        }

        else{
            // the Celsius flag is not set

            // update the main screen to reflect the Fahrenheit upper and lower bounds
            lowerBound.setText(Integer.toString(lowerBoundIntF));
            upperBound.setText(Integer.toString(upperBoundIntF));
            tempUnits1.setText("\u2109");
            tempUnits2.setText("\u2109");
            tempUnits3.setText("\u2109");
        }
        // Initialize  the bounds text to whatever is the default in the settings activity


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        thread notificationThread = new thread();
        new Thread(notificationThread).start();
    }

    class thread extends Thread {
        public void run() {
            float tempF = 0;
            float tempC = 0;
            while (true) {
                try {
                    dweet = DweetIO.getLatestDweet(thingName);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                if (dweet != null) {
                    String string = dweet.getContent().toString();
                    String[] splitString = string.split(":");
                    String[] splitString2 = splitString[1].split(":");
                    String[] splitString3 = splitString2[0].split(",");
                    String tempFstring = splitString3[0];
                    tempF = Float.parseFloat(tempFstring);
                    tempC = FtoC(tempF);

                }
                // format the floats to one decimal place
                DecimalFormat decimalFormat = new DecimalFormat("#.0");
                String outputF = decimalFormat.format(tempF);
                String outputC = decimalFormat.format(tempC);
                Log.d(TAG, outputF); // display the Fahrenheit value fetched
                Log.d(TAG, outputC); // display the Celsius version of the fetched value
                final String finalTempF = outputF;
                final String finalTempC = outputC;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(SettingsActivity2.cFlag == 1){
                            // Celsius flag is on
                            // display the Celsius value instead
                            currentTemp.setText(finalTempC);
                        }
                        else{
                            // Celsius flag is not on
                            currentTemp.setText(finalTempF);
                        }
                    }
                });
                //CHECK FOR OUT-OUT-BOUNDS
                notificationCall(tempF, lowerBoundIntF, upperBoundIntF);

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {}

                //SEND UPPER AND LOWER BOUNDS
                JsonObject json= new JsonObject();
                json.addProperty("tempMin", lowerBoundIntF);
                json.addProperty("tempMax", upperBoundIntF);
                try {
                    DweetIO.publish("AquaHeatSettings", json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {}
            }
        }
    }

    public void notificationCall(float value, int lowerBound, int upperBound) {
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationBuilder.setSmallIcon(R.drawable.ic_warning);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_warning));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (value <= (float)lowerBound) {
            notificationBuilder.setContentTitle("Notification from AquaHeat");
            notificationBuilder.setContentText("The temperature of your fish tank is too low!");
            notificationManager.notify(1, notificationBuilder.build());
        }
        if (value >= (float)upperBound) {
            notificationBuilder.setContentTitle("Notification from AquaHeat");
            notificationBuilder.setContentText("The temperature of your fish tank is too high!");
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity2.class);
        startActivity(intent);
    }

    public int randGenerate() {
        Random rand = new Random();
        int result;
        result = rand.nextInt(30);

        return result;
    }

    public float average(int num1, int num2) {
        return (((float) num1 + (float) num2) / 2);
    }

    public double CtoF(int num) {
        return ((1.8 * (float) num) + 32.0);
    }

    public float FtoC(float num) {
        return (float) ((num - 32.0) * (5.0/9.0));
    }
}
