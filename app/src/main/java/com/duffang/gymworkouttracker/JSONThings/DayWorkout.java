package com.duffang.gymworkouttracker.JSONThings;

import android.content.Context;
import android.net.Uri;

import com.duffang.gymworkouttracker.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DayWorkout extends MainActivity.ConveterToJSON implements Serializable {
    public final String dayName;
    public final ArrayList<Exercise> exercises;
    public final String imagePath;

    public DayWorkout(JSONFileTypes thisType, String dayName, ArrayList<Exercise> exercises, String imagePath) {
        this.imagePath = imagePath;
        this.thisType = thisType;
        this.dayName = dayName;
        this.exercises = exercises;
    }

    @Override
    public JSONObject ConvertToJSON(Context context) {
        JSONObject file = new JSONObject();
        try {
            file.put("Type", thisType);
            file.put("dayName", dayName);
            JSONArray exerciseArray = new JSONArray();
            for (int i = 0; i < exercises.size(); i++){
                exerciseArray.put(exercises.get(i).name);
            }
            file.put("exercises", exerciseArray);
            file.put("icon", imagePath.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    @Override
    public String getAppropriateFileName() {
        return dayName+"_Data.json";
    }

}
