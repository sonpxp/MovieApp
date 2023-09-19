package com.sonmob.movieapp.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.data.reponsitories.SearchTVShowRepository;
import com.sonmob.movieapp.data.responses.TVShowsResponse;

public class SearchViewModel extends ViewModel {

    private final SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        this.searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowsResponse> searchTVShow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query, page);
    }
}
