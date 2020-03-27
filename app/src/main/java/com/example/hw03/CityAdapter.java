//Homework 03
//File Name: Group12_HW03
//Sanika Pol
//Snehal Kekane
package com.example.hw03;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    ArrayList<City> cities;
    public static MainActivity cityOps;

    public CityAdapter(ArrayList<City> cities, MainActivity cityOps) {
        this.cities = cities;
        this.cityOps = cityOps;
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.city,parent,false);
        CityAdapter.ViewHolder viewHolder = new CityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        City city = cities.get(position);
        Log.d("demo" , "city = "  + city.toString());
        holder.tv_city.setText(city.getCityName());
        holder.tv_temperature.setText(city.getTemperature() + " " +city.getUnit());
        PrettyTime pt = new PrettyTime();
        holder.tv_time.setText(pt.format(city.getDate()));
        if(city.isFavorite()){
            holder.imgbtn_fav.setImageDrawable(ContextCompat.getDrawable(cityOps,android.R.drawable.btn_star_big_on));
            city.setFavorite(true);
        }
        else {
            holder.imgbtn_fav.setImageDrawable(ContextCompat.getDrawable(cityOps,android.R.drawable.btn_star_big_off));
            city.setFavorite(false);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("demo","Size: " + cities.size());
        return cities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_city,tv_temperature,tv_time;
        ImageView imgbtn_fav;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_city = (TextView)itemView.findViewById(R.id.tv_city);
            tv_temperature = (TextView)itemView.findViewById(R.id.tv_temperature);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            imgbtn_fav = (ImageView)itemView.findViewById(R.id.iv_fav);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cityOps.delete(getAdapterPosition());
                    return false;
                }
            });

            imgbtn_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cityOps.setFavorite(getAdapterPosition());
                }
            });

        }
    }

    public interface iCity{
        public void delete(int position);
        public void setFavorite(int position);
    }


}
