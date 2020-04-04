package com.nit.tourguide.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.media.ThumbnailUtils;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nit.tourguide.R;
import com.squareup.picasso.Picasso;


public class SliderPagerAdapter extends PagerAdapter {

    private String[] slides;
    private Context context;
    private LayoutInflater inflater;

    public SliderPagerAdapter(String[] slides, Context context) {
        this.slides = slides;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.gallery_slider, container, false);
        ImageView image = view.findViewById(R.id.image);
        RelativeLayout videoLayout = view.findViewById(R.id.videoLayout);
        ImageView video = view.findViewById(R.id.video);

        String extension = slides[position].substring(slides[position].lastIndexOf("."));
        if (extension.contains(".jpg")) {
            image.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            try {
                Picasso.get().load("file://" + slides[position]).placeholder(R.drawable.ic_image).error(R.drawable.ic_error).into(image);
            }catch (Exception e) {
                Log.d("Slider Adapter", "instantiateItem: " + e.getMessage());
            }


        } else if (extension.contains(".mp4")) {
            image.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(slides[position], MediaStore.Video.Thumbnails.MICRO_KIND);
            video.setImageBitmap(bMap);

            videoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo(slides[position]);
                }
            });
        }

        container.addView(view, 0);
        return view;
    }

    private void playVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    @Override
    public int getCount() {
        return slides.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
