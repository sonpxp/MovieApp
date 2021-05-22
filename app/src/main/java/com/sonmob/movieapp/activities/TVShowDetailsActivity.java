package com.sonmob.movieapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.adapters.ImageSliderAdapter;
import com.sonmob.movieapp.databinding.ActivityTvshowDetailsBinding;
import com.sonmob.movieapp.models.TVShow;
import com.sonmob.movieapp.utilities.TempDataHolder;
import com.sonmob.movieapp.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private TVShow tvShow;
    private Boolean isWatchlistAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isWatchlistAvailable = true;
                    binding.imageWatchlist.setImageResource(R.drawable.ic_added);
                    disposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            binding.setIsLoading(false);
            if (tvShowDetailsResponse.getTVShowDetails() != null) {
                if (tvShowDetailsResponse.getTVShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTVShowDetails().getPictures());

                }
                binding.setTvShowImageURL(tvShowDetailsResponse.getTVShowDetails().getImagePath());
                binding.imageRoundedTvShow.setVisibility(View.VISIBLE);
                binding.setDescription(String.valueOf(HtmlCompat.fromHtml(
                        tvShowDetailsResponse.getTVShowDetails().getDescription(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                )));
                binding.textDescription.setVisibility(View.VISIBLE);
                binding.textReadMore.setVisibility(View.VISIBLE);
                binding.textReadMore.setOnClickListener(v -> {
                    if (binding.textReadMore.getText().toString().equals("Read More")) {
                        binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                        binding.textDescription.setEllipsize(null);
                        binding.textReadMore.setText(R.string.read_less);
                    } else {
                        binding.textDescription.setMaxLines(4);
                        binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                        binding.textReadMore.setText(R.string.read_more);
                    }
                });
                binding.setRating(String.format(
                        Locale.getDefault(),
                        "%.2f",
                        Double.parseDouble(tvShowDetailsResponse.getTVShowDetails().getRating())
                        )
                );
                if (tvShowDetailsResponse.getTVShowDetails().getGenres() != null) {
                    binding.setGenre(tvShowDetailsResponse.getTVShowDetails().getGenres()[0]);
                } else {
                    binding.setGenre("N/A");
                }
                binding.setRuntime(tvShowDetailsResponse.getTVShowDetails().getRuntime() + " Min");
                binding.viewDivider1.setVisibility(View.VISIBLE);
                binding.linearMisc.setVisibility(View.VISIBLE);
                binding.viewDivider2.setVisibility(View.VISIBLE);

                binding.buttonWeb.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTVShowDetails().getUrl()));
                    startActivity(intent);
                });
                binding.buttonWeb.setVisibility(View.VISIBLE);
                binding.buttonEpisodes.setVisibility(View.VISIBLE);

                binding.imageWatchlist.setOnClickListener(v -> {
                    CompositeDisposable disposable = new CompositeDisposable();
                    if (isWatchlistAvailable) {
                        disposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isWatchlistAvailable = false;
                                    TempDataHolder.IS_WATCHLIST_UPDATE = true;
                                    binding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                    Toast.makeText(TVShowDetailsActivity.this, "Remove from watchlist", Toast.LENGTH_SHORT).show();
                                    disposable.dispose();
                                })
                        );
                    } else {
                        disposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    TempDataHolder.IS_WATCHLIST_UPDATE = true;
                                    binding.imageWatchlist.setImageResource(R.drawable.ic_added);
                                    Toast.makeText(TVShowDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    disposable.dispose();
                                })
                        );
                    }
                });
                binding.imageWatchlist.setVisibility(View.VISIBLE);
                loadBaseTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.pagerSlider.setOffscreenPageLimit(1);
        binding.pagerSlider.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.pagerSlider.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImages.length);
        binding.pagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setUpSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.bg_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            binding.linearSliderIndicators.addView(indicators[i]);
        }
        binding.linearSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = binding.linearSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.linearSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.bg_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.bg_slider_indicator_inactive));
            }
        }
    }

    private void loadBaseTVShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.setNetworkCountry(tvShow.getNetwork() + "(" +
                tvShow.getCountry() + ")");
        binding.setStatus(tvShow.getStatus());
        binding.setStartedDate(tvShow.getStartDate());

        binding.textName.setVisibility(View.VISIBLE);
        binding.textNetworkCountry.setVisibility(View.VISIBLE);
        binding.textStatus.setVisibility(View.VISIBLE);
        binding.textStarted.setVisibility(View.VISIBLE);

    }
}