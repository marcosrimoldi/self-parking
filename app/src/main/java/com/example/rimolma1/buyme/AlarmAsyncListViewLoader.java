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

        Alarm alarm = new Alarm(obj.getString("location"));

        //User creator = new User();
        //creator.setId(obj.getJSONObject("creator").getString("id"));
        //creator.setName(obj.getJSONObject("creator").getString("name"));

        alarm.setLocation(obj.getString("location"));
        return alarm;
    }

}