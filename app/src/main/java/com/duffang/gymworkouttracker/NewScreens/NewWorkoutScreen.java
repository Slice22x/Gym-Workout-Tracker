package com.duffang.gymworkouttracker.NewScreens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.JSONThings.DayWorkout;
import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.MainActivity;
import com.duffang.gymworkouttracker.R;

import java.util.ArrayList;
import java.util.Objects;

public class NewWorkoutScreen extends AppCompatActivity implements ExerciseDialogueAdapter.OnItemListener {

    DayWorkout workout;

    RecyclerView rvHolder;

    EditText etName;
    Button btNew;
    Button btCancel, btSave;

    ImageButton btIcon;

    TextView tvExists;

    String name;
    public ArrayList<Exercise> exercises;
    public Exercise chosenExercise;

    Boolean exist;
    Dialog dialog;

    Uri imagePath;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK && result.getData() != null){
            Uri image = result.getData().getData();
            imagePath = image;
            btIcon.setImageURI(image);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_workout_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        exercises = new ArrayList<>();
        name = "";

        rvHolder = findViewById(R.id.rvExercisesHolder);

        tvExists = findViewById(R.id.tvExists);

        etName = findViewById(R.id.etWorkoutName);

        btCancel = findViewById(R.id.btCancelButton);
        btSave = findViewById(R.id.btSaveButton);
        btNew = findViewById(R.id.btAddExButton);
        btIcon = findViewById(R.id.btWorkoutIcon);

        etName.setFocusable(true);

        workout = (DayWorkout) getIntent().getSerializableExtra("Editing_Workout");

        if (workout != null){
            etName.setText(workout.dayName);
            name = workout.dayName;
            exercises = workout.exercises;
            imagePath = Uri.parse(workout.imagePath);
            btIcon.setImageURI(Uri.parse(workout.imagePath));

            WorkoutExerciseAdapter adapter = new WorkoutExerciseAdapter(getApplicationContext(), exercises, this);
            rvHolder.setAdapter(adapter);
            rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

        dialog = new Dialog(NewWorkoutScreen.this);
        dialog.setContentView(R.layout.exercise_dialogue_box);
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = dialog.findViewById(R.id.rvDiaExHolder);
        ExerciseDialogueAdapter adapter = new ExerciseDialogueAdapter(NewWorkoutScreen.this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        exist = false;

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (workout != null){
                    exist = false;
                    return;
                }

                name = editable.toString();

                DayWorkout t = (DayWorkout) JSONFileHandler.ReadFromJSONFile(getApplicationContext(), MainActivity.usingCharacterToUpperCaseMethod(name)+"_Data.json", JSONFileTypes.DaysWorkout);

                if(t != null){
                    String s = "Already Exists";

                    tvExists.setText(s);
                    exist = true;
                }
                else {
                    tvExists.setText("");
                    exist = false;
                }
            }
        });

        btCancel.setOnClickListener(view -> finish());

        btNew.setOnClickListener(view -> dialog.show());

        btIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityResult.launch(intent);
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: " + "No Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if(exercises.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: " + "No Exercises", Toast.LENGTH_LONG).show();
                    return;
                }

                if(imagePath == null){
                    Toast.makeText(getApplicationContext(), "Failed: " + "No Icon", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!exist){
                    DayWorkout workoutC = new DayWorkout(JSONFileTypes.DaysWorkout,name, exercises, imagePath.toString());

                    for (int i = 0; i < exercises.size(); i++){
                        exercises.get(i).itemPath = imagePath.toString();
                        exercises.get(i).thisType = JSONFileTypes.Exercise;
                        JSONFileHandler.WriteToJSONFile(getApplicationContext(),exercises.get(i).ConvertToJSON(getApplicationContext()), exercises.get(i));
                    }

                    JSONFileHandler.CreateNewJSONFile(getApplicationContext(), workoutC);

                    String newE = "Successfully Made New Exercise: " + workoutC.dayName;
                    String changeE = "Successfully Made Changes";

                    Toast.makeText(getApplicationContext(),workout == null ? newE : changeE, Toast.LENGTH_LONG).show();

                    finish();
                }

                if(exist) {
                    Toast.makeText(getApplicationContext(), "Failed: " + MainActivity.usingCharacterToUpperCaseMethod(name) + " Already Exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(Exercise exercise) {
        chosenExercise = exercise;
        Log.i("NewWorkoutScreen", exercises.toString());
        updateChosenExercises();
        dialog.dismiss();
    }

    void updateChosenExercises(){
        exercises.add(chosenExercise);
        chosenExercise = null;

        WorkoutExerciseAdapter adapter = new WorkoutExerciseAdapter(getApplicationContext(), exercises, this);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void removeExercise(int index){
        exercises.remove(index);

        WorkoutExerciseAdapter adapter = new WorkoutExerciseAdapter(getApplicationContext(), exercises, this);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}