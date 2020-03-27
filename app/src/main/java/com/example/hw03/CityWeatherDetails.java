//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane
package com.example.hw03;

import java.io.Serializable;
import java.util.ArrayList;


public class CityWeatherDetails implements Serializable {
    String cityName,country,headline,cityKey,unit;
    ArrayList<Forecast> forecast;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(ArrayList<Forecast> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "CityWeatherDetails{" +
                "cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", headline='" + headline + '\'' +
                ", forecast=" + forecast +
                '}';
    }
}
