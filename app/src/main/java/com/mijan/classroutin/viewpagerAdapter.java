package com.mijan.classroutin;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mijan.classroutin.fragment.fragment_fri;
import com.mijan.classroutin.fragment.fragment_home;
import com.mijan.classroutin.fragment.fragment_sat;
import com.mijan.classroutin.fragment.fragment_sun;
import com.mijan.classroutin.fragment.fragment_thu;
import com.mijan.classroutin.fragment.fragment_tue;
import com.mijan.classroutin.fragment.fragment_wed;
import com.mijan.classroutin.fragment.fragmeny_mon;

import java.util.Calendar;


public class viewpagerAdapter extends FragmentPagerAdapter {
    Calendar calendar = Calendar.getInstance();

    private String fragmnet[] = {"Today","sun", "mon", "tue","wed","thu","fri","sat"};

    public viewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new fragment_home();
            case 1:
                return new fragment_sun();
            case 2:
                return new fragmeny_mon();
            case 3:
                return new fragment_tue();
            case 4:
                return new fragment_wed();
            case 5:
                return new fragment_thu();
             case 6:
             return new fragment_fri();
            case 7:
                return new fragment_sat();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return
                fragmnet.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmnet[position];
    }
}
