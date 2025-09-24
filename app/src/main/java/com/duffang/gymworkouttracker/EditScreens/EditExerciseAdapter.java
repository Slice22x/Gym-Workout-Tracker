package com.duffang.gymworkouttracker.EditScreens;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.R;

import java.util.ArrayList;

public class EditExerciseAdapter extends RecyclerView.Adapter<EditExerciseAdapter.EditViewHolder> {
    Context context;
    ArrayList<Exercise> exercises;
    OnItemListener onItemListener;

    public EditExerciseAdapter(Context context, ArrayList<Exercise> exercises, OnItemListener onItemListener) {
        this.context = context;
        this.exercises = exercises;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_exercise_card, parent, false);
        return new EditViewHolder(view, onItemListener);
    }


    @Override
    public void onBindViewHolder(@NonNull EditViewHolder holder, int position) {
        holder.exercise = exercises.get(position);
        holder.tvName.setText(exercises.get(position).name);
        holder.ivIcon.setImageURI(Uri.parse(exercises.get(position).itemPath));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class EditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        Exercise exercise;
        ImageView ivIcon;
        EditExerciseAdapter.OnItemListener onItemListener;

        public EditViewHolder(@NonNull View itemView, EditExerciseAdapter.OnItemListener onItemListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvEditExerciseNameCard);
            ivIcon = itemView.findViewById(R.id.ivWorkoutIcon);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(exercise);
        }
    }

    public interface OnItemListener{
        void onItemClick (Exercise exercise);
    }
}
