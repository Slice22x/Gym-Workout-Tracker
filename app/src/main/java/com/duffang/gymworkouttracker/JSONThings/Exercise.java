package com.duffang.gymworkouttracker.JSONThings;

import android.content.Context;

import androidx.annotation.Nullable;

import com.duffang.gymworkouttracker.MainActivity;
import com.duffang.gymworkouttracker.SettingsScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

public class Exercise extends MainActivity.ConveterToJSON implements Serializable {
    public String name;
    public float baseWeight, baseWeightLbs;
    public float recentWeight, recentWeightLbs;
    public int minRepRange, maxRepRange;
    public float changeWeightBy, changeWeightByLbs;
    public Settings.WeightUnit unit;
    public String itemPath;


    public Exercise(JSONFileTypes thisType, String name, float baseWeight, float recentWeight, int minRepRange, int maxRepRange, Settings.WeightUnit unit) {
        this.thisType = thisType;
        this.name = name;

        switch (unit){
            case KG:
                this.baseWeight = baseWeight;
                this.recentWeight = recentWeight;
                this.baseWeightLbs = Settings.convertKGtoLBS(baseWeight);
                this.recentWeightLbs = Settings.convertKGtoLBS(recentWeight);
                this.changeWeightBy = 2.5f;
                break;
            case LBS:
                this.baseWeightLbs = baseWeight;
                this.recentWeightLbs = recentWeight;
                this.baseWeight = Settings.convertLBStoKG(baseWeightLbs);
                this.recentWeight = Settings.convertLBStoKG(recentWeightLbs);
                this.changeWeightBy = 5f;
                break;
        }

        this.minRepRange = minRepRange;
        this.maxRepRange = maxRepRange;
        this.unit = unit;
    }

    public Exercise(JSONFileTypes thisType, String name, float baseWeight, float recentWeight, int minRepRange, int maxRepRange, float changeWeightBy, @Nullable Settings.WeightUnit unit) {
        this.thisType = thisType;
        this.name = name;

        if(unit == null) unit = Settings.WeightUnit.KG;

        switch (unit){
            case KG:
                this.baseWeight = baseWeight;
                this.recentWeight = recentWeight;
                this.baseWeightLbs = Settings.convertKGtoLBS(baseWeight);
                this.recentWeightLbs = Settings.convertKGtoLBS(recentWeight);
                this.changeWeightBy = changeWeightBy;
                this.changeWeightByLbs = Settings.convertKGtoLBS(changeWeightBy);
                break;
            case LBS:
                this.baseWeightLbs = baseWeight;
                this.recentWeightLbs = recentWeight;
                this.baseWeight = Settings.convertLBStoKG(baseWeightLbs);
                this.recentWeight = Settings.convertLBStoKG(recentWeightLbs);
                this.changeWeightByLbs = changeWeightBy;
                this.changeWeightBy = Settings.convertLBStoKG(changeWeightBy);
                break;
        }

        this.minRepRange = minRepRange;
        this.maxRepRange = maxRepRange;
        this.unit = unit;
    }

    @Override
    public JSONObject ConvertToJSON(Context context) {
        JSONObject file = new JSONObject();

        try {
            file.put("Type", thisType);
            file.put("name", name);
            file.put("baseWeight", baseWeight);
            file.put("recentWeight", recentWeight);
            file.put("minRepRange", minRepRange);
            file.put("maxRepRange", maxRepRange);
            file.put("changeWeightBy", changeWeightBy);
            file.put("unit", unit);
            if(!itemPath.isEmpty())
                file.put("path", itemPath);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @Override
    public String getAppropriateFileName() {
        return name + "_Data.json";
    }

    public static Comparator<Exercise> ascendingSort = new Comparator<Exercise>() {
        @Override
        public int compare(Exercise exercise1, Exercise exercise2) {
            String name1 = exercise1.name;
            String name2 = exercise2.name;

            return name1.compareTo(name2);
        }
    };

    public static Comparator<Exercise> descendingSort = new Comparator<Exercise>() {
        @Override
        public int compare(Exercise exercise1, Exercise exercise2) {
            String name1 = exercise1.name;
            String name2 = exercise2.name;

            return name2.compareTo(name1);
        }
    };
}
