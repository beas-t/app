package com.example.bladder_frontend.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.bladder_frontend.api.RetrofitClient;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

public class ImageUtils {

    public static void loadImageFromUrl(Context context, ImageView imageView, String url) {
        if (url == null || url.isEmpty()) return;

        String fullUrl = url;
        if (!url.startsWith("http")) {
            String baseUrl = RetrofitClient.getBaseUrl(context);
            if (baseUrl.endsWith("/") && url.startsWith("/")) {
                fullUrl = baseUrl + url.substring(1);
            } else {
                fullUrl = baseUrl + url;
            }
        }

        final String finalUrl = fullUrl;
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream in = new URL(finalUrl).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        if (bitmap != null && imageView != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setImageTintList(null);
                            imageView.setBackground(null);
                            imageView.setPadding(0, 0, 0, 0);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
