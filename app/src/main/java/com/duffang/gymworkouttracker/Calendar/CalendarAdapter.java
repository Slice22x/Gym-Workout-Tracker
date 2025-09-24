package com.duffang.gymworkouttracker.Calendar;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.DayWorkout;
import com.duffang.gymworkouttracker.JSONThings.Days;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    Context context;
    final ArrayList<String> daysOfMonth;
    final OnItemListener onItemListener;
    final LocalDate thisDate;

    public CalendarAdapter(Context context, ArrayList<String> daysOfMonth, OnItemListener onItemListener, LocalDate thisDate) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.thisDate = thisDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (parent.getHeight() * 0.166666666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String txt = daysOfMonth.get(position);
        holder.tvCellDay.setText(txt);

        Days dayData = (Days) JSONFileHandler.ReadFromJSONFile(context, txt + " " + monthYearFromText(thisDate) + "_Data.json", JSONFileTypes.Days);

        if(dayData != null){
            String path = dayData.thisDayWorkout.imagePath;

            holder.ivIcon.setImageURI(Uri.parse(path));
        }

        if(!txt.isEmpty()){
            int day = Integer.parseInt(txt);
            LocalDate date = thisDate.withDayOfMonth(day);

            if(date.toString().equals(LocalDate.now().toString())){
                Log.i("CalendarAdapter", "Applied");
                holder.clHolder.setBackground(AppCompatResources.getDrawable(context, R.drawable.cell_border));
            }
        }
    }

    String monthYearFromText(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener{
        void onItemClick (int position, String dayText);
    }

}
