package com.duffang.gymworkouttracker.Days;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.EditScreens.EditWorkoutsAdapter;
import com.duffang.gymworkouttracker.EditScreens.EditWorkoutsScreen;
import com.duffang.gymworkouttracker.JSONThings.DayWorkout;
import com.duffang.gymworkouttracker.JSONThings.Days;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.R;

public class MainActivityDay extends AppCompatActivity implements EditWorkoutsAdapter.OnItemListener {

    String date;
    Days thisDay;

    TextView tvDayName;
    Button btBack;
    RecyclerView rvHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_day);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvDayName = findViewById(R.id.tvEditDay);
        btBack = findViewById(R.id.btEditCancelButton);
        rvHolder = findViewById(R.id.rvHolder);

        date = getIntent().getStringExtra("Selected_Date");

        btBack.setOnClickListener(view -> finish());

        Days gotten = (Days) JSONFileHandler.ReadFromJSONFile(MainActivityDay.this, date + "_Data.json", JSONFileTypes.Days);

        if(gotten == null){
            String s = "Select Workout: ";
            tvDayName.setText(s);

            Log.i("MainActivityDay", tvDayName.getText().toString());

            EditWorkoutsAdapter adapter = new EditWorkoutsAdapter(MainActivityDay.this, JSONFileHandler.getAllWorkouts(this),this);
            rvHolder.setAdapter(adapter);
            rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        else {
            thisDay = gotten;
            DaysAdapter adapter = new DaysAdapter(MainActivityDay.this, thisDay.thisDayWorkout.exercises);
            rvHolder.setAdapter(adapter);
            rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    public void onItemClick(DayWorkout workout) {
        thisDay = new Days(JSONFileTypes.Days, date, workout);
        JSONFileHandler.CreateNewJSONFile(MainActivityDay.this, thisDay);

        DaysAdapter adapter = new DaysAdapter(MainActivityDay.this, thisDay.thisDayWorkout.exercises);
        rvHolder.setAdapter(adapter);
        rvHolder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}