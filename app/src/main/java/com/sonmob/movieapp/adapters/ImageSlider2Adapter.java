package com.sonmob.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.sonmob.movieapp.R;
import com.sonmob.movieapp.databinding.ItemContainerSliderImageBinding;

import org.jetbrains.annotations.NotNull;

public class ImageSlider2Adapter extends SliderViewAdapter<ImageSlider2Adapter.ImageSliderViewHolder> {

    private final String[] sliderImages;

    private LayoutInflater layoutInflater;

    public ImageSlider2Adapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderImageBinding sliderImageBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_image, parent, false
        );
        return new ImageSliderViewHolder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageSlider2Adapter.ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getCount() {
        return sliderImages.length;
    }

    public static class ImageSliderViewHolder extends SliderViewAdapter.ViewHolder {
        private final ItemContainerSliderImageBinding itemContainerSliderImageBinding;

        public ImageSliderViewHolder(ItemContainerSliderImageBinding itemContainerSliderImageBinding) {
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;
        }

        public void bindSliderImage(String imageURL) {
            itemContainerSliderImageBinding.setImageURL(imageURL);
        }
    }
}
