package com.example.rimolma1.buyme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 */
public abstract class CustomAdapter<T> extends ArrayAdapter<T> {

    private Activity activity;
    private List<T> objectList;
    protected static LayoutInflater inflater = null;

    public CustomAdapter(Activity activity, List<T> objects) {
        super(activity, 0);
        try {
            this.activity = activity;
            this.objectList = objects;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return objectList.size();
    }

    public T getObject(int position) {
        return objectList.get(position);
    }

    @Override
    public T getItem(int position) {
        return getObject(position);
    }

    public void setItemList(List<T> objects) {
        this.objectList = objects;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public List<T> getObjectList() {
        return this.objectList;
    }

    public Activity getActivity() {
        return this.activity;
    }

}
