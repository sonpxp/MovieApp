package com.sonmob.movieapp.presentation.listeners;

import com.sonmob.movieapp.data.models.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}
