package com.example.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String Api_ID = "aa7482526b62377427a3ee149f7e2fc9";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    final long min_time = 1000;
    final float min_dist = 1000;
    final int request_code = 101;
    String city;

    TextView nameCity, weathercondition, tempreture, describ, realfeel;
    ImageView weatherIcon;
    ImageButton myLocationbutton;
    Button cityFinderButton;
    LocationManager mlocationmanager;
    LocationListener mlocationListener;
    String Location_provider = LocationManager.NETWORK_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("");

        nameCity = findViewById(R.id.textView_cityname_id);
        weathercondition = findViewById(R.id.textView_condition_id);
        tempreture = findViewById(R.id.textView_tempreture_ID);
        weatherIcon = findViewById(R.id.weather_icon);
        cityFinderButton = findViewById(R.id.button_cityFinder);
        myLocationbutton = findViewById(R.id.mylocation_button_ID);
        describ = findViewById(R.id.descibTextView_id);
        realfeel = findViewById(R.id.textView_realfeel_id);
        //cityFinderButton.setBackgroundColor(Color.BLACK);

        myLocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherForCurrentLocation();
            }
        });

        cityFinderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Find_City.class);
                startActivity(intent);
            }
        });
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        getWeatherForCurrentLocation();
//    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent Cintent = getIntent();
        city = Cintent.getStringExtra("City");
        if (city != null) {
            newcityWeatherfinder(city);
        } else {
            getWeatherForCurrentLocation();
        }
    }

    private void newcityWeatherfinder(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", Api_ID);
        doNetworkPatch(params);
    }

    private static final String TAG = "MainActivity";

    private void getWeatherForCurrentLocation() {
        mlocationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                Log.d(TAG, "onLocationChanged: " + Latitude);
                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", Api_ID);
                doNetworkPatch(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_code);
            return;
        }
        mlocationmanager.requestLocationUpdates(Location_provider, min_time, min_dist, mlocationListener);
    }

    private void doNetworkPatch(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Weather_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // super.onSuccess(statusCode, headers, response);
                Log.d(TAG, "onSuccess: " + response.toString());
                Toast.makeText(MainActivity.this, "Data Get Success !", Toast.LENGTH_SHORT).show();
                WeatherData wdata = WeatherData.fromjson(response);
                updateUI(wdata);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure: " + errorResponse.toString());
                Toast.makeText(MainActivity.this, "Failed: " + errorResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherData wdata) {
        tempreture.setText(wdata.getMtepm());
        nameCity.setText(wdata.getmCity());
        weathercondition.setText(wdata.getmWtype());
        describ.setText(wdata.getMdescrip());
        int resourceid = getResources().getIdentifier(wdata.getMicon(), "drawable", getPackageName());
        weatherIcon.setImageResource(resourceid);
        realfeel.setText(wdata.getMfeel());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationmanager != null) {
            mlocationmanager.removeUpdates(mlocationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == request_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Lacation Access Granted", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
            }
        }
    }
}