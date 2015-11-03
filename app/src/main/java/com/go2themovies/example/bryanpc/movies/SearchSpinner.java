package com.go2themovies.example.bryanpc.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class SearchSpinner extends ActionBarActivity {

    private static final String LOG_TAG = SearchSpinner.class.getSimpleName();
    private Spinner spinner1;
    private Button btnSubmit;
    public static final String PREFS_NAME = "MyPrefsFile";

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spinner);



        spinner1 = (Spinner) findViewById(R.id.spinner1);



        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("searchopt", null);

        TextView optTv = (TextView) findViewById(R.id.optselected);
        optTv.setText("  Your have selected: ");


        if (restoredText == null ) restoredText = "Most popular";


        if (restoredText.equals("Most popular")) { spinner1.setSelection(0);}
        else
        if (restoredText.equals("Favorites")) { spinner1.setSelection(2);}
        else
        if (restoredText.equals("Highest rated")) { spinner1.setSelection(1);}
        else { spinner1.setSelection(0);}




        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_spinner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);


        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //  Toast.makeText(SearchSpinner.this,
                //          "OnClickListener : " +
                //                  "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()),

                //          Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("searchopt", String.valueOf(spinner1.getSelectedItem()));
                Log.d("pref1", String.valueOf(spinner1.getSelectedItem()));

                // Commit the edits!
                editor.commit();

                 Intent k = new Intent(getApplicationContext(),GolfcourseListActivity.class);
                 startActivity(k);

            }

        });
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();


    }




}

