package com.sonmob.movieapp.reponsitories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sonmob.movieapp.network.ApiClient;
import com.sonmob.movieapp.network.ApiService;
import com.sonmob.movieapp.responses.TVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostPopularTVShowsRespository {

    private MutableLiveData<TVShowsResponse> liveData;
    private ApiService apiService;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MostPopularTVShowsRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsResponse> makeApiCall(int page) {
        liveData = new MutableLiveData<>();
        apiService.getMostPopularTVShow(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMostPopularTVShowRx());
        return liveData;
    }

    private Observer<TVShowsResponse> getMostPopularTVShowRx() {
        return new Observer<TVShowsResponse>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

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
