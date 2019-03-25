package com.example.studentmanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.studentmanager.fragment.AddStudentFragment;
import com.example.studentmanager.fragment.ShowStudentListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int SHOW_STUDENT_TAB = 0;
    private static final int ADD_STUDENT_TAB = 1;

    //defining the titles of the tabs
    private String[] mTabTitles = new String[]{"Students", "Add/Update"};

    /**
     * Constructor for class ViewPagerAdapter
     *
     * @param fm
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SHOW_STUDENT_TAB:
                return new ShowStudentListFragment();
            case ADD_STUDENT_TAB:
                return new AddStudentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }
}
