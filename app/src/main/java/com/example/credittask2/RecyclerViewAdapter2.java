package com.example.credittask2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

    private ArrayList<Movie> movieArrayList;

    public RecyclerViewAdapter2(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter2.ViewHolder holder, int position) {

        holder.textView_title.setText(movieArrayList.get(position).getTitle());
        holder.textView_genre.setText(movieArrayList.get(position).getGenre());
        holder.textView_popularity.setText("Popularity: " + movieArrayList.get(position).getPopularity());
        holder.imageView.setImageBitmap(movieArrayList.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        //return movieArrayList.size();
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView_title,textView_genre, textView_popularity;
        public ImageView imageView;
        public Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_genre = itemView.findViewById(R.id.textView_genre);
            textView_popularity = itemView.findViewById(R.id.textView_popularity);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(context,MovieDetailActivity.class);
            intent.putExtra(GlobalVariables.MOVIE_KEY,movieArrayList.get(pos));
            intent.putExtra("watch_list",false);
            context.startActivity(intent);
        }

    }

}
