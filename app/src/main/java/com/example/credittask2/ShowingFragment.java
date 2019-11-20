package com.example.credittask2;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
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


public class ShowingFragment extends Fragment {

    public static final String MOVIE_LIST = "movie_list";
    private final int THUMBNAIL_SIZED = 300;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter myAdapter;
    private ArrayList<Movie> movieList;
    private ArrayList<Integer> imagesPosition;
    private ArrayList<String> urls;
    private RequestQueue queue;
    private Genre genreMaker;

    public ShowingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ShowingFragment newInstance(ArrayList<Movie> mvList) {
        ShowingFragment fragment = new ShowingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MOVIE_LIST, mvList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieList = getArguments().getParcelableArrayList(MOVIE_LIST);
        }
        imagesPosition = new ArrayList<>();
        urls = new ArrayList<>();
        genreMaker = new Genre();
        queue = Volley.newRequestQueue(getContext());
        getMovieFromRemoteServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_showing, container, false);
        recyclerView = view.findViewById(R.id.recylerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new RecyclerViewAdapter(movieList);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    public void getMovieFromRemoteServer()
    {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a2da1e5f0f66f5878289b97bf807eac3&language=en-US";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response);
                    JSONArray result = response.getJSONArray("results");
                    for(int i= 0; i < result.length(); i++)
                    {
                        double popularity = result.getJSONObject(i).getDouble("popularity");
                        String title = result.getJSONObject(i).getString("title");
                        JSONArray genre = result.getJSONObject(i).getJSONArray("genre_ids");
                        String genres = genreMaker.getGenre(genre);
                        int ID = result.getJSONObject(i).getInt("id");
                        String overview = result.getJSONObject(i).getString("overview");
                        String poster = "http://image.tmdb.org/t/p/w92" + result.getJSONObject(i).getString("poster_path");
                        String banner = "http://image.tmdb.org/t/p/w1280" + result.getJSONObject(i).getString("backdrop_path");
                        urls.add(poster);
                        movieList.add(new Movie(popularity,title,genres,ID,overview,poster,banner));
                        GlobalVariables.showingMovies = movieList;
                        ImageLoad(poster);
                        myAdapter = new RecyclerViewAdapter(movieList);
                        recyclerView.setAdapter(myAdapter);
                    }

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
                    myAdapter = new RecyclerViewAdapter(movieList);
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
