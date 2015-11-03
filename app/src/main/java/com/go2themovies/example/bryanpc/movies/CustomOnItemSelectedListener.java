package com.go2themovies.example.bryanpc.movies;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    private static final String LOG_TAG = CustomOnItemSelectedListener.class.getSimpleName();

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        // Toast.makeText(parent.getContext(),
        //         "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
        //         Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
