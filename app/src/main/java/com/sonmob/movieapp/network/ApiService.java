package com.sonmob.movieapp.network;

import com.sonmob.movieapp.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShow(@Query("page") int page);
}
