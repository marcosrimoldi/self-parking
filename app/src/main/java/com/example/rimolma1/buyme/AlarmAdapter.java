package com.example.rimolma1.buyme;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 */
public class AlarmAdapter extends CustomAdapter<Alarm> {

    public AlarmAdapter(Activity activity, ArrayList<Alarm> objects) {
        super(activity, objects);
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
                //holder.display_number = (TextView) vi.findViewById(R.id.thirdLine);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.display_name.setText(getObjectList().get(position).getDescription());
            //holder.display_number.setText(getObjectList().get(position).getDescription());


        } catch (Exception e) {


        }
        return vi;
    }

}
