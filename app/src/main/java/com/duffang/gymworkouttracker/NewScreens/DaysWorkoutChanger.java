package com.duffang.gymworkouttracker.NewScreens;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duffang.gymworkouttracker.EditScreens.EditExercisesScreen;
import com.duffang.gymworkouttracker.EditScreens.EditWorkoutsScreen;
import com.duffang.gymworkouttracker.R;

import java.io.File;
import java.util.Objects;

public class DaysWorkoutChanger extends AppCompatActivity {

    ImageButton btBack;

    Button btEditExercise,btEditWorkout, btAddNew;

    Button btNewExercise, btNewWorkout;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_days_workout_changer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btBack = findViewById(R.id.btResultButton);
        btEditExercise = findViewById(R.id.btEditExercise);
        btEditWorkout = findViewById(R.id.btEditWorkout);
        btAddNew = findViewById(R.id.btAddNew);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_dialogue_box);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.new_dialogue_box));

        btNewWorkout = dialog.findViewById(R.id.btNewWorkout);
        btNewExercise = dialog.findViewById(R.id.btNewExercise);

        btEditExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory() + "/GymApp/Exercise");

                if(!file.exists()){
                    Toast.makeText(getApplicationContext(), "Nothing To Show", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), EditExercisesScreen.class);
                startActivity(intent);
            }
        });

        btEditWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory() + "/GymApp/Workout");

                if(!file.exists()){
                    Toast.makeText(getApplicationContext(), "Nothing To Show", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), EditWorkoutsScreen.class);
                startActivity(intent);
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        btNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewExerciseScreen.class);
                startActivity(intent);
            }
        });

        btNewWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewWorkoutScreen.class);
                startActivity(intent);
            }
        });
    }
}