package com.go2themovies.example.bryanpc.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelHahn on 5/10/15.
 */
//http://mobileappdocs.com/android.html

public class Golfcourse implements Parcelable {
    // Golf course attributes
    String  name = "None";
    String  address = "None";
    String overview = "None";
    String id = "None";
    int holes =18;
    boolean isPublic;

    // Constructor for course
    Golfcourse() {}
    Golfcourse(String name,String address,String overview,String id) {
        this.name = name;
        this.address = address;
        this.overview = overview;
        this.id = id;
    }
    // Text representation of the class
    public String toString () {
        return name;
    }

    // Parcelable implementation
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name); out.writeString(address);out.writeString(overview);out.writeString(id);
    }
    public static final Parcelable.Creator<Golfcourse> CREATOR
            = new Parcelable.Creator<Golfcourse>() {
        public Golfcourse createFromParcel(Parcel in) {
            return new Golfcourse(in);
        }
        public Golfcourse[] newArray(int size) {
            return new Golfcourse[size];
        }
    };
      private Golfcourse(Parcel in) {        name = in.readString();
          address = in.readString(); overview = in.readString();id = in.readString();

    }
}

