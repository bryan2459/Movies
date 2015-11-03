package com.go2themovies.example.bryanpc.movies;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

public class FetchTrailerData extends AsyncTask<String, Void, ArrayList<Trailer>> {

    public static final String PREFS_NAME = "MyPrefsFile";

    public AsyncResponseTrailer delegate=null;
    ArrayList<Trailer> movieObjs = new ArrayList<Trailer>();
    private final String LOG_TAG =FetchTrailerData.class.getSimpleName();

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the moviesTask high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private ArrayList<Trailer> getMoviesDataFromJson(String moviesJsonStr, int numDays)
            throws JSONException {



        // JSONObject forecastJson = new JSONObject(forecastJsonStr);
        String[] resultStrs = new String[20];

        String id;
        String title;
        String key;
        String name;
        String site;

        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonMovieObject = array.getJSONObject(i);
                if (jsonMovieObject.getString("type").equals("Trailer"))
                {
                    id = jsonMovieObject.getString("id");
                    key = jsonMovieObject.getString("key");
                    name = jsonMovieObject.getString("name");
                    site = jsonMovieObject.getString("site");


                    //    resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + posterpath;

                    movieObjs.add(new Trailer(id, key,name,site));
                    //   Log.d("TAGE", "result: " + posterpath);
                    Log.d("TAGE", "id :" + id);
                }
            }
        } catch (JSONException e) {
            System.err.println(e);

            //  Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
        }
        // return resultStrs;


        return movieObjs;

    }
    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

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
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 7;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at



            // http://openmoviesTaskmap.org/API#forecast
            // final String FORECAST_BASE_URL =
            //         "http://api.openmoviesTaskmap.org/data/2.5/forecast/daily?";
            String FORECAST_BASE_URL = "";

            // FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/params[0]/videos?api_key=your api";

            final String FORMAT_PARAM = "/videos?api_key=your api";
            String movieid = params[0];
            FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/"+movieid+FORMAT_PARAM;

            final String QUERY_PARAM = "";

            //   final String UNITS_PARAM = "units";
            //   final String DAYS_PARAM = "cnt";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL);

            //  Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            //            .appendQueryParameter(QUERY_PARAM, params[0])
            //            .appendQueryParameter(FORMAT_PARAM, format)
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
            forecastJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies string: " + forecastJsonStr);
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
            return getMoviesDataFromJson(forecastJsonStr, numDays);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> resultTrailer) {
        super.onPostExecute(resultTrailer);
        //    Log.d("TAG",result[1]);
        ArrayList<String> mMoviesAdapter = new ArrayList<String>();
        //http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
        if (resultTrailer != null) {
           /*
             mMoviesAdapter.clear();

            for(String dayMoviesStr : result) {
               Log.d("TAGEr", "result: " + dayMoviesStr);

                   mMoviesAdapter.add(dayMoviesStr);

            }
            */
            delegate.processFinishTrailer(resultTrailer);

            // New data is back from the server.  Hooray!
        }
    }
}


