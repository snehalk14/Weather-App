//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane
package com.example.hw03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CityWeatherActivity extends AppCompatActivity implements ForecastAdapter.iForecast{

    private static final String TAG = "demo";
    public static final String CITY_KEY = "CITY";
    TextView tv_cityCountry,tv_headline,tv_date,tv_temp,tv_dayPhrase,tv_nightPhrase,tv_moreDtails;
    Button btn_saveCity,btn_SetAsCurrent;
    ImageView iv_day,iv_night;
    String baseURL = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = null;
    private RecyclerView.LayoutManager layoutManager;
    CityWeatherDetails cityWeatherDetails;
    City city;
    String cityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("City Weather");

        setContentView(R.layout.activity_city_weather);
        tv_cityCountry = findViewById(R.id.tv_cityCountry);
        tv_date = findViewById(R.id.tv_date);
        tv_headline = findViewById(R.id.tv_headline);
        tv_temp = findViewById(R.id.tv_temp);
        tv_dayPhrase  = findViewById(R.id.tv_dayPhrase);
        tv_nightPhrase = findViewById(R.id.tv_nightPhrase);
        tv_moreDtails = findViewById(R.id.tv_MoreDetails);
        tv_moreDtails.setClickable(true);

        btn_saveCity = findViewById(R.id.btn_saveCity);
        btn_SetAsCurrent = findViewById(R.id.btn_SetAsCurrent);

        iv_day = findViewById(R.id.iv_day);
        iv_night = findViewById(R.id.iv_night);

        recyclerView = findViewById(R.id.recyclerForecast);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        city = new City();


        if (isConnected()) {

            if(getIntent()!=null && getIntent().getExtras()!=null){

                Bundle bundle = getIntent().getBundleExtra(MainActivity.FORECAST_KEY);
                tv_cityCountry.setText(bundle.getString("city")+ ", " + bundle.getString("country"));
                cityKey = bundle.getString("key");
                String forecastURL = baseURL + cityKey + "?apikey=" + MainActivity.ApiKey;
                new GetCityWeatherDetails().execute(forecastURL);
            }

            btn_saveCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.progressDialog.dismiss();
                    Log.d(TAG,"City" + city.toString());
                    MainActivity.savedcities.add(city);
                    finish();
                }
            });

            btn_SetAsCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.progressDialog.dismiss();
                    MainActivity.currentCity = city;
                    finish();
                }
            });


        } else {
            Log.d(TAG, "Not connected");
            Toast.makeText(CityWeatherActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !((NetworkInfo) networkInfo).isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    @Override
    public void setForecast(int position) {

        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = outputFormat.format(cityWeatherDetails.forecast.get(position).getDate());
        tv_date.setText("Forecast on " + formattedDate);
        tv_temp.setText("Temperature " + cityWeatherDetails.forecast.get(position).getMaxTemp() +
                "/"  + cityWeatherDetails.forecast.get(position).getMinTemp() + " " + cityWeatherDetails.getUnit());
        tv_dayPhrase.setText(cityWeatherDetails.forecast.get(position).getDayPhrase());
        tv_nightPhrase.setText(cityWeatherDetails.forecast.get(position).getNightPhrase());

        String day,night;
        if(cityWeatherDetails.forecast.get(position).getDayIcon() < 10)
            day  = "0" + cityWeatherDetails.forecast.get(position).getDayIcon();
        else
            day = cityWeatherDetails.forecast.get(position).getDayIcon()+"";
        if(cityWeatherDetails.forecast.get(position).getNightIcon() < 10)
            night  = "0" + cityWeatherDetails.forecast.get(position).getNightIcon();
        else
            night = cityWeatherDetails.forecast.get(position).getNightIcon()+"";

        String imgUrlDay = "http://developer.accuweather.com/sites/default/files/" + day + "-s.png";
        String imgURLNight = "http://developer.accuweather.com/sites/default/files/" + night + "-s.png";

        Picasso.get().load(imgUrlDay).into(iv_day);
        Picasso.get().load(imgURLNight).into(iv_night);

        tv_moreDtails.setMovementMethod(LinkMovementMethod.getInstance());
        String moreDetailsLink = "<a href='" + cityWeatherDetails.forecast.get(position).getMobileLink() + "'> Click here for more details  </a>";
        tv_moreDtails.setText(Html.fromHtml(moreDetailsLink));


    }

    @Override
    public void saveCity(int position) {
        city.setCityKey(cityWeatherDetails.getCityKey());
        city.setCityName(tv_cityCountry.getText().toString().trim());
        city.setWeather(cityWeatherDetails.forecast.get(position).getDayPhrase());
        city.setIcon(cityWeatherDetails.forecast.get(position).getDayIcon());
        city.setDate(cityWeatherDetails.forecast.get(position).getDate());
        city.setTemperature(cityWeatherDetails.forecast.get(position).getMinTemp());
        city.setFavorite(false);
        PrettyTime pt = new PrettyTime();
        city.setTime(pt.format(city.getDate()));

    }

    class GetCityWeatherDetails extends AsyncTask<String,Void,CityWeatherDetails> {
        @Override
        protected CityWeatherDetails doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                String urlString = strings[0];
                Log.d(TAG, "URL = " + urlString);

                URL url = new URL(urlString);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Log.d(TAG, "status ok");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    cityWeatherDetails = new CityWeatherDetails();
                    cityWeatherDetails.setCityKey(cityKey);
                    JSONObject root = new JSONObject(json);
                    JSONObject headline = root.getJSONObject("Headline");
                    cityWeatherDetails.headline = headline.getString("Text");
                    JSONArray dailyForecasts = root.getJSONArray("DailyForecasts");
                    ArrayList<Forecast> forecasts = new ArrayList<>();
                    for(int i=0;i<dailyForecasts.length();i++){
                        Forecast forecast = new Forecast();
                        JSONObject details = dailyForecasts.getJSONObject(i);
                        JSONObject temp = details.getJSONObject("Temperature");
                        forecast.setMaxTemp(temp.getJSONObject("Maximum").getDouble("Value"));
                        cityWeatherDetails.setUnit(temp.getJSONObject("Maximum").getString("Unit"));
                        forecast.setMinTemp(temp.getJSONObject("Minimum").getDouble("Value"));
                        JSONObject day = details.getJSONObject("Day");
                        forecast.setDayIcon(day.getInt("Icon"));
                        forecast.setDayPhrase(day.getString("IconPhrase"));
                        JSONObject night = details.getJSONObject("Night");
                        forecast.setNightIcon(night.getInt("Icon"));
                        forecast.setNightPhrase(night.getString("IconPhrase"));
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        forecast.setDate(inputFormat.parse(details.getString("Date")));
                        forecast.setMobileLink(details.getString("MobileLink"));
                        forecasts.add(forecast);
                        Log.d(TAG,"forecast: " + forecast.toString());
                    }

                    cityWeatherDetails.forecast = forecasts;
                    Log.d(TAG,"weather : " + cityWeatherDetails.toString());
                    return cityWeatherDetails;

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(CityWeatherDetails cityWeatherDetails) {
            super.onPostExecute(cityWeatherDetails);

            tv_headline.setText(cityWeatherDetails.getHeadline());
            ArrayList<Forecast> forecasts = cityWeatherDetails.getForecast();
            tv_temp.setText("Temperature " + forecasts.get(0).getMaxTemp() + "/"  +forecasts.get(0).getMinTemp() + " " + cityWeatherDetails.getUnit());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy");
            //Log.d(TAG,"date: " +  forecasts.get(0).getDate().toString());
            String formattedDate = outputFormat.format(forecasts.get(0).getDate());
            tv_date.setText("Forecast on " + formattedDate);
            tv_dayPhrase.setText(forecasts.get(0).getDayPhrase());
            tv_nightPhrase.setText(forecasts.get(0).getNightPhrase());

            String day,night;
            if(cityWeatherDetails.forecast.get(0).getDayIcon() < 10)
                day  = "0" + cityWeatherDetails.forecast.get(0).getDayIcon();
            else
                day = cityWeatherDetails.forecast.get(0).getDayIcon()+"";
            if(cityWeatherDetails.forecast.get(0).getNightIcon() < 10)
                night  = "0" + cityWeatherDetails.forecast.get(0).getNightIcon();
            else
                night = cityWeatherDetails.forecast.get(0).getNightIcon()+"";
            int nightIcon = cityWeatherDetails.forecast.get(0).getNightIcon();
            String imgUrlDay = "http://developer.accuweather.com/sites/default/files/" + day + "-s.png";
            String imgURLNight = "http://developer.accuweather.com/sites/default/files/" + night + "-s.png";

            Picasso.get().load(imgUrlDay).into(iv_day);
            Picasso.get().load(imgURLNight).into(iv_night);

            tv_moreDtails.setMovementMethod(LinkMovementMethod.getInstance());
            String moreDetailsLink = "<a href='" + cityWeatherDetails.forecast.get(0).getMobileLink() + "'> Click here for more details </a>";
            tv_moreDtails.setText(Html.fromHtml(moreDetailsLink));

            mAdapter = new ForecastAdapter(forecasts,CityWeatherActivity.this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            city.setCityKey(cityWeatherDetails.getCityKey());
            city.setCityName(tv_cityCountry.getText().toString().trim());
            city.setWeather(cityWeatherDetails.forecast.get(0).getDayPhrase());
            city.setIcon( cityWeatherDetails.forecast.get(0).getDayIcon());
            city.setDate(cityWeatherDetails.forecast.get(0).getDate());
            city.setTemperature(cityWeatherDetails.forecast.get(0).getMinTemp());
            city.setUnit(cityWeatherDetails.getUnit());
            city.setFavorite(false);
            PrettyTime pt = new PrettyTime();
            city.setTime(pt.format(city.getDate()));



        }
    }


}
