package com.sonmob.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.models.TVShow;
import com.sonmob.movieapp.network.ApiClient;
import com.sonmob.movieapp.network.ApiService;
import com.sonmob.movieapp.responses.TVShowsResponse;
import com.sonmob.movieapp.reponsitories.MostPopularTVShowsRespository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsRespository mostPopularTVShowsRespository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRespository = new MostPopularTVShowsRespository();
    }

    public LiveData<TVShowsResponse> getPopularTVShows(int page) {
        return mostPopularTVShowsRespository.makeApiCall(page);
    }

}
