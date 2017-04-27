package com.example.rimolma1.selfparking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AsyncListViewLoader<T> extends AsyncTask<String, Void, List<T>> {

    private CustomAdapter<T> adapter;
    private Activity activity;
    private ProgressDialog dialog;

    AsyncListViewLoader(Activity activity, CustomAdapter<T> adapter) {
        this.adapter = adapter;
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPostExecute(List<T> result) {
        super.onPostExecute(result);
        dialog.dismiss();
        adapter.setItemList(result);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading data...");
        dialog.show();
    }

    @Override
    protected List<T> doInBackground(String... params) {
        List<T> result = new ArrayList<T>();

        try {
            URL u = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();
            InputStream is = conn.getInputStream();

            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(b) != -1)
                baos.write(b);

            String JSONResp = new String(baos.toByteArray());

            JSONArray arr = new JSONArray(JSONResp);
            for (int i=0; i < arr.length(); i++) {
                result.add(convertObject(arr.getJSONObject(i)));
            }

            return result;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public abstract T convertObject(JSONObject obj) throws JSONException;

}