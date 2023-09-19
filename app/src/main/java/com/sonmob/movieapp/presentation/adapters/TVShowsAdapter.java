package com.sonmob.movieapp.presentation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sonmob.movieapp.R;
import com.sonmob.movieapp.databinding.ItemContainerTvShowBinding;
import com.sonmob.movieapp.presentation.listeners.TVShowsListener;
import com.sonmob.movieapp.data.models.TVShow;

import java.util.List;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder> {

    private final List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private final TVShowsListener tvShowsListener;

    public TVShowsAdapter(List<TVShow> tvShows, TVShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener = tvShowsListener;
    }

    @NonNull
    @Override
    public TVShowsAdapter.TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show, parent, false
        );
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowsAdapter.TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(v ->
                    tvShowsListener.onTVShowClicked(tvShow));
        }
    }
}
