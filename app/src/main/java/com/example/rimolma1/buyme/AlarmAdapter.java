package com.example.rimolma1.buyme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private Activity activity;
    private ArrayList<Alarm> alarmList;
    private static LayoutInflater inflater = null;

    public AlarmAdapter(Activity activity, int textViewResourceId, ArrayList<Alarm> _alarmList) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            this.alarmList = _alarmList;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return alarmList.size();
    }

    public Alarm getAlarm(int position) {
        return alarmList.get(position);
    }

    @Override
    public Alarm getItem(int position) {
        return getAlarm(position);
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.alert_list_item_view, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.secondLine);
                holder.display_number = (TextView) vi.findViewById(R.id.thirdLine);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.display_name.setText(alarmList.get(position).getDescription());
            holder.display_number.setText("TEST.");


        } catch (Exception e) {


        }
        return vi;
    }




}
