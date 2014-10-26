package com.example.rimolma1.buyme;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;


public class AddAlarmActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        initActionBar();

        ((RadioButton) findViewById(R.id.radioPlaceType)).performClick();

        initPlaceTypesSpinner();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioPlaceType:
                toggleAddressFields(!checked);
                toggleTypeFields(checked);
                break;
            case R.id.radioPlaceAddress:
                toggleAddressFields(checked);
                toggleTypeFields(!checked);
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        newFragment.setArguments(args);

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void toggleAddressFields(boolean toggle) {
        if (toggle) {
            findViewById(R.id.placeAddress).setVisibility(View.VISIBLE);
            findViewById(R.id.editPlaceAddress).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.placeAddress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editPlaceAddress).setVisibility(View.INVISIBLE);
        }
    }

    private void toggleTypeFields(boolean toggle) {
        if (toggle) {
            findViewById(R.id.placeType).setVisibility(View.VISIBLE);
            findViewById(R.id.placeTypesSpinner).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.placeType).setVisibility(View.INVISIBLE);
            findViewById(R.id.placeTypesSpinner).setVisibility(View.INVISIBLE);
        }
    }

    private void initPlaceTypesSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.placeTypesSpinner);
        // Defined Array values to show in Spinner
        String[] values = new String[] { "Supermercado",
                "Farmacia",
                "Carniceria",
                "Verduleria",
                "Est. de servicio"
        };

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, values);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

}
