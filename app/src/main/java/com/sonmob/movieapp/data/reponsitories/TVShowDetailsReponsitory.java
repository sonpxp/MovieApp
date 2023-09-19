package com.sonmob.movieapp.data.reponsitories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sonmob.movieapp.data.network.ApiClient;
import com.sonmob.movieapp.data.network.ApiService;
import com.sonmob.movieapp.data.responses.TVShowDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsReponsitory {

    private final ApiService apiService;

    public TVShowDetailsReponsitory() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails (String tvShowId) {
        MutableLiveData<TVShowDetailsResponse> data = new MutableLiveData<>();
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailsResponse> call,@NonNull Response<TVShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
