package com.sonmob.movieapp.network;

import com.sonmob.movieapp.responses.TVShowDetailsResponse;
import com.sonmob.movieapp.responses.TVShowsResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Observable<TVShowsResponse> getMostPopularTVShow(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TVShowsResponse> searchTVShow(@Query("q") String query, @Query("page") int page);
}
