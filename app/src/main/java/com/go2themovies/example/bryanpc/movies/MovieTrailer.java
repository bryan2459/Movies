package com.go2themovies.example.bryanpc.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MovieTrailer extends ActionBarActivity implements AsyncResponseTrailer {

    ImageButton nextButton;
    String[] mStringArray;
    String[] mStringArray1;
    int index = 0;
    public static final String PREFS_NAME = "MyPrefsFile";

    FetchTrailerData asyncTask =new FetchTrailerData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTask.delegate = this;
        setContentView(R.layout.activity_review_trailer);

        //TextView trailerTv = (TextView) findViewById(R.id.idTrailer);
        // trailerTv.setText("  Movie ID: " + getIntent().getStringExtra("idTrailer")+" "+getIntent().getStringExtra("nameTrailer"));

        FetchTrailerData moviesTask = new FetchTrailerData();
        moviesTask.delegate = this;
        moviesTask.execute(getIntent().getStringExtra("idTrailer"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_trailer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            String trailerShare = prefs.getString("trailer", null);
            String movieName = prefs.getString("movieName",null);
            String movieTitle = prefs.getString("movieTitle",null);

            Log.d("Title",movieTitle);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This movie sent: " + currentDateTimeString + "\n" + movieTitle + "\n" + "https://www.youtube.com/embed/"+trailerShare);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "#From GoMovies");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void processFinishTrailer(final ArrayList<Trailer> output){
        Log.d("Trailer", "result: " + output.size());
        mStringArray = new String[output.size()];
        mStringArray1 = new String[output.size()];

        final String movieTitle = getIntent().getStringExtra("movieTitle");
        int i = 0;
        for(Trailer x : output)
        { Log.d("TAGET","movies: "+ x.getKey()+" "+x.getSite()+" "+ x.getName() );
            mStringArray[i] = x.getKey();
            mStringArray1[i] =  x.getName();

            i++;

        }

       TextView idTv = (TextView)findViewById(R.id.idTrailer);
       idTv.setText("Click to Play");

        nextButton = (ImageButton) findViewById(R.id.nextbut);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // showYouTube(mStringArray[index],mStringArray1[index]);
                if (index < output.size())  index++;
                if (index >= output.size()) index = 0;


                Log.d("index","index="+index+"output="+output.size());
                showYouTube(mStringArray[index],mStringArray1[index],movieTitle);

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("trailer", mStringArray[index]);
                editor.putString("movieName", mStringArray1[index]);
                editor.putString("movieTitle",movieTitle);
                editor.commit();



            }

        });

        //  showYouTube(mStringArray[0],mStringArray1[0]);



    }

    public void showYouTube(String trailer,String name,String movieTitle)
    {
        //  setContentView(R.layout.webview);
        //used json to get
        //http://api.themoviedb.org/3/movie/257344/videos?api_key=your api
        //  String frameVideo = "<html><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/movie/257344/videos\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        //    String frameVideo = "<html><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/7Ql1T41Jw5U\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        String frameVideo1 ="<html><br><br><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/";
        String source  = trailer;
        String frameVideo2 = "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";


       TextView idTv = (TextView)findViewById(R.id.idTrailer);
       idTv.setText(name);

        String frameVideo = frameVideo1+trailer+frameVideo2;
        Log.d("Movie",frameVideo);
        WebView displayVideo = (WebView)findViewById(R.id.webView);
        displayVideo.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayVideo.loadData(frameVideo, "text/html", "utf-8");

    }
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();


    }
}
