package com.example.android.popularmovies.BackgroundTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmovies.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by yalsaadi on 4/10/2018.
 */

public class AsyncTaskFetchMovies extends AsyncTask<String, Void, String> {


    private Context context = null;
    private AsyncTaskCompleteListener<String> asyncTaskCompleteListener;


    public AsyncTaskFetchMovies(Context context, AsyncTaskCompleteListener<String> asyncTaskCompleteListener) {

        this.context = context;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }


    @Override
    protected String doInBackground(String... strings) {


        StringBuilder result = new StringBuilder();
        String line = "";


        URL url = buildURL(strings[0], strings[1]);


        if (url != null) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }


        return result.toString();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        asyncTaskCompleteListener.onTaskComplete(s);
    }


    private URL buildURL(String pageNumber, String displayOption) {
        Uri.Builder builder = new Uri.Builder();
        URL url = null;


        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(displayOption)
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", pageNumber);


        String strUrl = builder.build().toString();
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    public interface AsyncTaskCompleteListener<T> {
        public void onTaskComplete(T result);
    }
}
