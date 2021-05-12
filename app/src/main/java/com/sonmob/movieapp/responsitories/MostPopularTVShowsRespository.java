package com.sonmob.movieapp.responsitories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sonmob.movieapp.network.ApiClient;
import com.sonmob.movieapp.network.ApiService;
import com.sonmob.movieapp.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsRespository {

    private ApiService apiService;

    public MostPopularTVShowsRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsResponse> getMostPopularTVShows(int page) {
        MutableLiveData<TVShowsResponse> liveData = new MutableLiveData<>();
        apiService.getMostPopularTVShow(page).enqueue(new Callback<TVShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsResponse> call, @NonNull Response<TVShowsResponse> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsResponse> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }
}
