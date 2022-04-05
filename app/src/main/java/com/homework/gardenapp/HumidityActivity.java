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
public class HumidityActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor humidity;
    Plant userPlant = new Plant();

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        Intent i = getIntent();
        if (i.getSerializableExtra("userPlant") != null) {
            userPlant = (Plant) i.getSerializableExtra("userPlant");
        }

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        //Set values to textviews and image
        ImageView plantImage = findViewById(R.id.plantImage);
        Picasso.get().load(userPlant.getImageURL()).resize(500,500).into(plantImage);


        TextView plantName = findViewById(R.id.plantName);
        plantName.setText(userPlant.getCommonName());

        TextView sName = findViewById(R.id.plantSName);
        sName.setText(userPlant.getScientificName());

        TextView observation = findViewById(R.id.observation);
        observation.setText(userPlant.getObservations());

        TextView plantHumidity = findViewById(R.id.plantHumidity);
        if(userPlant.getHumidityAir() == null){
            plantHumidity.setText("No Info");
        }else{
            plantHumidity.setText(userPlant.getHumidityAir()*10 + "%");
        }

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float percentHumidity = event.values[0];
        // Do something with this sensor data.
        TextView humidityTV = findViewById(R.id.humiditytv);
        humidityTV.setText(String.valueOf(percentHumidity) + "%");
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}