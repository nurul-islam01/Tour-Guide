package com.nit.tourguide.ui.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nit.tourguide.R;
import com.nit.tourguide.activity.SliderActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.nit.tourguide.ui.map.MapFragment.TAG;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String[] filepath;

    public GalleryAdapter(Context context, String[] fpath)
    {
        this.context = context;
        filepath = fpath;
    }

    @Override
    public int getItemViewType(int position) {
        String extension = filepath[position].substring(filepath[position].lastIndexOf("."));
        if (extension.contains(".jpg")) {
            position = 0;
        } else if (extension.contains(".mp4")) {
            position = 1;
        }
        Log.d(TAG, "getItemViewType: " + position);
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: return new ImageHolder(LayoutInflater.from(context).inflate(R.layout.gallery_row, parent, false));
            case 1: return new VideoHolder(LayoutInflater.from(context).inflate(R.layout.video_row, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            Log.d(TAG, "onBindViewHolder: Image ");
            ImageHolder imageHolder = (ImageHolder) holder;
            Picasso.get().load("file://" + filepath[position]).error(R.drawable.windy).into(imageHolder.image);
        } else if (holder.getItemViewType() == 1) {
            VideoHolder videoHolder = (VideoHolder) holder;
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(filepath[position], MediaStore.Video.Thumbnails.MICRO_KIND);
            videoHolder.video.setImageBitmap(bMap);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SliderActivity.class);
                intent.putExtra("slides", filepath);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return filepath.length;
    }


    public class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
    public class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView video;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.video);
        }
    }
}
