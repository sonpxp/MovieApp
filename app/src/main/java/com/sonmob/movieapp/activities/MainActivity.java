package com.sonmob.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;

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

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, this);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowRecyclerView.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage +=1;
                        getMostPopularTVShow();
                    }
                }
            }
        });
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

    private void toggleLoading(){
        if (currentPage == 1){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else {
                activityMainBinding.setIsLoading(true);
            }
        }else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("id", tvShow.getId());
        intent.putExtra("name", tvShow.getName());
        intent.putExtra("startDate", tvShow.getStartDate());
        intent.putExtra("country", tvShow.getCountry());
        intent.putExtra("network", tvShow.getNetwork());
        intent.putExtra("status", tvShow.getStatus());
        startActivity(intent);
    }
}