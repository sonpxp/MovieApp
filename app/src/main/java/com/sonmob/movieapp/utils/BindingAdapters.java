package com.sonmob.movieapp.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class BindingAdapters {

    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String URL) {
        try {
            imageView.setAlpha(0f);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder image while loading
                    .error(android.R.drawable.ic_delete); // Error image in case of loading failure

            Glide.with(imageView.getContext())
                    .load(URL)
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade()) // Crossfade animation
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            // Handle error here if needed
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            imageView.animate().setDuration(300).alpha(1f).start();
                            return false;
                        }
                    })
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
