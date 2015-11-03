package com.go2themovies.example.bryanpc.movies;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Handler;
import android.os.Bundle;

import android.view.Menu;
import android.widget.ProgressBar;


public class GolfcourseListActivity extends ActionBarActivity
        implements GolfcourseListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean mTwoPane;
    private static  String TAG = "GolfcourseListActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get golf courses from a file named courses.txt
        //check preferences and pass the preferences thru course variable
        //to do........................

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("searchopt", null);
        DataModel dataModel = new DataModel(this, restoredText);
        ArrayList<Golfcourse> courses = dataModel.getCourses();

        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println(e);
        }



        FragmentManager fm = getFragmentManager();
        setContentView(R.layout.activity_golfcourse_list);

        if (findViewById(R.id.golfcourse_detail_container) != null) {
            mTwoPane = true;
            // Its a tablet, so create a new detail fragment if one does not already exist
            GolfcourseDetailFragment df = (GolfcourseDetailFragment) fm.findFragmentByTag("Detail");
            if (df == null) {


                // Initialize new detail fragment
                df = new GolfcourseDetailFragment();
                Bundle args = new Bundle();
                args.putParcelable("course", new Golfcourse("Please select movie??","? "," ?","? "));
                df.setArguments(args);
                fm.beginTransaction().replace(R.id.golfcourse_detail_container, df, "Detail").commit();
            }
        }

        // Initialize a new golfcourse list fragment, if one does not already exist
        GolfcourseListFragment cf = (GolfcourseListFragment) fm.findFragmentByTag("List");
        if ( cf == null) {
            cf = new GolfcourseListFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList("courses", dataModel.getCourses());

            String[] imgid = new String[courses.size()];
              int i = 0;
              for (Golfcourse c : courses) {
                 Log.d("Man", "list=" + c.address);
                imgid[i] = c.address;
             }
            int num = dataModel.getCoursesNo();
            if (num ==  0) {
                Toast.makeText(getApplicationContext(), "No data try again "+num,
                        Toast.LENGTH_LONG).show();
            }
            cf.setArguments(arguments);
            fm.beginTransaction().replace(R.id.golfcourse_list, cf, "List").commit();
        }
    }

    /**
     * Callback method from {@link GolfcourseListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Golfcourse c) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.


            Bundle arguments = new Bundle();
            // Pass the selected Golfcourse object to the DetailFragment

            Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)"+c.name,
                    Toast.LENGTH_LONG).show();

            arguments.putParcelable("course", c);
            GolfcourseDetailFragment fragment = new GolfcourseDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.golfcourse_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, GolfcourseDetailActivity.class);
            // Pass the selected Golfcourse object to the DetailActivity
            detailIntent.putExtra("course", c);
            Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)"+c.name,
                    Toast.LENGTH_LONG).show();

            startActivity(detailIntent);
        }
    }



}
