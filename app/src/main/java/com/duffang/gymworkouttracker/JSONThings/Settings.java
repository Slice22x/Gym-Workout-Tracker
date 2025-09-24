package com.duffang.gymworkouttracker.JSONThings;

import android.content.Context;

import com.duffang.gymworkouttracker.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Settings extends MainActivity.ConveterToJSON {
    public enum WeightUnit {
        KG,
        LBS
    }

    public WeightUnit unit;

    public Settings (JSONFileTypes thisType, WeightUnit unit){
        this.thisType = thisType;
        this.unit = unit;
    }

    @Override
    public JSONObject ConvertToJSON(Context context) {
        JSONObject file = new JSONObject();

        try {
            file.put("Type", thisType);
            file.put("unit", unit);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @Override
    public String getAppropriateFileName() {
        return "Settings_Data.json";
    }

    public static Settings.WeightUnit getCurrentUnit(Context context){
        Settings s = (Settings) JSONFileHandler.ReadFromJSONFile(context, "Settings_Data.json", JSONFileTypes.Settings);

        assert s != null;
        return s.unit;
    }

    public static int convertKGtoLBS(float kg){
        return round(kg * 2.2046226218488f, 1).intValue();
    }

    public static float convertLBStoKG(float lbs){
        return round(lbs * 0.45359237f, 2).floatValue();
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
