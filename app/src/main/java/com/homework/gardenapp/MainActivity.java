package com.homework.gardenapp;
//Name: Alan Lim Team 19
//CUID: C18104514
//Date: 4/18/2021
//Project: Final Project Garden Helper App

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements PlantListFragment.OnPlantSelectedListener {

    private static final int PERMISSIONS_COARSE_LOCATION = 201;
    private final String TAG = "MainActivity";
    private int mPlantId;

    //Stores the user's plant
    Plant userPlant;

    PlantListFragment plantListFragment = new PlantListFragment();

    //Getting Location Variables from google api
    FusedLocationProviderClient fusedClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Location lastLoc = new Location("main");

    private final Weather[] weatherArray = new Weather[5];

    //Arrays to store text views and image views
    private final TextView[] wDay = new TextView[5];
    private final TextView[] dDay = new TextView[5];
    private final ImageView[] icon = new ImageView[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set locationRequest variables
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                lastLoc = locationResult.getLastLocation();
            }
        };

        //Bind widgets
        wDay[0] = (TextView) findViewById(R.id.wDay1);
        wDay[1] = (TextView) findViewById(R.id.wDay2);
        wDay[2] = (TextView) findViewById(R.id.wDay3);
        wDay[3] = (TextView) findViewById(R.id.wDay4);
        wDay[4] = (TextView) findViewById(R.id.wDay5);

        dDay[0] = (TextView) findViewById(R.id.dDay1);
        dDay[1] = (TextView) findViewById(R.id.dDay2);
        dDay[2] = (TextView) findViewById(R.id.dDay3);
        dDay[3] = (TextView) findViewById(R.id.dDay4);
        dDay[4] = (TextView) findViewById(R.id.dDay5);

        icon[0]= (ImageView) findViewById(R.id.weather1);
        icon[1]= (ImageView) findViewById(R.id.weather2);
        icon[2]= (ImageView) findViewById(R.id.weather3);
        icon[3]= (ImageView) findViewById(R.id.weather4);
        icon[4]= (ImageView) findViewById(R.id.weather5);

        EditText editText = (EditText) findViewById(R.id.etItem);
        Button sButton = (Button) findViewById(R.id.sButton);


        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get value from edit text for search query
                String etItem = editText.getText().toString();

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container1);

                //If there is a fragment displayed remove it
                if(fragment != null) {
                    fragmentManager.beginTransaction().remove(plantListFragment).commit();
                    fragmentManager.popBackStackImmediate();
                }

                plantListFragment = PlantListFragment.newInstance(etItem);
                //Create a new instance of the search query when the button is pressed
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container1, plantListFragment)
                        .commit();



                //Hide Keyboard after button is pressed
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
            }
        });

        //callAPI(lat,lon);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "PERMISSION GRANTED");
                getLocation();
            } else {
                Toast.makeText(this, "This app requires location to get current location weather", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getLocation(){
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        //If user provides the permission to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Set the location variable
                    if (location != null) {
                        lastLoc = location;
                        callAPI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                    }
                }
            });

        } else { //User did not give permission yet.
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_COARSE_LOCATION);
        }
    }

    //Calls openweather api and stores information in weather array and displays information
    //Lat and Long for the current location
    private void callAPI(String lat, String lon){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String app_id = getString(R.string.weather_api_key);

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat
                + "&lon=" + lon
                + "&exclude=current,minutely,hourly,alerts&appid=" + app_id
                + "&units=imperial";

        JsonObjectRequest requestObj = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("daily");

                            //Only needs the 5 consecutive days
                            for (int i = 0; i < 5; i++){
                                //Fetch Data based on objects and arrays;
                                JSONObject daily = jsonArray.getJSONObject(i);
                                JSONObject temp = daily.getJSONObject("temp");
                                JSONArray weatArr = daily.getJSONArray("weather");
                                JSONObject weather = weatArr.getJSONObject(0);

                                //Store Weather information in an Array
                                weatherArray[i] = new Weather(
                                        daily.getLong("dt"),
                                        temp.getInt("day"),
                                        temp.getInt("night"),
                                        daily.getInt("pressure"),
                                        daily.getInt("humidity"),
                                        daily.getInt("wind_speed"),
                                        weather.getString("main"),
                                        weather.getString("icon")
                                );

                                //Set day of the week from today to 4 consecutive day
                                wDay[i].setText(weatherArray[i].getDayOfWeek());
                                //Set the image to image view based on weather type
                                Picasso.get().load(weatherArray[i].getURL()).resize(250,250).into(icon[i]);
                                //Set temperature in Fahrenheit
                                String degreesF = weatherArray[i].getDayTemp() + " \u2109";
                                dDay[i].setText(degreesF);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                    }
                });

        queue.add(requestObj);

    }

    //Id is provided by the api
    //When a plant is selected calls the api to store the plant locally with information
    @Override
    public void onPlantSelected(int plantId) {

        mPlantId = plantId;

        //Get plant info using plant id and store it
        getPlantInfo(mPlantId);

    }

    // Calls trefle and get the plants info using ID
    //id = plant id that is associated with trefle
    public void getPlantInfo(int id){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        Resources res = this.getResources();
        String API_KEY = res.getString(R.string.trefle_api_key);

        String SEARCH_URL = "https://trefle.io/api/v1/species/" + id + "?token=" + API_KEY;

        JsonObjectRequest requestObj = new JsonObjectRequest
                (Request.Method.GET, SEARCH_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //There should be only one plant
                            JSONObject data = response.getJSONObject("data");

                            JSONObject growth = data.getJSONObject("growth");
                            JSONObject minTemp = growth.getJSONObject("minimum_temperature");
                            JSONObject maxTemp = growth.getJSONObject("maximum_temperature");

                            //Non-primitives for values that are null
                            Double pMin, pMax;
                            Integer aH, sH, tMin, tMax;

                            if(!growth.isNull("ph_minimum")) {
                                pMin = growth.getDouble("ph_minimum");
                            } else {
                                pMin = null;
                            }

                            if(!growth.isNull("ph_maximum")) {
                                pMax = growth.getDouble("ph_maximum");
                            } else {
                                pMax = null;
                            }

                            if(!growth.isNull("atmospheric_humidity")) {
                                aH = growth.getInt("atmospheric_humidity");
                            } else {
                                aH = null;
                            }

                            if(!growth.isNull("soil_humidity")) {
                                sH = growth.getInt("soil_humidity");
                            } else {
                                sH = null;
                            }

                            if(!minTemp.isNull("deg_f")) {
                                tMin = minTemp.getInt("deg_f");
                            } else {
                                tMin = null;
                            }

                            if(!maxTemp.isNull("deg_f")) {
                                tMax = maxTemp.getInt("deg_f");
                            } else {
                                tMax = null;
                            }


                            //Create a new plant containing necessary information to display
                            userPlant = new Plant(
                                    data.getInt("id"),
                                    data.getString("common_name"),
                                    data.getString("scientific_name"),
                                    data.getString("observations"),
                                    data.getString("image_url"),
                                    pMin,
                                    pMax,
                                    aH,
                                    sH,
                                    tMin,
                                    tMax
                                    //Pressure doesn't exist
                            );

                            String toastText = "Your Plant is set to " + userPlant.getCommonName();
                            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                    }
                });

        mQueue.add(requestObj);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case R.id.get_humidity:
                intent = new Intent(this, HumidityActivity.class);
                if(userPlant != null) {
                    intent.putExtra("userPlant", userPlant);
                }
                startActivity(intent);
                return true;

            case R.id.get_pressure:
                intent = new Intent(this, PressureActivity.class);
                if(userPlant != null) {
                    intent.putExtra("userPlant", userPlant);
                }
                startActivity(intent);
                return true;

            case R.id.get_temp:
                intent = new Intent(this, TemperatureActivity.class);
                if(userPlant != null) {
                    intent.putExtra("userPlant", userPlant);
                }
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}