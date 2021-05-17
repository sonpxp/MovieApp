package com.sonmob.movieapp.reponsitories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sonmob.movieapp.network.ApiClient;
import com.sonmob.movieapp.network.ApiService;
import com.sonmob.movieapp.responses.TVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostPopularTVShowsRespository {

    private ApiService apiService;
    MutableLiveData<TVShowsResponse> liveData;

    public MostPopularTVShowsRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    /*public LiveData<TVShowsResponse> getMostPopularTVShows(int page) {
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
    }*/

    public LiveData<TVShowsResponse> makeApiCall(int page) {
        liveData = new MutableLiveData<>();
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        apiService.getMostPopularTVShow(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMostPopularTVShowRx());
        return liveData;
    }

    private Observer<TVShowsResponse> getMostPopularTVShowRx() {
        return new Observer<TVShowsResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull TVShowsResponse tvShowsResponse) {
                liveData.postValue(tvShowsResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                liveData.postValue(null);
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
