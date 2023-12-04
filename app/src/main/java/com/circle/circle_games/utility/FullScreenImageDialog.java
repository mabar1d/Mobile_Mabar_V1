package com.circle.circle_games.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.circle.circle_games.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullScreenImageDialog extends Dialog {

    public FullScreenImageDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_fullscreen_image_view);

        ImageView imageView = findViewById(R.id.fullscreenImageView);
        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setImageFromUrl(String imageUrl) {
        PhotoView imageView = findViewById(R.id.fullscreenImageView);
        imageView.setMaximumScale(10); // Set batas zoom jika diperlukan
        imageView.setZoomable(true);

        CircularProgressDrawable cp = new CircularProgressDrawable(getContext());
        cp.setStrokeWidth(5f);
        //cp.setBackgroundColor(R.color.material_grey_300);
        cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
        cp.setCenterRadius(30f);
        cp.start();

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(cp)
                .error(R.drawable.not_found); // Gambar placeholder jika unduhan gagal



        Glide.with(getContext())
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }
}
