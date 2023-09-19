package com.sonmob.movieapp.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.data.reponsitories.MostPopularTVShowsRepository;
import com.sonmob.movieapp.data.responses.TVShowsResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private final MostPopularTVShowsRepository mostPopularTVShowsRespository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRespository = new MostPopularTVShowsRepository();
    }

    public LiveData<TVShowsResponse> getPopularTVShows(int page) {
        return mostPopularTVShowsRespository.makeApiCall(page);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
