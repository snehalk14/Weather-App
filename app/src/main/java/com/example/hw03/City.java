//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane

package com.example.hw03;

import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class City implements Serializable {
    int icon;
    String cityKey;
    String cityName;
    String weather;
    String time;
    double temperature;
    String unit;
    boolean favorite;
    Date date;

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityKey=" + cityKey +
                ", icon=" + icon +
                ", cityName='" + cityName + '\'' +
                ", weather='" + weather + '\'' +
                ", time='" + time + '\'' +
                ", temperature=" + temperature +
                ", favorite=" + favorite +
                ", date=" + date +
                '}';
    }

}
