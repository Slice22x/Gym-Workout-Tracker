package com.duffang.gymworkouttracker.Calendar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.R;

import java.time.LocalDate;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ConstraintLayout clHolder;
    public final TextView tvCellDay;
    final CalendarAdapter.OnItemListener onItemListener;
    public ImageView ivIcon;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        tvCellDay = itemView.findViewById(R.id.tvCellDay);
        ivIcon = itemView.findViewById(R.id.ivCalendarIcon);
        clHolder = itemView.findViewById(R.id.clHolder);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), (String) tvCellDay.getText());
    }
}
