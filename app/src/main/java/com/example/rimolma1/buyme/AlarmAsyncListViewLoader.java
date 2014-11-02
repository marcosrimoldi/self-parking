package com.example.rimolma1.buyme;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmAsyncListViewLoader extends AsyncListViewLoader<Alarm> {

    AlarmAsyncListViewLoader(Activity activity, CustomAdapter<Alarm> adapter) {
        super(activity, adapter);
    }

    @Override
    public Alarm convertObject(JSONObject obj) throws JSONException {
        Long id = obj.getLong("id");
        String description = obj.getString("description");

        return new Alarm(id, description);
    }

}