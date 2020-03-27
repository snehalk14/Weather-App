//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane
package com.example.hw03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CityAdapter.iCity{
    private static final String TAG = "demo";
    public static final String FORECAST_KEY = "FORECAST";
    String baseURL = "http://dataservice.accuweather.com/locations/v1/cities/";
    Map<String,String> cityLocationDetails = new HashMap<String, String>();
    String searchedCity = null;
    Button btn_setCity,btn_searchCity;
    EditText et_city,et_country;
    TextView tv_noCityMsg,tv_SavedCities,tv_1,tv_CityName,tv_Temperature,tv_TempVal,tv_Weather,tv_Updated,tv_updatedVal;
    ImageView iv_icon;
    String KEY = null;
    String adCity, adCountry;
    public static ArrayList<City> savedcities = new ArrayList<City>();
    public static City currentCity = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = null;
    private RecyclerView.LayoutManager layoutManager;
    //public static String ApiKey = "b4xFAgvozcmUyJzqQddqp1M3xrdEPc0u";
    //public static String ApiKey = "TsCQrEFKSBJBBa0yGQKvhpO49cnLcaGE";
    //public static String ApiKey = "NArP2PR0FXStaXAByWAjReGvi84r0ToQ";
    public static String ApiKey = "eQFkOMT4KMDn7vtVmws1UhWUMjxURODe";


    ProgressDialog aprogressDialog;
    public static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather App");

        btn_setCity = findViewById(R.id.btn_SetCity);
        btn_searchCity = findViewById(R.id.btn_SearchCity);
        et_city = findViewById(R.id.et_CityName);
        et_country = findViewById(R.id.et_Country);
        tv_noCityMsg = findViewById(R.id.tv_noCityMsg);
        tv_SavedCities = findViewById(R.id.tv_SavedCities);
        tv_1 = findViewById(R.id.tv_1);
        tv_CityName = findViewById(R.id.tv_CityName);
        tv_Temperature = findViewById(R.id.tv_Temperature);
        tv_TempVal = findViewById(R.id.tv_TempVal);
        tv_Weather = findViewById(R.id.tv_Weather);
        tv_Updated = findViewById(R.id.tv_Updated);
        tv_updatedVal = findViewById(R.id.tv_updatedVal);
        iv_icon = findViewById(R.id.iv_icon);


        recyclerView = findViewById(R.id.recylcerSavedCitites);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);



        if (isConnected()) {

            btn_setCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.city_details,null);

                    final EditText et_adCity = (EditText)view.findViewById(R.id.et_adCity);
                    final EditText et_adCountry = (EditText)view.findViewById(R.id.et_adCountry);

                    builder.setView(view);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Dismiss the dialog here
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Add ok operation here

                            adCity = et_adCity.getText().toString().trim();
                            adCity =  replaceChar(adCity,Character.toTitleCase(adCity.charAt(0)),0);
                            adCountry = et_adCountry.getText().toString().trim();
                            adCountry = adCountry.toUpperCase();

                            Log.d(TAG, "cityName = " + adCity);
                            Log.d(TAG, "country = " + adCountry);

                            String locationUrl = baseURL + adCountry + "/search?apikey=" + ApiKey + "&q=" + adCity;
                            aprogressDialog = new ProgressDialog(MainActivity.this);
                            aprogressDialog.setTitle("Loading...");
                            aprogressDialog.show();
                            new GetCityKey().execute(locationUrl);

                        }
                    });

                    builder.show();


                }
            });



            btn_searchCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String country = et_country.getText().toString().trim();
                    if(et_city.getText().toString().trim().length() == 0 || country.length() == 0){
                        Toast.makeText(MainActivity.this,"Please enter city and country!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String locationUrl = baseURL + country + "/search?apikey=" + ApiKey + "&q=" + et_city.getText().toString().trim();
                        new GetCityList().execute(locationUrl);

                    }

                }
            });



        } else {
            Log.d(TAG, "Not connected");
            Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        if(savedcities.size() == 0){
            tv_noCityMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_SavedCities.setVisibility(View.INVISIBLE);

        }else{
            tv_noCityMsg.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            tv_SavedCities.setVisibility(View.VISIBLE);
            mAdapter = new CityAdapter(savedcities,MainActivity.this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        if(currentCity!=null) {
            tv_1.setVisibility(View.INVISIBLE);
            btn_setCity.setVisibility(View.INVISIBLE);
            tv_CityName.setVisibility(View.VISIBLE);
            tv_Temperature.setVisibility(View.VISIBLE);
            tv_TempVal.setVisibility(View.VISIBLE);
            tv_Updated.setVisibility(View.VISIBLE);
            tv_Weather.setVisibility(View.VISIBLE);
            tv_updatedVal.setVisibility(View.VISIBLE);
            iv_icon.setVisibility(View.VISIBLE);

            tv_CityName.setText(currentCity.getCityName());
            tv_TempVal.setText(currentCity.getTemperature()+ " " + currentCity.getUnit());
            tv_updatedVal.setText(currentCity.getTime());
            tv_Weather.setText(currentCity.getWeather());
            String imgURL;
            if(currentCity.getIcon() < 10)
                imgURL  =  "http://developer.accuweather.com/sites/default/files/" + "0" + currentCity.getIcon() + "-s.png";
            else
                imgURL = "http://developer.accuweather.com/sites/default/files/" + currentCity.getIcon() + "-s.png";

            Picasso.get().load(imgURL).into(iv_icon);

        }else {
            tv_1.setVisibility(View.VISIBLE);
            btn_setCity.setVisibility(View.VISIBLE);
            tv_CityName.setVisibility(View.INVISIBLE);
            tv_Temperature.setVisibility(View.INVISIBLE);
            tv_TempVal.setVisibility(View.INVISIBLE);
            tv_Updated.setVisibility(View.INVISIBLE);
            tv_Weather.setVisibility(View.INVISIBLE);
            tv_updatedVal.setVisibility(View.INVISIBLE);
            iv_icon.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void delete(int position) {
        savedcities.remove(position);
        if(savedcities.size() == 0) {
            tv_noCityMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_SavedCities.setVisibility(View.INVISIBLE);
        }
        else{
            tv_noCityMsg.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            tv_SavedCities.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFavorite(int position) {
        if(savedcities.get(position).isFavorite())
            savedcities.get(position).setFavorite(false);
        else
            savedcities.get(position).setFavorite(true);
        mAdapter.notifyDataSetChanged();
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

    public String replaceChar(String str, char ch, int index) {
        StringBuilder myString = new StringBuilder(str);
        myString.setCharAt(index, ch);
        return myString.toString();
    }


    class GetCityList extends AsyncTask<String, Void, HashMap<String,String>> {
        @Override


        protected HashMap<String, String> doInBackground(String... strings) {

            HttpURLConnection connection = null;
            try {
                String urlString = strings[0];
                Log.d(TAG, "URL = " + urlString);

                URL url = new URL(urlString);

                Map<String,String> cityDetails = new HashMap<String, String>();

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "status ok");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);
                    for(int i = 0;i<root.length();i++){
                        JSONObject object = root.getJSONObject(i);
                        String key = object.getString("Key");
                        Log.d(TAG, "key: " + key);
                        StringBuilder city = new StringBuilder();
                        city.append(object.getString("LocalizedName"));
                        city.append(", ");
                        JSONObject adminArea = object.getJSONObject("AdministrativeArea");
                        city.append(adminArea.getString("ID"));
                        Log.d(TAG, "city location details: " + city.toString());

                        cityDetails.put(key, city.toString());
                    }

                }

                return (HashMap<String, String>) cityDetails;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> stringStringHashMap) {
            super.onPostExecute(stringStringHashMap);

            final String[] list = new String[stringStringHashMap.size()];
            Iterator iterator = stringStringHashMap.entrySet().iterator();
            int i=0;
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                list[i++] = entry.getValue().toString();
            }


            cityLocationDetails = stringStringHashMap;


            if(list.length>0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select a Keyword!")
                        .setItems(list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Clicked on: " + list[which]);
                                searchedCity = list[which];
                                if (cityLocationDetails.containsValue(searchedCity)) {
                                    for (Map.Entry entry : cityLocationDetails.entrySet()) {
                                        if (entry.getValue().equals(searchedCity)) {
                                            KEY = entry.getKey().toString();
                                            Intent intent = new Intent(MainActivity.this, CityWeatherActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("key", KEY);
                                            String city = et_city.getText().toString().trim();
                                            city =  replaceChar(city,Character.toTitleCase(city.charAt(0)),0);
                                            String country = et_country.getText().toString().trim();
                                            bundle.putString("city", city);
                                            bundle.putString("country",country.toUpperCase());
                                            intent.putExtra(FORECAST_KEY, bundle);
                                            progressDialog = new ProgressDialog(MainActivity.this);
                                            progressDialog.setTitle("Loading...");
                                            progressDialog.show();
                                            startActivity(intent);
                                        }
                                    }
                                }

                                Log.d(TAG, "key: " + KEY);


                            }
                        });

                builder.create().show();
            }
            else{
                Toast.makeText(MainActivity.this, "City not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class GetCityKey extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                String urlString = strings[0];
                Log.d(TAG, "URL = " + urlString);

                URL url = new URL(urlString);
                String key = null;

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "status ok");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);
                    Log.d(TAG,"root size" + root.length());
                    JSONObject object = root.getJSONObject(0);
                    key = object.getString("Key");
                    Log.d(TAG, "key: " + key);
                }

                return key;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String conditionURL = "http://dataservice.accuweather.com/currentconditions/v1/" + s + "?apikey=" + ApiKey;
            new GetWeatherData().execute(conditionURL);
        }
    }

    class GetWeatherData extends AsyncTask<String, Void, City> {
        @Override
        protected City doInBackground(String... strings) {
            City city = new City();
            HttpURLConnection connection = null;

            try {
                String urlString = strings[0];
                Log.d(TAG, "URL = " + urlString);

                URL url = new URL(urlString);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "status ok");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);

                    JSONObject object = root.getJSONObject(0);
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    city.date = inputFormat.parse(object.getString("LocalObservationDateTime"));
                    PrettyTime pt = new PrettyTime();
                    city.time = pt.format(city.date);
                    city.setWeather(object.getString("WeatherText"));
                    city.setIcon(object.getInt("WeatherIcon"));
                    JSONObject temperature = object.getJSONObject("Temperature");
                    JSONObject metric = temperature.getJSONObject("Imperial");
                    city.setTemperature(metric.getDouble("Value"));
                    city.setUnit(metric.getString("Unit"));

                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return city;
        }

        @Override
        protected void onPostExecute(City city) {
            super.onPostExecute(city);

            tv_1.setVisibility(View.INVISIBLE);
            btn_setCity.setVisibility(View.INVISIBLE);
            tv_CityName.setVisibility(View.VISIBLE);
            tv_Temperature.setVisibility(View.VISIBLE);
            tv_TempVal.setVisibility(View.VISIBLE);
            tv_Updated.setVisibility(View.VISIBLE);
            tv_Weather.setVisibility(View.VISIBLE);
            tv_updatedVal.setVisibility(View.VISIBLE);
            iv_icon.setVisibility(View.VISIBLE);


            tv_CityName.setText(adCity + ", " + adCountry);
            tv_TempVal.setText(city.getTemperature()+ " " +city.getUnit());
            tv_updatedVal.setText(city.getTime());
            tv_Weather.setText(city.getWeather());
            String imgURL;
            if(city.getIcon() < 10)
                imgURL  =  "http://developer.accuweather.com/sites/default/files/" + "0" + city.getIcon() + "-s.png";
            else
                imgURL = "http://developer.accuweather.com/sites/default/files/" + city.getIcon() + "-s.png";

            Picasso.get().load(imgURL).into(iv_icon);

            aprogressDialog.dismiss();

        }
    }

}
