package com.example.credittask2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WatchFragment extends Fragment {

    private static final String MOVIE_LIST = "movie_list";
    private final int THUMBNAIL_SIZED = 300;


    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList;
    private ArrayList<Integer> movieIDs;
    private ArrayList<Integer> imagesPosition;
    private ArrayList<String> urls;
    private RecyclerViewAdapter3 myAdapter;
    private RequestQueue queue;
    private Genre genreMaker;

    public WatchFragment() {
        // Required empty public constructor
    }

    public static WatchFragment newInstance(ArrayList<Movie> mvList) {
        WatchFragment fragment = new WatchFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MOVIE_LIST, mvList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        imagesPosition.clear();
        urls.clear();
        movieIDs.clear();
        movieList.clear();
        initData();
        myAdapter = new RecyclerViewAdapter3(movieList);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieList = getArguments().getParcelableArrayList(MOVIE_LIST);
        }
        imagesPosition = new ArrayList<>();
        urls = new ArrayList<>();
        movieIDs = new ArrayList<>();
        queue = Volley.newRequestQueue(getContext());
        genreMaker = new Genre();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, container, false);
        recyclerView = view.findViewById(R.id.recylerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void initData()
    {
        DatabaseAdapter db = new DatabaseAdapter(getContext());
        db.open();
        Cursor c = db.getAllItems();
        for(int i = 0; i < c.getCount(); i++)
        {
            movieIDs.add(c.getInt(1));
            c.moveToNext();
        }
        db.close();
        for(int i = 0; i < movieIDs.size(); i++)
        {
            getMovieFromRemoteServer(movieIDs.get(i));
        }
    }

    public void getMovieFromRemoteServer(int movieID)
    {
        String url = "https://api.themoviedb.org/3/movie/"+movieID+"?api_key=a2da1e5f0f66f5878289b97bf807eac3&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double popularity = response.getDouble("popularity");
                    String title = response.getString("title");
                    JSONArray temp_genre= response.getJSONArray("genres");
                    JSONArray temp = new JSONArray();
                    for(int i = 0; i < temp_genre.length(); i++)
                    {
                        temp.put(temp_genre.getJSONObject(i).getInt("id"));
                    }
                    String genres = genreMaker.getGenre(temp);
                    int ID = response.getInt("id");
                    String overview = response.getString("overview");
                    String poster = "http://image.tmdb.org/t/p/w92" + response.getString("poster_path");
                    String banner = "http://image.tmdb.org/t/p/w1280" + response.getString("backdrop_path");
                    urls.add(poster);
                    movieList.add(new Movie(popularity,title,genres,ID,overview,poster,banner));
                    GlobalVariables.watchListMovies = movieList;
                    ImageLoad(poster);
                    myAdapter = new RecyclerViewAdapter3(movieList);
                    recyclerView.setAdapter(myAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    private void ImageLoad(final String url)
    {
        ImageLoader imageLoader = MySingleton.getInstance(getContext())
                .getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap image = response.getBitmap();
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image,THUMBNAIL_SIZED,THUMBNAIL_SIZED);

                if(thumbnail != null) // if the image was received, then refresh the adapter
                {
                    imagesPosition.add(urls.indexOf(url));
                    movieList.get(imagesPosition.get(imagesPosition.size()-1)).setBitmap(thumbnail);
                    myAdapter = new RecyclerViewAdapter3(movieList);
                    recyclerView.setAdapter(myAdapter);
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Image Load Error: " + error.getMessage());
            }
        });
    }

}
