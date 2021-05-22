package com.sonmob.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.adapters.TVShowsAdapter;
import com.sonmob.movieapp.databinding.ActivityMainBinding;
import com.sonmob.movieapp.listeners.TVShowsListener;
import com.sonmob.movieapp.models.TVShow;
import com.sonmob.movieapp.viewmodels.MostPopularTVShowsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {

    private ActivityMainBinding mainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        mainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, this);
        mainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        mainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mainBinding.tvShowRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShow();
                    }
                }
            }
        });
        mainBinding.imageWatchList.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), WatchlistActivity.class)));
        getMostPopularTVShow();

    }

    private void getMostPopularTVShow() {
        toggleLoading();
        viewModel.getPopularTVShows(currentPage).observe(this, mostPopularTVShowsRespose -> {
            toggleLoading();
            if (mostPopularTVShowsRespose != null) {
                totalAvailablePages = mostPopularTVShowsRespose.getTotalPages();
                if (mostPopularTVShowsRespose.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShowsRespose.getTvShows());
                    //tvShowsAdapter.notifyDataSetChanged();
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });

    }

    private void toggleLoading() {
        if (currentPage == 1) {
            mainBinding.setIsLoading(mainBinding.getIsLoading() == null ||
                    !mainBinding.getIsLoading());
        } else {
            mainBinding.setIsLoadingMore(mainBinding.getIsLoadingMore() == null ||
                    !mainBinding.getIsLoadingMore());
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}