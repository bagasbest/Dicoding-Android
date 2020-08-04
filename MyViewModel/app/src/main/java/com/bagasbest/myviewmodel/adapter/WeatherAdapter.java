package com.bagasbest.myviewmodel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.myviewmodel.R;
import com.bagasbest.myviewmodel.model.WeatherItems;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private ArrayList<WeatherItems> listWeather = new ArrayList<>();

    public void setData(ArrayList<WeatherItems> items) {
        listWeather.clear();
        listWeather.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {
        holder.bind(listWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return listWeather.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCity, textViewTemperature, textViewDesc;

         WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCity = itemView.findViewById(R.id.textCity);
            textViewTemperature = itemView.findViewById(R.id.textTemp);
            textViewDesc = itemView.findViewById(R.id.textDesc);
        }

        void bind (WeatherItems weatherItems) {
             textViewCity.setText(weatherItems.getName());
             textViewTemperature.setText(weatherItems.getTemperature());
             textViewDesc.setText(weatherItems.getDescription());
        }
    }
}