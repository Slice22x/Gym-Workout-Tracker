package com.duffang.gymworkouttracker.NewScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.R;

import java.util.ArrayList;

public class WorkoutExerciseAdapter extends RecyclerView.Adapter<WorkoutExerciseAdapter.ViewHolder> {
    Context context;
    ArrayList<Exercise> exercises;
    NewWorkoutScreen screen;

    public WorkoutExerciseAdapter(Context context, ArrayList<Exercise> exercises, NewWorkoutScreen screen) {
        this.context = context;
        this.exercises = exercises;
        this.screen = screen;
    }

    @NonNull
    @Override
    public WorkoutExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_exercise_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutExerciseAdapter.ViewHolder holder, int position) {
        if(exercises.get(position) == null){
            return;
        }

        holder.tvExerciseNameCard.setText(exercises.get(position).name);

        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                screen.removeExercise(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvCardHolder;
        TextView tvExerciseNameCard;
        Button btRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvCardHolder = itemView.findViewById(R.id.cvCardHolder);
            tvExerciseNameCard = itemView.findViewById(R.id.tvExerciseNameCard);
            btRemove = itemView.findViewById(R.id.btCompleteButton);
        }
    }
}
