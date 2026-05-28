package com.example.listafilmw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText searchEdit;
    Button searchButton;
    ListView movieList;

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdit = findViewById(R.id.searchEdit);
        searchButton = findViewById(R.id.searchButton);
        movieList = findViewById(R.id.movieList);

        searchButton.setOnClickListener(v -> searchMovies());

        movieList.setOnItemClickListener((parent, view, position, id) -> {

            Movie movie = movies.get(position);

            saveMovieToWatchlist(movie);

            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("year", movie.getYear());
            intent.putExtra("imdbID", movie.getImdbID());

            startActivity(intent);
        });
    }

    private void searchMovies() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApi api = retrofit.create(MovieApi.class);

        Call<MovieResponse> call = api.searchMovies(
                "545968c4",
                searchEdit.getText().toString()
        );

        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.body() != null) {

                    movies = response.body().getSearch();

                    ArrayAdapter<Movie> adapter =
                            new ArrayAdapter<>(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    movies
                            );

                    movieList.setAdapter(adapter);
                }
                if(response.body() == null){
                    Toast.makeText(MainActivity.this,"No response",Toast.LENGTH_LONG).show();
                    return;
                }

                if(response.body().getSearch() == null){
                    Toast.makeText(MainActivity.this,"No movies found",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error loading movies", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveMovieToWatchlist(Movie movie) {

        SharedPreferences prefs = getSharedPreferences("watchlist", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(movie.getImdbID(), movie.getTitle());
        editor.apply();

        Toast.makeText(this, "Saved to Watchlist", Toast.LENGTH_SHORT).show();
    }
}