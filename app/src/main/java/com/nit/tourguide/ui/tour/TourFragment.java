package com.nit.tourguide.ui.tour;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nit.tourguide.R;
import com.nit.tourguide.ui.tour.All.AllFragment;
import com.nit.tourguide.ui.tour.recent.RecentFragment;

public class TourFragment extends Fragment {

    private TourViewModel mViewModel;
    private ViewPager pager;
    public static TourFragment newInstance() {
        return new TourFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tour_fragment, container, false);

        pager = root.findViewById(R.id.viewpager);

        TourPager tourPager = new TourPager(getChildFragmentManager());
        pager.setAdapter(tourPager);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TourViewModel.class);
        // TODO: Use the ViewModel
    }


    private class TourPager extends FragmentStatePagerAdapter {

        public TourPager(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return new RecentFragment();
                }
                case 1: {
                    return new AllFragment();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
