package com.example.listafilmw;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {

    TextView movieTitle;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitle = findViewById(R.id.movieTitle);
        ratingBar = findViewById(R.id.ratingBar);

        String title = getIntent().getStringExtra("title");
        movieTitle.setText(title);
    }
}