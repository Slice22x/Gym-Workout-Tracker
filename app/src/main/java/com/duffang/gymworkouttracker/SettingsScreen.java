package com.duffang.gymworkouttracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duffang.gymworkouttracker.JSONThings.Exercise;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.JSONThings.Settings;

import java.util.ArrayList;

public class SettingsScreen extends AppCompatActivity {

    Settings currentSetting;

    Button btKG, btLBS, btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btKG = findViewById(R.id.btSetKg);
        btLBS = findViewById(R.id.btSetLbs);
        btSave = findViewById(R.id.btSaveButton);

        Settings s = (Settings) JSONFileHandler.ReadFromJSONFile(this, "Settings_Data.json", JSONFileTypes.Settings);

        if(s == null){
            currentSetting = new Settings(JSONFileTypes.Settings, Settings.WeightUnit.KG);
            JSONFileHandler.CreateNewJSONFile(this, currentSetting);
        }
        else {
            currentSetting = s;
            switch (currentSetting.unit) {
                case KG:
                    btKG.setBackgroundColor(Color.parseColor("#00C853"));
                    btLBS.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    break;
                case LBS:
                    btLBS.setBackgroundColor(Color.parseColor("#00C853"));
                    btKG.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    break;
            }
        }

        btKG.setOnClickListener(view -> {
            currentSetting.unit = Settings.WeightUnit.KG;
            btKG.setBackgroundColor(Color.parseColor("#00C853"));
            btLBS.setBackgroundColor(Color.parseColor("#9E9E9E"));
        });

        btLBS.setOnClickListener(view -> {
            currentSetting.unit = Settings.WeightUnit.LBS;
            btLBS.setBackgroundColor(Color.parseColor("#00C853"));
            btKG.setBackgroundColor(Color.parseColor("#9E9E9E"));
        });

        btSave.setOnClickListener(view -> {
            JSONFileHandler.WriteToJSONFile(this, currentSetting.ConvertToJSON(this), currentSetting);

            ArrayList<Exercise> exercises = JSONFileHandler.getAllExercises(getApplicationContext());

            for (int i = 0; i < exercises.size(); i++){
                Exercise e = exercises.get(i);

                Settings.WeightUnit unit = null;

                if(e.unit != null) {
                    unit = e.unit;
                }

                Exercise ex = new Exercise(e.thisType, e.name, e.baseWeight, e.recentWeight, e.minRepRange, e.maxRepRange, e.changeWeightBy, unit);
                ex.itemPath = e.itemPath;

                JSONFileHandler.WriteToJSONFile(this, ex.ConvertToJSON(this), ex);
            }

            finish();
        });
    }

}