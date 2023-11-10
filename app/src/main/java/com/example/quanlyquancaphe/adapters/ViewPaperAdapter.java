package com.example.quanlyquancaphe.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.quanlyquancaphe.fragments.DanhSachBanhFragment;
import com.example.quanlyquancaphe.fragments.DanhSachCaPheFragment;
import com.example.quanlyquancaphe.fragments.DanhSachMonKhacFragment;

public class ViewPaperAdapter extends FragmentPagerAdapter {

    public ViewPaperAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new DanhSachCaPheFragment();
        else if (position == 1)
            fragment = new DanhSachBanhFragment();
        else if (position == 2)
            fragment = new DanhSachMonKhacFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

