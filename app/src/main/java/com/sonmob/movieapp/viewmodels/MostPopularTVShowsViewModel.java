package com.sonmob.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.responses.TVShowsResponse;
import com.sonmob.movieapp.reponsitories.MostPopularTVShowsRespository;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsRespository mostPopularTVShowsRespository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRespository = new MostPopularTVShowsRespository();
    }

    public LiveData<TVShowsResponse> getPopularTVShows(int page) {
        return mostPopularTVShowsRespository.getMostPopularTVShows(page);
    }

}
