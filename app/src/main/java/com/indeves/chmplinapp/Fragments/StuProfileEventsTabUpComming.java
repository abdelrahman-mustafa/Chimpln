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

import com.indeves.chmplinapp.Adapters.UserProfEventsAdaptor;
import com.indeves.chmplinapp.Models.PhotographerData;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class StuProfileEventsTabUpComming extends android.support.v4.app.Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    PhotographerData photographerData;
    //    private ViewPager viewPager;
    TextView sortDate, sortLocation, sortType;
    int textDefaultColor;
    UserProfEventsAdaptor userProfEventsAdaptor;
    private List<PhotographerData> list;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stu_profile_up_coming_events_tab, container, false);
        recyclerView = rootView.findViewById(R.id.proProfile_event_recycler_view);
        sortLocation = rootView.findViewById(R.id.proProfile_phot_search_location);
        sortDate = rootView.findViewById(R.id.proProfile_phot_search_date);
        sortType = rootView.findViewById(R.id.proProfile_phot_search_number_events);
        textDefaultColor = sortDate.getCurrentTextColor();
        photographerData = new PhotographerData("Ahmed", "Wedding", "10:00am  3:00pm", "6", "Feb", "no");
        list = new ArrayList<PhotographerData>();
        list.add(photographerData);
        list.add(photographerData);
        list.add(photographerData);
        list.add(photographerData);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userProfEventsAdaptor = new UserProfEventsAdaptor(list);
        recyclerView.setAdapter(userProfEventsAdaptor);

        sortLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotographerData data = new PhotographerData("Ahmed", "Wedding", "10:00am  3:00pm", "6", "Feb", "no");
                list = new ArrayList<PhotographerData>();
                list.add(data);

                sortDate.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortDate.setTextColor(textDefaultColor);
                sortLocation.setTextColor(getResources().getColor(R.color.colorWhite));
                sortLocation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sortType.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortType.setTextColor(textDefaultColor);

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userProfEventsAdaptor = new UserProfEventsAdaptor(list);
                recyclerView.setAdapter(userProfEventsAdaptor);
            }
        });
        sortType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotographerData data = new PhotographerData("Ahmed", "Wedding", "10:00am  3:00pm", "6", "Feb", "no");

                list = new ArrayList<PhotographerData>();
                list.add(data);
                list.add(data);
                sortType.setTextColor(getResources().getColor(R.color.colorWhite));
                sortType.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sortLocation.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortLocation.setTextColor(textDefaultColor);
                sortDate.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortDate.setTextColor(textDefaultColor);
                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userProfEventsAdaptor = new UserProfEventsAdaptor(list);
                recyclerView.setAdapter(userProfEventsAdaptor);
            }
        });
        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotographerData data = new PhotographerData("Ahmed", "Wedding", "10:00am  3:00pm", "6", "Feb", "no");
                list = new ArrayList<PhotographerData>();

                sortDate.setTextColor(getResources().getColor(R.color.colorWhite));
                sortDate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sortLocation.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortLocation.setTextColor(textDefaultColor);
                sortType.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sortType.setTextColor(textDefaultColor);
                list.add(data);
                list.add(data);
                list.add(data);
                list.add(data);

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userProfEventsAdaptor = new UserProfEventsAdaptor(list);
                recyclerView.setAdapter(userProfEventsAdaptor);
            }
        });


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
