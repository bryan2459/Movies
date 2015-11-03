package com.go2themovies.example.bryanpc.movies;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FetchMoviesData extends AsyncTask<String, Void, ArrayList<Movie>> {

    private static final String LOG_TAG = FetchMoviesData.class.getSimpleName();

    public static final String PREFS_NAME = "MyPrefsFile";

    public AsyncResponse delegate=null;
    ArrayList<Movie> movieObjs = new ArrayList<Movie>();



    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {



        // JSONObject forecastJson = new JSONObject(forecastJsonStr);
        String[] resultStrs = new String[20];

        String id;
        String title;
        String backdroppath;
        String originaltitle;
        String popularity;
        String posterpath;
        String releasedate;
        String overview;
        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonMovieObject = array.getJSONObject(i);

                id = jsonMovieObject.getString("id");
                title = jsonMovieObject.getString("title");
                backdroppath = jsonMovieObject.getString("backdrop_path");
                originaltitle = jsonMovieObject.getString("original_title");
                popularity = jsonMovieObject.getString("vote_average");
                posterpath =  jsonMovieObject.getString("poster_path");
                releasedate = jsonMovieObject.getString("release_date");
                overview = jsonMovieObject.getString("overview");
                //results.add(movieBuilder.build());



                //resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + posterpath;
                resultStrs[i] = posterpath;

                movieObjs.add(new Movie(id,title,backdroppath,originaltitle,popularity,resultStrs[i],releasedate,overview));
                Log.d("TAGE", "result: " + posterpath);
                Log.d("TAGE","id :" +id);
            }
        } catch (JSONException e) {
            System.err.println(e);

            //  Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
        }



        return movieObjs;

    }
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        Log.d("prefxx length:","x"+params[0]);
        if ((params.length == 0) || (params[0] == null)) {
            params[0] = "Most popular";
        }
        // Log.d("prefxx",params[0]);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String resultJsonStr = null;



        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at



            // http://openmoviesTaskmap.org/API#forecast
            // final String FORECAST_BASE_URL =
            //         "http://api.openmoviesTaskmap.org/data/2.5/forecast/daily?";
            String FORECAST_BASE_URL = "";
            if (params[0].equals("Most popular"))
            { FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=your api";}
            else
            { FORECAST_BASE_URL ="http://api.themoviedb.org/3/discover/movie?sort_by=original_title.asc&vote_count.gte=30&with_genres=35&api_key=your api";}
            //   final String QUERY_PARAM = "q";
            //   final String FORMAT_PARAM = "mode";
            //   final String UNITS_PARAM = "units";
            //   final String DAYS_PARAM = "cnt";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL);

            //  Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            //          .appendQueryParameter(QUERY_PARAM, params[0])
            //          .appendQueryParameter(FORMAT_PARAM, format)
            //          .appendQueryParameter(UNITS_PARAM, units)
            //          .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
            //          .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies string: " + resultJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the moviesTask data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(resultJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        super.onPostExecute(result);
        //    Log.d("TAG",result[1]);
        ArrayList<String> mMoviesAdapter = new ArrayList<String>();
        //http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
        if (result != null) {
           /*
             mMoviesAdapter.clear();

            for(String dayMoviesStr : result) {
               Log.d("TAGEr", "result: " + dayMoviesStr);

                   mMoviesAdapter.add(dayMoviesStr);

            }
            */
            delegate.processFinish(result);

            // New data is back from the server.  Hooray!
        }
    }
}

