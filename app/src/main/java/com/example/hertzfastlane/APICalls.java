package com.example.hertzfastlane;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by dapik on 3/15/2017.
 */

public class APICalls {

    private static Member user;

    public static Member getUser() {
        return user;
    }

    public static boolean login(String username, String password) throws IOException, JSONException {
        URL url = null;
        URL url2 = null;

        url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/readMemberInfo?username="
                + username);

        HttpURLConnection urlConnection = null;
        HttpURLConnection urlConnection2 = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        String resultString = result.toString();
        //Log.d("TAG",resultString);
        JSONObject hash = new JSONObject(resultString);
        JSONObject hashcode = hash.getJSONObject("Item");
        //Log.d("TAG",(String)hashcode.get("hash"));
        url2 = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/hashedLogin?hash=" + hashcode.get("hash"));
        StringBuilder result2 = null;

        urlConnection2 = (HttpURLConnection) url2.openConnection();
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(url2.openStream()));
        result2 = new StringBuilder();
        String line2;
        while ((line2 = reader2.readLine()) != null) {
            result2.append(line2);
        }
        String resultString2 = result2.toString();
        //Log.d("TAG",resultString2);
        JSONObject memberItem = new JSONObject(resultString2);
        JSONObject member = memberItem.getJSONObject("Item");

        // Log.d("Tag", member.toString());
        ObjectMapper mapper = new ObjectMapper();

        user = mapper.readValue(member.toString(), Member.class);
        // Log.d("Tag", user.getPassword());

        if (password.equals(user.getPassword())) {
            return true;
        }


        return false;
    }

    public static Car getCarInfo(String car_id) throws IOException, JSONException {
        Car carInformation = null;
        URL url = null;
        try {
            url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/readingFleet?car_id="
                    + car_id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        String resultString = result.toString();

        JSONObject carMap = new JSONObject(resultString);

        JSONObject car = carMap.getJSONObject("Item");
        JSONObject carInfo = car.getJSONObject("info");

        //Deserializing to JSON Car Information
        ObjectMapper mapper = new ObjectMapper();

        carInformation = mapper.readValue(car.toString(), Car.class);

        Info infoCar = mapper.readValue(carInfo.toString(), Info.class);
        carInformation.setInfo(infoCar);

        return carInformation;
    }

    public static Weather getWeather(String lat, String lon) throws IOException, JSONException {
        Weather weatherData;
        URL url = null;
        try {
            url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/openWeather?lat="
                    + lat + "&lon=" + lon);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        String resultString = result.toString();

        JSONObject weatherResult = new JSONObject(resultString);

        //Deserializing to JSON Weather Information
        ObjectMapper mapper = new ObjectMapper();

        weatherData = mapper.readValue(weatherResult.toString(), Weather.class);

        return weatherData;
    }

    public static Reservation getReservation(Member member) throws IOException, JSONException {
        StringBuilder result = null;
        URL url = null;
        try {
            url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/gate?mem_id="
                    + member.getMember_id());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        String resultString = result.toString();
        Log.d("TAGGY", result.toString());

        JSONObject resMap = new JSONObject(resultString);

        JSONArray reses = resMap.getJSONArray("Items");
        JSONObject res = reses.getJSONObject(0);

        //Deserializing to JSON Car Information
        ObjectMapper mapper = new ObjectMapper();

        Reservation reservation = mapper.readValue(res.toString(), Reservation.class);

        return reservation;


    }

    public static String checkExitConditions(String member_id, List<String> carIds) throws IOException, JSONException {


        URL url = null;
        try {
            url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/gate?mem_id="
                    + member_id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        String resultString = result.toString();
        Log.d("TAGGY", result.toString());

        JSONObject resMap = new JSONObject(resultString);

        JSONArray reses = resMap.getJSONArray("Items");
        JSONObject res = reses.getJSONObject(0);

        String car_vin = res.getString("car_Vin");

        if (carIds.contains(car_vin) && res.getString("status").equals("active")) {
            return "Success!";
        }


        return "FAILED";
    }

    public static String getCar_ID(String beacon_id) throws IOException, JSONException{

        URL url = null;
        try {
            url = new URL("https://q3igdv3op1.execute-api.us-east-1.amazonaws.com/prod/beaconMap?id="
                    + beacon_id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            String resultString = result.toString();


            JSONObject map = new JSONObject(resultString);

            JSONObject hash = map.getJSONObject("Item");


            String car_id = hash.getString("car_id");

            return car_id;

    }


}