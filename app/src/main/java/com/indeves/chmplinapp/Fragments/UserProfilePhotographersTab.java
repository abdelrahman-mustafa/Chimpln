package com.indeves.chmplinapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePhotographersTab extends android.support.v4.app.Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public UserProfilePhotographersTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.container_special);
        setupViewPager(viewPager);

        tabLayout= rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(4);

        setupTabIcons();
        //tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
    private void setupTabIcons() {


        tabLayout.getTabAt(0).setCustomView(R.layout.tab_item_2);
        tabLayout.getTabAt(1).setCustomView(R.layout.tab_item);
    }

    private void setupViewPager(ViewPager viewPager) {
        UserProfilePhotographersTab.ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new UserProfilePhotographersTabSearchSelect(), "Search");
        adapter.addFragment(new UserProfilePhotographersTabNearBy(), "Nearby");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}