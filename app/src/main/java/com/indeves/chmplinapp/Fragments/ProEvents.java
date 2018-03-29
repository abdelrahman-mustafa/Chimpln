package com.indeves.chmplinapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProEvents extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TabLayout tabLayout;
    Context attachedActivityContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private ViewPager viewPager;
    private String mParam2;

    public ProEvents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static ProEvents newInstance(String param1, String param2) {
        ProEvents fragment = new ProEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_events, container, false);
        if (attachedActivityContext != null && ((ProLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ProLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_events));
        }


        viewPager = (ViewPager) rootView.findViewById(R.id.container);

        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setSelectedTabIndicatorHeight(0);

        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#545454"));
        setupViewPager(viewPager);
        return rootView;

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProProfileEventsTabUpComming(), "UpComing");
        adapter.addFragment(new ProProfileEventsTabPending(), "Pending");
        adapter.addFragment(new ProProfileEventsTabHistory(), "History");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.attachedActivityContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
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
