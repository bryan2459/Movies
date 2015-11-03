package com.go2themovies.example.bryanpc.movies;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by BryanPC on 2/11/2015.
 */
public class courseList {

    public courseList(ArrayList<Golfcourse> courses) {

        for (Golfcourse c : courses) {Log.d("Tag","list="+ c.address);}


    }
}