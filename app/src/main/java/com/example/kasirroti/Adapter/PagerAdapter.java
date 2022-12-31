package com.example.kasirroti.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kasirroti.Fragment.KalkulatorFragment;
import com.example.kasirroti.Fragment.MetodeFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public PagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new KalkulatorFragment();
            case 1:
                return new MetodeFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    @Override
    public int getCount() {
        return number_tabs;
    }
}