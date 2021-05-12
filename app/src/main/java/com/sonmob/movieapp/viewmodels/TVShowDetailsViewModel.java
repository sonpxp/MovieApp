package com.sonmob.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sonmob.movieapp.reponsitories.TVShowDetailsReponsitory;
import com.sonmob.movieapp.responses.TVShowDetailsResponse;

public class TVShowDetailsViewModel extends ViewModel {
    private TVShowDetailsReponsitory tvShowDetailsReponsitory;

    public TVShowDetailsViewModel() {
        tvShowDetailsReponsitory = new TVShowDetailsReponsitory();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsReponsitory.getTVShowDetails(tvShowId);
    }
}
