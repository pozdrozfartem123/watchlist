package com.example.listafilmw;

import retrofit2.Call;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/")
    Call<MovieResponse> searchMovies(
            @Query("apikey") String apiKey,
            @Query("s") String title
    );
}
