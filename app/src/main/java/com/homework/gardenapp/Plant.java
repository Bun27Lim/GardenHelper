 package com.homework.gardenapp;

import java.io.Serializable;

//Plant class to store information from api

public class Plant implements Serializable {

    //Integers because values can not exist.
    private int id;

    private String commonName;
    private String scientificName;

    private String observations; // Small description about the plant
    private String imageURL;
    private String selfURL; // API endpoint for more information about the plants

    private Integer harvestDays;
    private Double phMAX, phMIN;

    private Integer air_humidity;
    private Integer soil_humidity;

    private Integer tempMAX, tempMIN;

    private Integer pressure;

    //default constructor
    public Plant(){
    }
    //Only needs these for the recycler view
    //i: ID of plant being stored
    //cN: Common name of the plant
    //sN: Scientific name of the plant
    //iU: Image url to display the image
    //sU: Self url api endpoint that gives more information on the plants.
    public Plant(int i, String cN, String sN, String iU, String sU){
        id = i;
        commonName = cN;
        scientificName = sN;
        imageURL = iU;
        selfURL = sU;
    }

    //Need to get all the information for user plant
    public Plant(int i, String cN, String sN, String o,String iU,
                 Double pMin, Double pMax, Integer aH, Integer sH, Integer tMin, Integer tMax){
        id = i;
        commonName = cN;
        scientificName = sN;
        observations = o;
        imageURL = iU;
        phMIN = pMin;
        phMAX = pMax;
        air_humidity = aH;
        soil_humidity = sH;
        tempMIN = tMin;
        tempMAX = tMax;
    }

    public int getId(){
        return id;
    }

    public String getCommonName(){
        return commonName;
    }

    public String getScientificName(){
        return scientificName;
    }

    public String getObservations() { return observations;}

    public String getImageURL(){
        return imageURL;
    }

    public String getSelfURL(){
        return selfURL;
    }

    //Right now there is no information on air pressure for plants so return -1;
    public Integer getPressure() {return pressure;}

    public Integer getHumidityAir() {return air_humidity;}

    public Integer getTempMin() {return tempMIN;}

    public Integer getTempMax() {return tempMAX;}

}
