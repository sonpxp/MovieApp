package com.sonmob.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.reponsitories.MostPopularTVShowsRespository;
import com.sonmob.movieapp.responses.TVShowsResponse;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MostPopularTVShowsViewModel extends ViewModel {
    private final MostPopularTVShowsRespository mostPopularTVShowsRespository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRespository = new MostPopularTVShowsRespository();
    }

    public LiveData<TVShowsResponse> getPopularTVShows(int page) {
        return mostPopularTVShowsRespository.makeApiCall(page);
    }

}
