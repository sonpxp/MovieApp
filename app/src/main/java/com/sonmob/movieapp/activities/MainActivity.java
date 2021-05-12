package com.sonmob.movieapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.adapters.TVShowsAdapter;
import com.sonmob.movieapp.databinding.ActivityMainBinding;
import com.sonmob.movieapp.models.TVShow;
import com.sonmob.movieapp.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;

    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        getMostPopularTVShow();

    }

    private void getMostPopularTVShow() {
        activityMainBinding.setIsLoading(true);
        viewModel.getPopularTVShows(0).observe(this, mostPopularTVShowsRespose -> {
            activityMainBinding.setIsLoading(false);
            if (mostPopularTVShowsRespose != null) {
                if (mostPopularTVShowsRespose.getTvShows() != null) {
                    tvShows.addAll(mostPopularTVShowsRespose.getTvShows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}