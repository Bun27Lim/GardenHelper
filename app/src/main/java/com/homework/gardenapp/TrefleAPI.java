package com.homework.gardenapp;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TrefleAPI {
    public interface VolleyCallBack {
        void onSuccess();
    }

    private static TrefleAPI sTrefleApi;

    private String etItem;

    private Context context;

    String SEARCH_URL = "https://trefle.io/api/v1/species/search?token=";

    private List<Plant> mPlants; //List to store the plants you get from search query to use later to print

    public static TrefleAPI get(Context context, String etItem) {
        if (sTrefleApi == null) {
            sTrefleApi = new TrefleAPI(context, etItem);
        }
        return sTrefleApi;
    }

    public TrefleAPI(Context c, String e) {
        //List to store the plants in
        mPlants = new ArrayList<>();
        etItem = e;
        context = c;
        callAPI();
    }

    public List<Plant> getPlants() { return mPlants; }

    public Plant getPlant(int plantId) {
        for (Plant plant : mPlants) {
            if (plant.getId() == plantId) {
                return plant;
            }
        }
        return null;
    }

    //API TO SEARCH FOR THE PLANTS BASED ON INPUT
    public void callAPI(){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        Resources res = context.getResources();
        //get api key
        String API_KEY = res.getString(R.string.trefle_api_key);

        SEARCH_URL += API_KEY + "&q=" + etItem;

        JsonObjectRequest requestObj = new JsonObjectRequest
                    (Request.Method.GET, SEARCH_URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //Fetch Data based on objects and arrays;
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    JSONObject links = data.getJSONObject("links");

                                    //Create a new plant containing necessary information to display
                                    Plant searchedPlant = new Plant(
                                            data.getInt("id"),
                                            data.getString("common_name"),
                                            data.getString("scientific_name"),
                                            data.getString("image_url"),
                                            links.getString("self")
                                    );

                                    mPlants.add(searchedPlant);
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

            mQueue.add(requestObj);
    }
}
