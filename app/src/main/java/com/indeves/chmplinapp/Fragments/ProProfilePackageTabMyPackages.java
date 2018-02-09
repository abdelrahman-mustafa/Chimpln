package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.indeves.chmplinapp.Adapters.ProProfPackageAdaptor;
import com.indeves.chmplinapp.Adapters.UserProfEventsAdaptor;
import com.indeves.chmplinapp.Controllers.MyPackageData;
import com.indeves.chmplinapp.Controllers.PhotographerData;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProProfilePackageTabMyPackages extends android.support.v4.app.Fragment {
    private List<MyPackageData> list;
    private ViewPager viewPager;
    MyPackageData myPackageData;

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    ProProfPackageAdaptor userProfEventsAdaptor;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_events_upcoming, container, false);
        recyclerView = rootView.findViewById(R.id.userProfile_event_recycler_view);

        list = new ArrayList<MyPackageData>();
        myPackageData = new MyPackageData("Golden Package", "2 full day event", "3000", "EGP", "photo & share", "6 photo sessions, Wedding");

        list.add(myPackageData);
        list.add(myPackageData);
        list.add(myPackageData);
        list.add(myPackageData);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userProfEventsAdaptor = new ProProfPackageAdaptor(list);
        recyclerView.setAdapter(userProfEventsAdaptor);
        Toast.makeText(getContext(), "Hi", Toast.LENGTH_LONG).show();

        return rootView;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }
}
