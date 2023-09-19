package com.sonmob.movieapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.data.models.TVShow;
import com.sonmob.movieapp.databinding.ActivityWatchlistBinding;
import com.sonmob.movieapp.presentation.adapters.WatchlistAdapter;
import com.sonmob.movieapp.presentation.listeners.WatchlistListener;
import com.sonmob.movieapp.presentation.viewmodels.WatchlistViewModel;
import com.sonmob.movieapp.utils.TempDataHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding binding;
    private WatchlistViewModel watchlistViewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitialization();
    }

    private void doInitialization() {
        watchlistViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(WatchlistViewModel.class);
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        watchlist = new ArrayList<>();
        loadWatchlist();
    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        disposable = new CompositeDisposable();
        disposable.add(watchlistViewModel.loadWatchlist()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                            binding.setIsLoading(false);
                            if (watchlist.size() > 0) {
                                watchlist.clear();
                            }
                            watchlist.addAll(tvShows);
                            watchlistAdapter = new WatchlistAdapter(watchlist, this);
                            binding.rcvWatchlist.setAdapter(watchlistAdapter);
                            binding.rcvWatchlist.setVisibility(View.VISIBLE);
                            //disposable.dispose();
                        }, throwable -> {
                            Toast.makeText(this, "Error data: " + throwable, Toast.LENGTH_SHORT).show();
                        }
                ));

    }


    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {

        disposable = new CompositeDisposable();
        disposable.add(watchlistViewModel.removeTVShowFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            watchlist.remove(position);
                            watchlistAdapter.notifyItemRemoved(position);
                            watchlistAdapter.notifyItemChanged(position, watchlistAdapter.getItemCount());
                        }, throwable -> {
                            Toast.makeText(this, "Remove error: " + throwable, Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        Log.e("msg", "onResume");
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATE) {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATE = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}