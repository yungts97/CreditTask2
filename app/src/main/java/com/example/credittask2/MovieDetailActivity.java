package com.example.credittask2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageBanner;
    private TextView textTitle, textPopularity, textOverview;
    private Button ButtonAdd;
    private final int THUMBNAIL_WIDTH = 1280;
    private final int THUMBNAIL_HEIGTH = 720;
    private Movie movie;
    private boolean watch_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getExtras().getParcelable(GlobalVariables.MOVIE_KEY);
        watch_list = getIntent().getBooleanExtra("watch_list",false);

        textTitle = findViewById(R.id.txt_title);
        textPopularity = findViewById(R.id.txt_popularity);
        textOverview = findViewById(R.id.txt_overview);
        imageBanner = findViewById(R.id.imageBanner);
        ButtonAdd = findViewById(R.id.btnAddWatchList);
        ButtonAdd.setOnClickListener(this);

        textTitle.setText(movie.getTitle());
        textPopularity.setText("Popularity: " + movie.getPopularity());
        textOverview.setText(movie.getOverview());
        if(watch_list)
        {
            ButtonAdd.setVisibility(View.INVISIBLE);
            ButtonAdd.setEnabled(false);
        }
        ImageLoad(movie.getBanner_path());
    }

    private void ImageLoad(final String url)
    {
        ImageLoader imageLoader = MySingleton.getInstance(getApplicationContext())
                .getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap image = response.getBitmap();
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image,THUMBNAIL_WIDTH,THUMBNAIL_HEIGTH);

                if(thumbnail != null) // if the image was received, then refresh the adapter
                {
                    imageBanner.setImageBitmap(thumbnail);
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Image Load Error: " + error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAddWatchList)
        {
            DatabaseAdapter db = new DatabaseAdapter(getApplication());
            db.open();
            db.insertItem(movie.getID());
            db.close();
            finish();
        }
    }
}
