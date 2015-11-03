package com.go2themovies.example.bryanpc.movies;

public class Movie {

    private static final String LOG_TAG = Movie.class.getSimpleName();

    private String id;
    private String  title;
    private String  backdroppath;
    private String  originaltitle;
    private String popularity;
    private String posterpath;
    private String releasedate;
    private String overview;

    public Movie (
            String id,
            String title,
            String backdroppath,
            String originaltitle,
            String popularity,
            String posterpath,
            String releasedate,
            String overview
    )
    {   this.id =id;
        this.title = title;
        this.backdroppath = backdroppath;
        this.originaltitle = originaltitle;
        this.popularity = popularity;
        this.posterpath = posterpath;
        this.releasedate = releasedate;
        this.overview = overview;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdroppath() {
        return backdroppath;
    }

    public void setBackdroppath(String backdroppath) {
        this.backdroppath = backdroppath;
    }

    public String getOriginaltitle() {
        return originaltitle;
    }

    public void setOriginaltitle(String originaltitle) {
        this.originaltitle = originaltitle;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}

