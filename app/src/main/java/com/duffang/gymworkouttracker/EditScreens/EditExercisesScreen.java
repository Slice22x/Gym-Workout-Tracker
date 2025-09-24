package com.duffang.gymworkouttracker.EditScreens;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.NewScreens.NewExerciseScreen;
import com.duffang.gymworkouttracker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditExercisesScreen extends AppCompatActivity implements EditExerciseAdapter.OnItemListener {

    RecyclerView rvHolder;
    Button btBack;
    ImageButton btOrder;

    Boolean ascending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_exercises_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvHolder = findViewById(R.id.rvEditExerciseHolder);
        btBack = findViewById(R.id.btEditCancelButton);
        btOrder = findViewById(R.id.btOrder);

        btBack.setOnClickListener(view -> finish());

        ascending = true;

        btOrder.setOnClickListener(view -> {
            ascending = !ascending;

            Drawable draw = null;

            if(ascending){
                ArrayList<Exercise> exercises = JSONFileHandler.getAllExercises(this);

                exercises.sort(Exercise.ascendingSort);

                EditExerciseAdapter adapter = new EditExerciseAdapter(EditExercisesScreen.this, exercises,this);
                rvHolder.setAdapter(adapter);
                rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                draw = AppCompatResources.getDrawable(this, R.drawable.ascending_icon);
            }
            else {
                ArrayList<Exercise> exercises = JSONFileHandler.getAllExercises(this);

                exercises.sort(Exercise.descendingSort);

                EditExerciseAdapter adapter = new EditExerciseAdapter(EditExercisesScreen.this, exercises,this);
                rvHolder.setAdapter(adapter);
                rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                draw = AppCompatResources.getDrawable(this, R.drawable.descending_icon);
            }

            btOrder.setImageDrawable(draw);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Exercise> exercises = JSONFileHandler.getAllExercises(this);

        exercises.sort(Exercise.ascendingSort);
        EditExerciseAdapter adapter = new EditExerciseAdapter(EditExercisesScreen.this,exercises,this);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onItemClick(Exercise exercise) {
        Intent intent = new Intent(getApplicationContext(), NewExerciseScreen.class);
        intent.putExtra("Editing_Exercise", exercise);
        startActivity(intent);
    }
}