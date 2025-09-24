package com.duffang.gymworkouttracker.EditScreens;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.DayWorkout;
import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.R;

import java.util.ArrayList;

public class EditWorkoutsAdapter extends RecyclerView.Adapter<EditWorkoutsAdapter.EditViewHolder> {
    Context context;
    ArrayList<DayWorkout> workouts;
    EditWorkoutsAdapter.OnItemListener onItemListener;

    public EditWorkoutsAdapter(Context context, ArrayList<DayWorkout> workouts, EditWorkoutsAdapter.OnItemListener onItemListener) {
        this.context = context;
        this.workouts = workouts;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_exercise_card, parent, false);
        return new EditWorkoutsAdapter.EditViewHolder(view, onItemListener);
    }


    @Override
    public void onBindViewHolder(@NonNull EditWorkoutsAdapter.EditViewHolder holder, int position) {
        holder.workout = workouts.get(position);
        holder.tvName.setText(workouts.get(position).dayName);
        holder.ivIcon.setImageURI(Uri.parse(workouts.get(position).imagePath));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class EditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        DayWorkout workout;
        ImageView ivIcon;
        EditWorkoutsAdapter.OnItemListener onItemListener;

        public EditViewHolder(@NonNull View itemView, EditWorkoutsAdapter.OnItemListener onItemListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvEditExerciseNameCard);
            ivIcon = itemView.findViewById(R.id.ivWorkoutIcon);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(workout);
        }
    }

    public interface OnItemListener{
        void onItemClick (DayWorkout workout);
    }
}
