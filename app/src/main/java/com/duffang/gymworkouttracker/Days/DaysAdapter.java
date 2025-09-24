package com.duffang.gymworkouttracker.Days;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.Settings;
import com.duffang.gymworkouttracker.R;

import java.util.ArrayList;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ThisViewHolder> {

    Context context;
    ArrayList<Exercise> exercises;

    public DaysAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ThisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.days_exercise_card, parent, false);
        return new ThisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThisViewHolder holder, int position) {
        Exercise e = exercises.get(position);
        holder.tvName.setText(e.name);
        String m = "";

        switch (Settings.getCurrentUnit(context)){
            case KG:
                m = e.recentWeight + "kg";
                break;
            case LBS:
                m = e.recentWeightLbs + "Lbs";
                break;
        }

        holder.tvWeight.setText(m);
        holder.etHit.setText("");
        holder.cvHolder.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        holder.tvRange.setText(String.format("Range: %s - %s", e.minRepRange, e.maxRepRange));


        holder.etHit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int hit = 0;

                if(!editable.toString().isEmpty()){
                    hit = Integer.parseInt(editable.toString());

                    if(e.minRepRange <= hit && hit <= e.maxRepRange){
                        holder.cvHolder.setCardBackgroundColor(Color.parseColor("#FFFDD835"));
                    }

                    if(e.minRepRange > hit){
                        holder.cvHolder.setCardBackgroundColor(Color.parseColor("#FFE53935"));
                    }

                    if( hit > e.maxRepRange){
                        holder.cvHolder.setCardBackgroundColor(Color.parseColor("#FF7CB342"));
                    }
                }
            }
        });

        holder.btComplete.setOnClickListener(view -> {
            int hit = 0;

            if(!holder.etHit.getText().toString().isEmpty()){
                hit = Integer.parseInt(holder.etHit.getText().toString());

                Log.i("DayAdapter", String.valueOf(e.changeWeightByLbs));

                if(e.minRepRange > hit){
                    switch (Settings.getCurrentUnit(context)){
                        case KG:
                            e.recentWeight -= e.changeWeightBy;
                            break;
                        case LBS:
                            e.recentWeightLbs -= e.changeWeightByLbs;
                            break;
                    }
                }

                if( hit > e.maxRepRange){
                    switch (Settings.getCurrentUnit(context)){
                        case KG:
                            e.recentWeight += e.changeWeightBy;
                            break;
                        case LBS:
                            e.recentWeightLbs += e.changeWeightByLbs;
                            break;
                    }
                }
            }
            JSONFileHandler.WriteToJSONFile(context, e.ConvertToJSON(context), e);
            onBindViewHolder(holder, position);
        });
    }

    @Override
    public int getItemCount() {
        Log.i("DaysAdapter", String.valueOf(exercises.size()));
        return exercises.size();
    }

    public static class ThisViewHolder extends RecyclerView.ViewHolder{

        CardView cvHolder;
        TextView tvName, tvWeight, tvRange;
        EditText etHit;
        Button btComplete;

        public ThisViewHolder(@NonNull View itemView) {
            super(itemView);

            cvHolder = itemView.findViewById(R.id.cvCardHolderEx);

            tvName = itemView.findViewById(R.id.tvExerciseNameCardEx);
            tvWeight = itemView.findViewById(R.id.tvWeightCard);
            tvRange = itemView.findViewById(R.id.tvRangeCard);

            etHit = itemView.findViewById(R.id.etRepsHit);

            btComplete = itemView.findViewById(R.id.btCompleteButton);
        }
    }
}
