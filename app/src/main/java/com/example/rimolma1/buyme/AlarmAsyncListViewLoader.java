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
       /* Map<String,String> map = new HashMap<String,String>();
        Iterator iter = obj.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = obj.getString(key);
            map.put(key,value);
        }

        String title = map.get("title");
        String description = map.get("description");
*/
        return new Alarm(obj.getString("location"));
    }

}