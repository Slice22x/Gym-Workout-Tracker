package com.duffang.gymworkouttracker.NewScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.R;

public class ExerciseDialogueAdapter extends RecyclerView.Adapter<ExerciseDialogueAdapter.ThisViewHolder> {

    Context context;
    ExerciseDialogueAdapter.OnItemListener onItemListener;

    public ExerciseDialogueAdapter(Context context, OnItemListener listener) {
        this.context = context;
        this.onItemListener = listener;
    }

    @NonNull
    @Override
    public ThisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.created_exercise_name, parent, false);
        return new ThisViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ThisViewHolder holder, int position) {
        holder.tvName.setText(JSONFileHandler.getNamesOfFileInDir(JSONFileHandler.getPath(JSONFileTypes.Exercise)).get(position).replace("_Data.json", ""));
        holder.exercise = (Exercise) JSONFileHandler.ReadFromJSONFile(context,JSONFileHandler.getNamesOfFileInDir(JSONFileHandler.getPath(JSONFileTypes.Exercise)).get(position), JSONFileTypes.Exercise);
    }

    @Override
    public int getItemCount() {
        return JSONFileHandler.getFilesInDir(JSONFileHandler.getPath(JSONFileTypes.Exercise));
    }

    public interface OnItemListener{
        void onItemClick (Exercise exercise);
    }

    public static class ThisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        ExerciseDialogueAdapter.OnItemListener onItemClick;
        Exercise exercise;

        public ThisViewHolder(@NonNull View itemView, ExerciseDialogueAdapter.OnItemListener onItemListener) {
            super(itemView);
            onItemClick = onItemListener;
            tvName = itemView.findViewById(R.id.tvDialogueName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick.onItemClick(exercise);
        }
    }
}
