package com.example.myweather;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    private String mtepm, micon, mCity, mWtype, mdescrip, mfeel;
    private int mcondition;

    public static WeatherData fromjson(JSONObject jsonObject) {
        try {
            WeatherData weatherD = new WeatherData();
            weatherD.mCity = jsonObject.getString("name");
            weatherD.mcondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWtype = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mdescrip = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherD.micon = updateweathericon(weatherD.mcondition);
            double tempresult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundval = (int) Math.rint(tempresult);
            weatherD.mtepm = Integer.toString(roundval);
            double realtemp = jsonObject.getJSONObject("main").getDouble("feels_like") - 273.15;
            int val = (int) Math.rint(realtemp);
            weatherD.mfeel = Integer.toString(val);
            return weatherD;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateweathericon(int mcondition) {
        if (mcondition >= 0 && mcondition <= 300) {
            return "thunderstorm1";
        } else if (mcondition >= 300 && mcondition <= 500) {
            return "lightrain ";
        } else if (mcondition >= 500 && mcondition <= 600) {
            return "shower";
        } else if (mcondition >= 600 && mcondition <= 700) {
            return "snow1";
        } else if (mcondition >= 701 && mcondition <= 771) {
            return "fog";
        } else if (mcondition >= 772 && mcondition <= 800) {
            return "overcast";
        } else if (mcondition == 800) {
            return "sunny";
        } else if (mcondition >= 801 && mcondition <= 804) {
            return "cloudy";
        } else if (mcondition >= 900 && mcondition <= 902) {
            return "thunderstorm1";
        } else if (mcondition == 903) {
            return "snow2";
        } else if (mcondition == 904) {
            return "sunny";
        } else if (mcondition >= 905 && mcondition <= 1000) {
            return "thunderstorm2";
        }
        return null;
    }

    public String getMtepm() {
        return mtepm + "°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWtype() {
        return mWtype;
    }

    public String getMfeel() {
        return "Real Feel " + mfeel + "°C";
    }

    public String getMdescrip() {
        return mdescrip;
    }

}
