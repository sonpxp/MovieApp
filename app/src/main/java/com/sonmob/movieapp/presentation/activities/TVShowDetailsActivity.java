package com.sonmob.movieapp.presentation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.sonmob.movieapp.R;
import com.sonmob.movieapp.presentation.adapters.ImageSliderAdapter;
import com.sonmob.movieapp.databinding.ActivityTvshowDetailsBinding;
import com.sonmob.movieapp.data.models.TVShow;
import com.sonmob.movieapp.utils.TempDataHolder;
import com.sonmob.movieapp.presentation.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private TVShow tvShow;
    private Boolean isWatchlistAvailable = false;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    //khoi tao
    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    //kiem tra xem phim da duoc add vao watchlist chua, add roi thi thay icon check
    private void checkTVShowInWatchlist() {
        disposable = new CompositeDisposable();
        disposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                            isWatchlistAvailable = true;
                            binding.imageWatchlist.setImageResource(R.drawable.ic_added);
                        }, throwable -> {
                            Toast.makeText(this, "Error: " + throwable, Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    //get data va hien thi
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
                                    //disposable.dispose();
                                }, throwable -> {
                                    Toast.makeText(this, "Remove from watchlist error: " + throwable, Toast.LENGTH_SHORT).show();
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
                                    //disposable.dispose();
                                }, throwable -> {
                                    Toast.makeText(this, "Added from watchlist error: " + throwable, Toast.LENGTH_SHORT).show();
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
        ImageSliderAdapter slider2Adapter = new ImageSliderAdapter(sliderImages);
        binding.imageSlider.setSliderAdapter(slider2Adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.startAutoCycle();

        binding.imageSlider.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
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