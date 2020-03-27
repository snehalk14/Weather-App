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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    public static iForecast displayForecast;
    ArrayList<Forecast> forecasts;

    public ForecastAdapter(ArrayList<Forecast> forecasts,iForecast displayForecast ) {
        this.forecasts = forecasts;
        this.displayForecast = displayForecast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast forecast = forecasts.get(position);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM''yy");
        String formattedDate = outputFormat.format(forecasts.get(position).getDate());
        holder.tv_date.setText(formattedDate);
        String icon;
        if(forecast.getDayIcon() < 10)
            icon = "0" + forecast.getDayIcon();
        else
            icon = forecast.getDayIcon() +"";
        String imgUrl = "http://developer.accuweather.com/sites/default/files/" + icon + "-s.png";
        Picasso.get().load(imgUrl).into(holder.iv_icon);

    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date;
        ImageView iv_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo", "onClick " + getAdapterPosition());
                    displayForecast.setForecast(getAdapterPosition());
                    displayForecast.saveCity(getAdapterPosition());
                }
            });
        }
    }

    public interface iForecast{
        public void setForecast(int position);
        public void saveCity(int poition);
    }

}
