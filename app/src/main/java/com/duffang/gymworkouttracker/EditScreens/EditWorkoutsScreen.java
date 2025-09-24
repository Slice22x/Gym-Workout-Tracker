package com.duffang.gymworkouttracker.EditScreens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.DayWorkout;
import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.NewScreens.NewExerciseScreen;
import com.duffang.gymworkouttracker.NewScreens.NewWorkoutScreen;
import com.duffang.gymworkouttracker.R;

import java.io.File;

public class EditWorkoutsScreen extends AppCompatActivity implements EditWorkoutsAdapter.OnItemListener {

    RecyclerView rvHolder;
    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_workouts_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvHolder = findViewById(R.id.rvEditWorkoutsHolder);
        btBack = findViewById(R.id.btEditCancelButton);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        EditWorkoutsAdapter adapter = new EditWorkoutsAdapter(EditWorkoutsScreen.this, JSONFileHandler.getAllWorkouts(this),this);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        EditWorkoutsAdapter adapter = new EditWorkoutsAdapter(EditWorkoutsScreen.this, JSONFileHandler.getAllWorkouts(this),this);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onItemClick(DayWorkout workout) {
        Intent intent = new Intent(getApplicationContext(), NewWorkoutScreen.class);
        intent.putExtra("Editing_Workout", workout);
        startActivity(intent);
    }
}