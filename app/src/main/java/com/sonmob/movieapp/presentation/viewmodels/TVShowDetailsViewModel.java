package com.sonmob.movieapp.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sonmob.movieapp.data.local.database.TVShowsDatabase;
import com.sonmob.movieapp.data.models.TVShow;
import com.sonmob.movieapp.data.reponsitories.TVShowDetailsReponsitory;
import com.sonmob.movieapp.data.responses.TVShowDetailsResponse;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private final TVShowDetailsReponsitory tvShowDetailsReponsitory;
    private final TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsReponsitory = new TVShowDetailsReponsitory();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsReponsitory.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvShow);
    }
}
