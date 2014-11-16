package com.example.rimolma1.buyme;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class AddAlarmActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        initActionBar();

        ((RadioButton) findViewById(R.id.radioPlaceAddress)).performClick();

        initPlaceTypesSpinner();
        initUsersSpinner();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.editPlaceAddress);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.places_suggest_list, this));
        autoCompView.setOnItemClickListener(this);

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
        if (id == R.id.action_alert_save) {
            String address = ((TextView) findViewById(R.id.editPlaceAddress)).getText().toString();
            Address addressObj = LocationUtils.getLocationFromAddress(this, address);
            String description = ((TextView) findViewById(R.id.editText)).getText().toString();
            String dueDate = ((TextView) findViewById(R.id.dueDateTitle)).getText().toString();
            String fromTime = ((TextView) findViewById(R.id.fromTime)).getText().toString();
            String toTime = ((TextView) findViewById(R.id.toTime)).getText().toString();

            Log.d(this.getClass().toString(), description);
            Log.d(this.getClass().toString(), address);
            Log.d(this.getClass().toString(), Double.toString(addressObj.getLatitude()));
            Log.d(this.getClass().toString(), Double.toString(addressObj.getLongitude()));

            Log.d(this.getClass().toString(), dueDate);
            Log.d(this.getClass().toString(), fromTime);
            Log.d(this.getClass().toString(), toTime);

            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost request = new HttpPost(Constants.POST_NEW_ALARM);
            request.setHeader("Content-type", "application/json");

            JSONObject alertJSON = new JSONObject();
            try {
                alertJSON.put("userId", selectedUser.getId());
                alertJSON.put("description", description);
                alertJSON.put("location", address);
                alertJSON.put("businessArea", addressObj.getAdminArea());
                alertJSON.put("latitude", Double.toString(addressObj.getLatitude()));
                alertJSON.put("longitude", Double.toString(addressObj.getLongitude()));
                alertJSON.put("creatorId", Constants.userID);
                alertJSON.put("title", "title");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                StringEntity se = new StringEntity( alertJSON.toString(), HTTP.UTF_8);
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);

                //request.setEntity(new ByteArrayEntity(alertJSON.toString().getBytes("UTF8")));
                HttpResponse response = client.execute(request);
                Log.d(this.getClass().toString(), response.getEntity().getContent().toString());

                if (200 == response.getStatusLine().getStatusCode()){
                    Intent intent = new Intent(this, AlertListViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, values);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void initUsersSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.usersSpinner);
        // Defined Array values to show in Spinner
        final List<User> users = new ArrayList<User>();
        users.add(new User("marcos.rimoldi", "Marcos"));
        users.add(new User("mdsossich", "Mario"));

        // Create an ArrayAdapter using the string array and a default spinner layout
        //UserAdapter adapter = new UserAdapter(this, users);
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, R.layout.custom_spinner_item, users);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                selectedUser = (User) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + "AIzaSyBb9YcTitPdDnUyPYkUcvZwohRuZCfKgtY");
            sb.append("&components=country:ar");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("Places API", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("Places API", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("Places API", "Cannot process JSON results", e);
        }

        return resultList;
    }

}
