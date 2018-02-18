package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.Adapters.UserAccPhotographersAdaptor;
import com.indeves.chmplinapp.Models.UserAccPhotographerData;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePhotographersTabSearchOutput extends android.support.v4.app.Fragment {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    TextView date, location, numEvents;
    private RecyclerView recyclerView;
    private UserAccPhotographersAdaptor userAccPhotographersAdaptor;
    private List<UserAccPhotographerData> list;

    public UserProfilePhotographersTabSearchOutput() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search_output, container, false);

        recyclerView = rootView.findViewById(R.id.userProfile_pho_recycler_view);
        location = rootView.findViewById(R.id.userProfile_phot_search_location);
        date = rootView.findViewById(R.id.userProfile_phot_search_date);
        numEvents = rootView.findViewById(R.id.userProfile_phot_search_number_events);

        UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");

        list = new ArrayList<UserAccPhotographerData>();
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
                list = new ArrayList<UserAccPhotographerData>();
                list.add(data);

                numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                location.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                location.setTextColor(getResources().getColor(R.color.colorWhite));
                date.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
                recyclerView.setAdapter(userAccPhotographersAdaptor);
            }
        });
        numEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");

                list = new ArrayList<UserAccPhotographerData>();
                list.add(data);
                list.add(data);
                numEvents.setTextColor(getResources().getColor(R.color.colorWhite));

                numEvents.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                location.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                date.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
                recyclerView.setAdapter(userAccPhotographersAdaptor);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
                list = new ArrayList<UserAccPhotographerData>();

                date.setTextColor(getResources().getColor(R.color.colorWhite));
                numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                location.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                date.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                list.add(data);
                list.add(data);
                list.add(data);
                list.add(data);

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
                recyclerView.setAdapter(userAccPhotographersAdaptor);
            }
        });


        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
        recyclerView.setAdapter(userAccPhotographersAdaptor);
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

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


}
