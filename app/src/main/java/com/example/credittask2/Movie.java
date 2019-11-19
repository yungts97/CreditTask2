package com.example.credittask2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private double popularity;
    private String title;
    private String genre;
    private int ID;
    private String overview;
    private String poster_path;
    private String banner_path;
    private Bitmap bitmap;

    public Movie(double popularity, String title, String genre, int ID, String overview, String poster_path, String banner_path) {
        this.popularity = popularity;
        this.title = title;
        this.genre = genre;
        this.ID = ID;
        this.overview = overview;
        this.poster_path = poster_path;
        this.banner_path = banner_path;
        this.bitmap = null;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBanner_path() {
        return banner_path;
    }

    public void setBanner_path(String banner_path) {
        this.banner_path = banner_path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    protected Movie(Parcel in) {
        popularity = in.readDouble();
        title = in.readString();
        genre = in.readString();
        ID = in.readInt();
        overview = in.readString();
        poster_path = in.readString();
        banner_path = in.readString();
        bitmap = in.readParcelable(null);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeString(genre);
        parcel.writeInt(ID);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(banner_path);
        //parcel.writeParcelable(bitmap,i);
    }
}
