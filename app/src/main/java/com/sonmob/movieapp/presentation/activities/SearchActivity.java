package com.sonmob.movieapp.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.data.models.TVShow;
import com.sonmob.movieapp.databinding.ActivitySearchBinding;
import com.sonmob.movieapp.presentation.adapters.TVShowsAdapter;
import com.sonmob.movieapp.presentation.listeners.TVShowsListener;
import com.sonmob.movieapp.presentation.viewmodels.SearchViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();
    }

    private void doInitialization() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.rcvSearchTvShow.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows, this);
        binding.rcvSearchTvShow.setAdapter(tvShowsAdapter);

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage = 1;
                                    totalAvailablePages = 1;
                                    searchTVShow(s.toString());
                                }
                            });
                        }
                    }, 1000);
                } else {
                    tvShows.clear();
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.rcvSearchTvShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rcvSearchTvShow.canScrollVertically(1)) {
                    if (!binding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTVShow(binding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        binding.inputSearch.requestFocus();
    }

    private void searchTVShow(String query) {
        toggleLoading();
        viewModel.searchTVShow(query, currentPage).observe(this, tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowsResponse.getTvShows());
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