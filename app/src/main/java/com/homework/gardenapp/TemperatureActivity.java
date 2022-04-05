package com.homework.gardenapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

//From Android Developer Tools Guides
public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor temperature;
    Plant userPlant = new Plant();

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        Intent i = getIntent();
        if (i.getSerializableExtra("userPlant") != null) {
            userPlant = (Plant) i.getSerializableExtra("userPlant");
        }

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        //Set values to textviews and image
        ImageView plantImage = findViewById(R.id.plantImage);
        Picasso.get().load(userPlant.getImageURL()).resize(500,500).into(plantImage);


        TextView plantName = findViewById(R.id.plantName);
        plantName.setText(userPlant.getCommonName());

        TextView sName = findViewById(R.id.plantSName);
        sName.setText(userPlant.getScientificName());

        TextView observation = findViewById(R.id.observation);
        observation.setText(userPlant.getObservations());

        TextView plantTemperature = findViewById(R.id.plantTemperature);
        if(userPlant.getTempMax() == null){
            plantTemperature.setText("No Info");
        }else{
            plantTemperature.setText(userPlant.getTempMin() + " - " + userPlant.getTempMax() + " \u2109");
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float temperature = event.values[0];
        // Do something with this sensor data.
        int F = getFahrenheit(temperature);
        TextView temperatureTV = findViewById(R.id.temperaturetv);
        temperatureTV.setText(F + " \u2109");
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //Convert Celcius to Fahrenheit
    private int getFahrenheit(float C){
        int F;
        F = (int) ((9/5) * C + 32);
        return F;
    }
}