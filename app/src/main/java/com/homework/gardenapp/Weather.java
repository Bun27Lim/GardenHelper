package com.homework.gardenapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Weather {

    private long dt; //This is the GMT Unix timestamp in seconds

    private int dayTemp;
    private int nightTemp;
    private int pressure;
    private int humidity;
    private int windSpeed;

    private String skyType; //Sunny or Cloudy etc
    private String icon; //Icon Id to display image from openweatherapi url

    private String URL; //url where image is from using Icon

    public Weather(long d, int da, int n, int p, int h, int w, String sky, String i){
        dt = d;
        dayTemp = da;
        nightTemp = n;
        pressure = p;
        humidity = h;
        windSpeed = w;
        skyType = sky;
        icon = i;

        URL = "https://openweathermap.org/img/w/" + icon + ".png";
    }

    public String getDayOfWeek(){
        Date date = new Date(dt*1000L); //Convert seconds to milliseconds
        SimpleDateFormat dayName = new SimpleDateFormat("EEE");
        dayName.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return dayName.format(date);
    }

    public String getURL(){
        return URL;
    }

    public int getDayTemp(){
        return dayTemp;
    }
}
