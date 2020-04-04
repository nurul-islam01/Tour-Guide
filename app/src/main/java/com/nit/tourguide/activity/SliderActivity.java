package com.nit.tourguide.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.nit.tourguide.R;
import com.nit.tourguide.activity.adapter.SliderPagerAdapter;

public class SliderActivity extends AppCompatActivity {
    private String[] slides;
    private int position;
    private ViewPager slidePager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_slider);
        Intent data = getIntent();
        slides = data.getStringArrayExtra("slides");
        position = data.getIntExtra("position", 0);

        slidePager = findViewById(R.id.slidePager);
        SliderPagerAdapter adapter = new SliderPagerAdapter(slides, this);
        slidePager.setAdapter(adapter);
        slidePager.setCurrentItem(position);
    }
}
