package com.example.android.theweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherInfo extends AppCompatActivity {

    ImageView imgView, imgIcon;
    TextView tvMaxTemp, tvMinTemp, tvHumidity, tvVisibility, tvPressure, tvConfidence, tvWeatherState, tvDate;


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("consolidated_weather");

                Log.i("consolidated weather", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);


                JSONObject jsonPart = jsonArray.getJSONObject(0); // we get every single object within the JSON;
                //then we get the individual strings that we want
                tvWeatherState.setText(tvWeatherState.getText() + " " + jsonPart.getString("weather_state_name"));
                tvDate.setText(tvDate.getText() + " " + jsonPart.getString("applicable_date"));
                tvMinTemp.setText(tvMinTemp.getText() + " " + jsonPart.getString("min_temp").substring(0, 5));
                tvMaxTemp.setText(tvMaxTemp.getText() + " " + jsonPart.getString("max_temp").substring(0, 5));
                tvPressure.setText(tvPressure.getText() + " " + jsonPart.getString("air_pressure").substring(0, 5));
                tvHumidity.setText(tvHumidity.getText() + " " + jsonPart.getString("humidity") + "%");
                tvVisibility.setText(tvVisibility.getText() + " " + jsonPart.getString("visibility").substring(0, 5));
                tvConfidence.setText(tvConfidence.getText() + " " + jsonPart.getString("predictability") + "%");

                switch(jsonPart.getString("weather_state_name")){
                    case "Light Cloud": imgIcon.setImageResource(R.drawable.light_cloud); break;
                    case "Showers": imgIcon.setImageResource(R.drawable.heavy_rain); break;
                    case "Heavy Cloud": imgIcon.setImageResource(R.drawable.heavy_cloud); break;
                    case "Light Rain": imgIcon.setImageResource(R.drawable.light_rain); break;
                }


            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);
        DownloadTask task = new DownloadTask();
        tvConfidence = (TextView) findViewById(R.id.tvConfidence);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvMaxTemp = (TextView) findViewById(R.id.tvMaxTemp);
        tvMinTemp = (TextView) findViewById(R.id.tvMinTemp);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvVisibility = (TextView) findViewById(R.id.tvVisibility);
        tvWeatherState = (TextView) findViewById(R.id.tvWeatherState);
        tvDate = (TextView) findViewById(R.id.tvDate);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);


        Bundle parametros = getIntent().getExtras();

        String seleccion = parametros.getString("selection");

        imgView = (ImageView) findViewById(R.id.imgView);

        switch (seleccion){
            case "Lima": imgView.setImageResource(R.drawable.lima2);
                task.execute("https://www.metaweather.com/api/location/418440/"); break;
            case "Newark": imgView.setImageResource(R.drawable.newark2);
                task.execute("https://www.metaweather.com/api/location/2459269/");break;
            case "NewYork": imgView.setImageResource(R.drawable.new_york2);
                task.execute("https://www.metaweather.com/api/location/2459115/");break;
            case "Barcelona": imgView.setImageResource(R.drawable.barcelona_hero_2);
                task.execute("https://www.metaweather.com/api/location/753692/");break;
        }
    }

}
