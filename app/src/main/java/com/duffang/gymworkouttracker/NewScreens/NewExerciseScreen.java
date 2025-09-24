package com.duffang.gymworkouttracker.NewScreens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.JSONThings.Settings;
import com.duffang.gymworkouttracker.MainActivity;
import com.duffang.gymworkouttracker.R;
import com.duffang.gymworkouttracker.SettingsScreen;

public class NewExerciseScreen extends AppCompatActivity {

    Exercise exercise;

    TextView tvExists, tvRecentText, tvUnits;
    EditText etExerciseName, etExerciseWeight, etExerciseMin, etExerciseMax, etExerciseChange, etExerciseRecent;
    Button btCancel, btSave;

    String name;
    int min, max;
    float weight, change, recent;
    Boolean exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_exersise_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        exercise = (Exercise) intent.getSerializableExtra("Editing_Exercise");

        tvExists = findViewById(R.id.tvExists);
        tvRecentText = findViewById(R.id.tvExerciseRecent);
        tvUnits = findViewById(R.id.tvCurrentUnit);

        etExerciseName = findViewById(R.id.etExerciseName);
        etExerciseWeight = findViewById(R.id.etExerciseWeight);
        etExerciseMin = findViewById(R.id.etExerciseRangeMin);
        etExerciseMax = findViewById(R.id.etExerciseRangeMax);
        etExerciseChange = findViewById(R.id.etExerciseChange);
        etExerciseRecent = findViewById(R.id.etExerciseRecent);

        btCancel = findViewById(R.id.btCancelButton);
        btSave = findViewById(R.id.btSaveButton);

        etExerciseName.setFocusable(true);
        tvRecentText.setVisibility(View.INVISIBLE);
        etExerciseRecent.setVisibility(View.INVISIBLE);

        if (exercise != null){
            etExerciseName.setText(exercise.name);
            name = exercise.name;
            etExerciseName.setFocusable(false);

            switch (Settings.getCurrentUnit(this)){
                case KG:
                    etExerciseWeight.setText(String.valueOf(exercise.baseWeight));
                    weight = exercise.baseWeight;
                    etExerciseChange.setText(String.valueOf(exercise.changeWeightBy));
                    change = exercise.changeWeightBy;
                    etExerciseRecent.setText(String.valueOf(exercise.recentWeight));
                    recent = exercise.recentWeight;
                    break;
                case LBS:
                    etExerciseWeight.setText(String.valueOf(exercise.baseWeightLbs));
                    weight = exercise.baseWeightLbs;
                    etExerciseChange.setText(String.valueOf(exercise.changeWeightByLbs));
                    change = exercise.changeWeightByLbs;
                    etExerciseRecent.setText(String.valueOf(exercise.recentWeightLbs));
                    recent = exercise.recentWeightLbs;
                    break;
            }

            etExerciseMin.setText(String.valueOf(exercise.minRepRange));
            min = exercise.minRepRange;
            etExerciseMax.setText(String.valueOf(exercise.maxRepRange));
            max = exercise.maxRepRange;

            tvRecentText.setVisibility(View.VISIBLE);
            etExerciseRecent.setVisibility(View.VISIBLE);
        }

        String m = "Current Units: " + Settings.getCurrentUnit(this).toString();

        tvUnits.setText(m);

        exist = false;

        etExerciseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(exercise != null){
                    exist = false;
                    return;
                }

                name = editable.toString();

                Exercise t = (Exercise) JSONFileHandler.ReadFromJSONFile(getApplicationContext(), MainActivity.usingCharacterToUpperCaseMethod(name)+"_Data.json", JSONFileTypes.Exercise);

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

        etExerciseMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    min = Integer.parseInt(editable.toString());
                }
            }
        });

        etExerciseMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    max = Integer.parseInt(editable.toString());
                }
            }
        });

        etExerciseWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    weight = Float.parseFloat(editable.toString());
                }
            }
        });

        etExerciseChange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    change = Float.parseFloat(editable.toString());
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = null;
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etExerciseName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: Name is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etExerciseWeight.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: Weight is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etExerciseMin.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: Min Range is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etExerciseMax.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: Max Range is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etExerciseChange.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Failed: Change Weight is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!exist){
                    Exercise exerciseC = new Exercise(JSONFileTypes.Exercise,MainActivity.usingCharacterToUpperCaseMethod(name), weight, exercise != null ? recent : weight, min, max, change, Settings.getCurrentUnit(getApplicationContext()));

                    JSONFileHandler.CreateNewJSONFile(getApplicationContext(), exerciseC);

                    String newE = "Successfully Made New Exercise: " + exerciseC.name;
                    String changeE = "Successfully Made Changes";

                    Toast.makeText(getApplicationContext(),exercise == null ? newE : changeE, Toast.LENGTH_LONG).show();

                    exercise = null;

                    finish();
                }

                if(exist) {
                    Toast.makeText(getApplicationContext(), "Failed: " + MainActivity.usingCharacterToUpperCaseMethod(name) + "Exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}