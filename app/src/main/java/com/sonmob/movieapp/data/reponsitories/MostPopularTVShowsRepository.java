package com.sonmob.movieapp.data.reponsitories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sonmob.movieapp.data.network.ApiClient;
import com.sonmob.movieapp.data.network.ApiService;
import com.sonmob.movieapp.data.responses.TVShowsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostPopularTVShowsRepository {

    private MutableLiveData<TVShowsResponse> liveData;
    private final ApiService apiService;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MostPopularTVShowsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsResponse> makeApiCall(int page) {
        liveData = new MutableLiveData<>();
        apiService.getMostPopularTVShow(page)
                .subscribeOn(Schedulers.io())
                // .filter(tvShowsResponse -> page < 2) // loc theo dieu kien
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMostPopularTVShowRx());
        return liveData;
    }

    private Observer<TVShowsResponse> getMostPopularTVShowRx() {
        return new Observer<TVShowsResponse>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
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
