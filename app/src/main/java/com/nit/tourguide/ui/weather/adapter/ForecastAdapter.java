package com.nit.tourguide.ui.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nit.tourguide.R;
import com.nit.tourguide.pojos.weather.weekly.List;
import com.nit.tourguide.pojos.weather.weekly.Weekly;
import com.squareup.picasso.Picasso;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private static final String TAG = ForecastAdapter.class.getSimpleName();

    private java.util.List<List> weeklyLIst;
    private Context context;

    public ForecastAdapter(java.util.List<List> weeklyLIst, Context context) {
        this.weeklyLIst = weeklyLIst;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.weekly_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        List list = weeklyLIst.get(position);

        holder.dayName.setText(list.getDay());
        holder.maxTemp.setText(String.valueOf(list.getTemp().getMax()) + "\u00B0" + "C");
        holder.minTemp.setText(String.valueOf(list.getTemp().getMin()) + "\u00B0" + "C");
        Picasso.get().load("http://openweathermap.org/img/wn/"+list.getWeather().get(0).getIcon()+"@2x.png")
                .into(holder.conditionIcon);
        holder.dateTimeText.setText(list.getDayString());
        holder.conditonText.setText(list.getWeather().get(0).getDescription());

    }

    @Override
    public int getItemCount() {
        return weeklyLIst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayName,maxTemp,conditonText,dateTimeText,minTemp;
        private ImageView conditionIcon;

        public ViewHolder(@NonNull View convertView) {
            super(convertView);
            dayName = convertView.findViewById(R.id.dayName);
            maxTemp = convertView.findViewById(R.id.maxTemp);
            conditonText = convertView.findViewById(R.id.conditonText);
            dateTimeText = convertView.findViewById(R.id.dateTimeText);
            minTemp = convertView.findViewById(R.id.minTemp);
            conditionIcon = convertView.findViewById(R.id.conditionIcon);
        }
    }
}
