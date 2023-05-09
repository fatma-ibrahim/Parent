package org.code.parentsplashscreen.storage;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.code.parentsplashscreen.models.Child;
import org.code.parentsplashscreen.models.Father;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {
    private static SharedPreferencesManager mInstance;
    private Context context;
    private static final String SHARED_PREFERENCES_NAME = "shared_preferences";

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesManager(context);
        }
        return mInstance;
    }

    public void saveUser(Father father) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", father.getId());
        editor.putString("lng", father.getLng());
        editor.putString("lit", father.getLit());
        editor.putString("email", father.getEmail());
        editor.putString("name", father.getName());
        if (father.getTripId() != null) {
            editor.putString("tripId", father.getTripId().toString());
        }
        editor.putString("region", father.getRegion());
        editor.putString("mobileNumber", father.getMobileNumber());
        if (father.getImagePath() != null) {
            editor.putString("image_path", father.getImagePath());
        }
        editor.apply();
    }

    public void SaveChildrenList(List<Child> childList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(childList);
        editor.putString("childList", json);
        editor.apply();
    }

    public List<Child> getChildrenList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        List<Child> arrayItems = new ArrayList<>();
        String serializedObject = sharedPreferences.getString("childList", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Child>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public void saveToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        if (sharedPreferences.getInt("id", -1) != -1) {
            return true;
        }
        return false;
    }

    public Father getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        Father father = new Father(
                sharedPreferences.getString("lng", null),
                sharedPreferences.getString("mobileNumber", null),
                sharedPreferences.getBoolean("confirmed", false),
                sharedPreferences.getString("lit", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("tripId", null),
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("region", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("image_path", null)
        );
        return father;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        return sharedPreferences.getString("token", null);
    }

    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//MODE_PRIVATE keeps the files private and secures the user’s data.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
