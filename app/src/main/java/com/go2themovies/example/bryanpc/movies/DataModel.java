package com.go2themovies.example.bryanpc.movies;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

class DataModel implements AsyncResponse {

    private ArrayList<Golfcourse> coursesArray = new ArrayList<Golfcourse>();
    private Context ctx;
    FetchMoviesData asyncTask =new FetchMoviesData();

    // Initializer to read a text file into an array of golfcourse objects
    public DataModel(Context ctx, String coursesFilename) {
        String line;
        BufferedReader br;
        String file ="courses.txt";


        asyncTask.delegate = this;
        FetchMoviesData moviesTask = new FetchMoviesData();
        moviesTask.delegate = this;
       // String restoredText = "Most popular";
        moviesTask.execute(coursesFilename);


        /*
        try {
            br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(file)));



            while ((line = br.readLine()) != null) {
                StringTokenizer sTok = new StringTokenizer(line, ":");
                String name =  sTok.nextToken();
                String address =  sTok.nextToken();
                Golfcourse gc = new Golfcourse(name,address);
               // gc.address = sTok.nextToken();
                coursesArray.add(gc);
            }
        } catch (IOException e) {
            return;
        }
       */
    }

    // Method to retrieve courses
    public ArrayList<Golfcourse> getCourses() {
        return coursesArray;
    }
    public int getCoursesNo() { return coursesArray.size();}

    public void processFinish(final ArrayList<Movie> output){
        //this you will received result fired from async class of onPostExecute(result) method.
        //   Log.d("TAGT",output);

        // screensize = Utility.getDeviceResolution();



     //   Log.d("Screen",screensize);
        String imageurl = "";
        /*
        if(screensize.equals("XHDPI"))
        { imageurl = "http://image.tmdb.org/t/p/w185/";}
        // String d = "http://image.tmdb.org/t/p/w342/";
        else { imageurl = "http://image.tmdb.org/t/p/w342";}
        */

        imageurl = "http://image.tmdb.org/t/p/w185/";
        Log.d("TAGET", "result: " + output.size());
        String[] mStringArray = new String[output.size()];
        String[] mStringArray1 = new String[output.size()];
        String[] mStringArray2 = new String[output.size()];
        String[] mStringArray3 = new String[output.size()];
        int i = 0;
        for(Movie x : output)
        { Log.d("TAGET", "movies: " + imageurl + x.getPosterpath());
            mStringArray[i] = imageurl+x.getPosterpath();
            mStringArray1[i] = x.getTitle();
            mStringArray2[i] = x.getOverview();
            mStringArray3[i] = x.getId();


            Golfcourse gc = new Golfcourse(mStringArray1[i],mStringArray[i],mStringArray2[i],mStringArray3[i]);
            // gc.address = sTok.nextToken();
            coursesArray.add(gc);
            i++;
        }






    }
}
