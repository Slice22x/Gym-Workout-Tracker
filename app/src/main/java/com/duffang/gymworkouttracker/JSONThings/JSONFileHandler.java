package com.duffang.gymworkouttracker.JSONThings;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.duffang.gymworkouttracker.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class JSONFileHandler {
    public static void CreateNewJSONFile(Context context, MainActivity.ConveterToJSON fileToMake){
        File file = new File(Environment.getExternalStorageDirectory() + "/GymApp");
        file.mkdir();

        File mainFilesHolder = new File(getPath(fileToMake));
        mainFilesHolder.mkdir();

        File fileHandler = new File(getPath(fileToMake) + "/" + fileToMake.getAppropriateFileName());

        try {
            boolean b = fileHandler.createNewFile();
            WriteToJSONFile(context, fileToMake.ConvertToJSON(context), fileToMake);
            if(b){
                Log.i("JSONFileHandler", "Created Successfully");
            }
        } catch (IOException e) {
            Log.e("JSONFileHandler", "Couldn't make file");
            throw new RuntimeException(e);
        }
    }

    public static void WriteToJSONFile(Context context, JSONObject object, MainActivity.ConveterToJSON fileToMake){
        File file = new File(Environment.getExternalStorageDirectory() + "/GymApp");
        file.mkdir();

        File filePath = new File(getPath(fileToMake) + "/" + fileToMake.getAppropriateFileName());

        if(filePath.exists()){
            try {
                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(object.toString());
                fileWriter.flush();
                fileWriter.close();
            }
            catch(IOException e){
                Log.e("JSONFileHandler", "Something Went Wrong");
            }
        }
        else {
            Toast.makeText(context, "No File Exists", Toast.LENGTH_SHORT).show();
        }
    }

    public static MainActivity.ConveterToJSON ReadFromJSONFile(Context context, String fileName, JSONFileTypes type){
        File file = new File(Environment.getExternalStorageDirectory() + "/GymApp");
        file.mkdir();

        File filePath = new File(getPath(type) + "/" +  fileName);

        if(!filePath.exists()){
            return null;
        }

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String response = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject(response);
            JSONFileTypes thisType = JSONFileTypes.valueOf(jsonObject.get("Type").toString());

            MainActivity.ConveterToJSON data = null;

            switch (type){
                case DaysWorkout:
                    String dayName = jsonObject.get("dayName").toString();
                    JSONArray names = jsonObject.getJSONArray("exercises");
                    ArrayList<Exercise> e = new ArrayList<>();

                    for (int i = 0; i < names.length(); i++){
                        e.add((Exercise) ReadFromJSONFile(context,names.get(i).toString()+"_Data.json", JSONFileTypes.Exercise));
                    }

                    Uri imagePath = Uri.EMPTY;

                    if(!jsonObject.isNull("icon")){
                        imagePath = Uri.parse(jsonObject.get("icon").toString());
                    }

                    data = new DayWorkout(JSONFileTypes.DaysWorkout, dayName, e, imagePath.toString());

                    break;
                case Days:
                    String date = jsonObject.get("date").toString();
                    String thisDayName = jsonObject.get("thisDaysWorkout").toString();
                    DayWorkout dayWorkout = (DayWorkout) ReadFromJSONFile(context,thisDayName+"_Data.json", JSONFileTypes.DaysWorkout);

                    data = new Days(JSONFileTypes.Days, date, dayWorkout);
                    break;
                case Exercise:
                    String name = jsonObject.get("name").toString();
                    float baseWeight = Float.parseFloat(jsonObject.get("baseWeight").toString());
                    float recentWeight = Float.parseFloat(jsonObject.get("recentWeight").toString());
                    int minRepRange = Integer.parseInt(jsonObject.get("minRepRange").toString());
                    int maxRepRange = Integer.parseInt(jsonObject.get("maxRepRange").toString());
                    float changeWeightBy = Float.parseFloat(jsonObject.getString("changeWeightBy"));
                    Settings.WeightUnit unitS = null;
                    if(!jsonObject.isNull("unit")) unitS = Settings.WeightUnit.valueOf(jsonObject.get("unit").toString());

                    Uri imPath = Uri.EMPTY;

                    if(!jsonObject.isNull("path")){
                        imPath = Uri.parse(jsonObject.get("path").toString());
                    }

                    Exercise ex = new Exercise(JSONFileTypes.Exercise, name, baseWeight, recentWeight, minRepRange, maxRepRange, changeWeightBy, unitS);
                    ex.itemPath = imPath.toString();
                    data = ex;

                    break;
                case Settings:
                    Settings.WeightUnit unit = Settings.WeightUnit.valueOf(jsonObject.get("unit").toString());

                    data = new Settings(JSONFileTypes.Settings, unit);
                    break;
            }

            return data;

        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static ArrayList<String> getNamesOfFileInDir(String dir){
        ArrayList<String> results = new ArrayList<>();


        File[] files = new File(dir).listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        return results;
    }

    public static String getPath(MainActivity.ConveterToJSON fileToMake){
        String extention = "";

        switch (fileToMake.thisType){
            case DaysWorkout:
                extention = "/Workout";
                break;
            case Days:
                extention = "/Days";
                break;
            case Exercise:
                extention = "/Exercise";
                break;
            case Settings:
                extention = "/Settings";
                break;
        }

        return Environment.getExternalStorageDirectory() + "/GymApp" + extention;
    }

    public static ArrayList<Exercise> getAllExercises(Context context){
        ArrayList<String> names = getNamesOfFileInDir(getPath(JSONFileTypes.Exercise));
        ArrayList<Exercise> exercises = new ArrayList<>();

        for (int i = 0; i < names.size(); i++){
            exercises.add((Exercise) ReadFromJSONFile(context, names.get(i), JSONFileTypes.Exercise));
        }

        return exercises;
    }

    public static ArrayList<DayWorkout> getAllWorkouts(Context context){
        ArrayList<String> names = getNamesOfFileInDir(getPath(JSONFileTypes.DaysWorkout));
        ArrayList<DayWorkout> exercises = new ArrayList<>();

        for (int i = 0; i < names.size(); i++){
            exercises.add((DayWorkout) ReadFromJSONFile(context,names.get(i), JSONFileTypes.DaysWorkout));
        }

        return exercises;
    }

    public static String getPath(JSONFileTypes type){
        String extention = "";

        switch (type){
            case DaysWorkout:
                extention = "/Workout";
                break;
            case Days:
                extention = "/Days";
                break;
            case Exercise:
                extention = "/Exercise";
                break;
            case Settings:
                extention = "/Settings";
                break;
        }

        return Environment.getExternalStorageDirectory() + "/GymApp" + extention;
    }

    public static int getFilesInDir(String dir){
        File dirName = new File(dir);


        return Objects.requireNonNull(dirName.list()).length;
    }

}
