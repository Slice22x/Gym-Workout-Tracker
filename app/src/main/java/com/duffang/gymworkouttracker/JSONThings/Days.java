package com.duffang.gymworkouttracker.JSONThings;

import android.content.Context;

import com.duffang.gymworkouttracker.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Days extends MainActivity.ConveterToJSON {
    public final String date;
    public final DayWorkout thisDayWorkout;

    public Days(JSONFileTypes thisType, String date, DayWorkout thisDayWorkout) {
        this.thisType = thisType;
        this.date = date;
        this.thisDayWorkout = thisDayWorkout;
    }

    @Override
    public JSONObject ConvertToJSON(Context context) {
        JSONObject file = new JSONObject();

        try {
            file.put("Type", thisType);
            file.put("date", date);
            file.put("thisDaysWorkout", thisDayWorkout.dayName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @Override
    public String getAppropriateFileName() {
        return date + "_Data.json";
    }
}
