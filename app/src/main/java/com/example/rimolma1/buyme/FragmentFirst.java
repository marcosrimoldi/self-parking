package com.example.rimolma1.buyme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RIMOLMA1 on 10/13/2014.
 */
public class FragmentFirst extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Get ListView object from xml
        listView = (ListView) view.findViewById(R.id.list);

        AlarmAdapter alarmAdapter;
        ArrayList<Alarm> myListItems  = new ArrayList<Alarm>();

        // Populate with hardcoded Alarms list
        for (int i=0; i<10; i++) {
            Alarm alarm = new Alarm();
            alarm.setDescription("Alarm " + i);
            myListItems.add(alarm);
        }

        alarmAdapter = new AlarmAdapter(this.getActivity(), 0, myListItems);
        listView.setAdapter(alarmAdapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                Alarm  item  = (Alarm) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(view.getContext().getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + item.getDescription(), Toast.LENGTH_LONG)
                        .show();

            }

        });

        return view;


    }

}
