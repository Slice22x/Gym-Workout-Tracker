package com.duffang.gymworkouttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duffang.gymworkouttracker.Calendar.CalendarAdapter;
import com.duffang.gymworkouttracker.Days.MainActivityDay;
import com.duffang.gymworkouttracker.JSONThings.JSONFileHandler;
import com.duffang.gymworkouttracker.JSONThings.JSONFileTypes;
import com.duffang.gymworkouttracker.NewScreens.DaysWorkoutChanger;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    TextView tvMonthYear;
    RecyclerView rvCellsHolder;
    LocalDate selectedDate;

    ImageButton btLeft, btRight, btAdd, btSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
        }

        //init();

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        btLeft = findViewById(R.id.btLeftButton);
        btRight = findViewById(R.id.btRightButton);
        btAdd = findViewById(R.id.btAddButton);
        btSettings = findViewById(R.id.btSettingsButton);

        com.duffang.gymworkouttracker.JSONThings.Settings s = (com.duffang.gymworkouttracker.JSONThings.Settings) JSONFileHandler.ReadFromJSONFile(this, "Settings_Data.json", JSONFileTypes.Settings);

        if(s == null){
            com.duffang.gymworkouttracker.JSONThings.Settings currentSetting = new com.duffang.gymworkouttracker.JSONThings.Settings(JSONFileTypes.Settings, com.duffang.gymworkouttracker.JSONThings.Settings.WeightUnit.KG);
            JSONFileHandler.CreateNewJSONFile(this, currentSetting);
        }

        btLeft.setOnClickListener(view -> {
            selectedDate = selectedDate.minusMonths(1);
            setMonthView();
        });

        btRight.setOnClickListener(view -> {
            selectedDate = selectedDate.plusMonths(1);
            setMonthView();
        });

        btSettings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingsScreen.class);
            startActivity(intent);
        });

        Log.i("MainActivity",String.valueOf(LocalDate.now()));

        btAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DaysWorkoutChanger.class);
            startActivity(intent);
        });
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        //init();

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        btLeft = findViewById(R.id.btLeftButton);
        btRight = findViewById(R.id.btRightButton);
        btAdd = findViewById(R.id.btAddButton);

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });
    }*/

    /*void init(){
        Exercise benchPress = new Exercise(JSONFileTypes.Exercise,"Bench Press",100f, 105f, 3, 5, 1.25f);
        Exercise inclineDumbbellPress = new Exercise(JSONFileTypes.Exercise,"Incline Dumbbell Press",32f, 28f, 7, 9, 2f);
        Exercise dips = new Exercise(JSONFileTypes.Exercise,"Dips",94f, 95f, 11, 15, 10f);

        ArrayList<Exercise> exercise = new ArrayList<>();
        exercise.add(benchPress);
        exercise.add(inclineDumbbellPress);
        exercise.add(dips);

        DayWorkout push = new DayWorkout(JSONFileTypes.DaysWorkout, "Push", exercise);

        JSONFileHandler.CreateNewJSONFile(this, benchPress);
        JSONFileHandler.CreateNewJSONFile(this, inclineDumbbellPress);
        JSONFileHandler.CreateNewJSONFile(this, dips);

        JSONFileHandler.CreateNewJSONFile(this, push);

        DayWorkout o = (DayWorkout) JSONFileHandler.ReadFromJSONFile(this, push.getAppropriateFileName(), push.thisType);
        Log.i("MainActivity", String.valueOf(o.exercises.get(0).baseWeight));
    }*/

    private void initWidgets() {
        rvCellsHolder = findViewById(R.id.rvCellsHolder);
        tvMonthYear = findViewById(R.id.tvMonthYear);
    }

    private void setMonthView() {
        tvMonthYear.setText(monthYearFromText(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(this, daysInMonth, this, selectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        rvCellsHolder.setLayoutManager(layoutManager);
        rvCellsHolder.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDay = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }
            else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    String monthYearFromText(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.isEmpty()){
            String message = dayText + " " + monthYearFromText(selectedDate);
            Toast.makeText(this, "Selected Date " + message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MainActivityDay.class);

            intent.putExtra("Selected_Date", message);
            startActivity(intent);
        }
    }

    public static class ConveterToJSON{
        public JSONFileTypes thisType;

        public JSONObject ConvertToJSON(Context context){
            return null;
        }

        public String getAppropriateFileName(){
            return "";
        }
    }

    public static String usingCharacterToUpperCaseMethod(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Arrays.stream(input.split("\\s+"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}