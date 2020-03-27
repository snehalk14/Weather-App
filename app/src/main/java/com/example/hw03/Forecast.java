//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane
package com.example.hw03;

import java.util.Date;

public class Forecast {
    int dayIcon,nightIcon;
    Double minTemp,maxTemp;
    Date date;
    String dayPhrase,nightPhrase;
    String mobileLink;


    public int getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(int dayIcon) {
        this.dayIcon = dayIcon;
    }

    public int getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(int nightIcon) {
        this.nightIcon = nightIcon;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "dayIcon=" + dayIcon +
                ", nightIcon=" + nightIcon +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", date=" + date +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", nightPhrase='" + nightPhrase + '\'' +
                '}';
    }
}
