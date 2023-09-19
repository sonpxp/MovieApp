package com.sonmob.movieapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.data.models.TVShow;
import com.sonmob.movieapp.databinding.ActivityMainBinding;
import com.sonmob.movieapp.presentation.adapters.TVShowsAdapter;
import com.sonmob.movieapp.presentation.listeners.TVShowsListener;
import com.sonmob.movieapp.presentation.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {

    private ActivityMainBinding binding;
    private MostPopularTVShowsViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, this);
        binding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        binding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.tvShowRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShow();
                    }
                }
            }
        });
        binding.imageWatchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), WatchlistActivity.class)));
        binding.imageSearch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));
        getMostPopularTVShow();
    }

    private void getMostPopularTVShow() {
        toggleLoading();
        viewModel.getPopularTVShows(currentPage).observe(this, mostPopularTVShows -> {
            toggleLoading();
            if (mostPopularTVShows != null) {
                totalAvailablePages = mostPopularTVShows.getTotalPages();
                if (mostPopularTVShows.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShows.getTvShows());
                    //tvShowsAdapter.notifyDataSetChanged();
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });

    }

    private void toggleLoading() {
        if (currentPage == 1) {
            binding.setIsLoading(binding.getIsLoading() == null ||
                    !binding.getIsLoading());
        } else {
            binding.setIsLoadingMore(binding.getIsLoadingMore() == null ||
                    !binding.getIsLoadingMore());
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}