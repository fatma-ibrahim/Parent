package org.code.parentsplashscreen.adapters;

import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.codeforiraq.parentsplashscreen.R;

public class BindingAdapters {

    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String imageURL) {
        try {
            Picasso.get().load(imageURL).error(R.drawable.ic_error).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
