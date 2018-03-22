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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Adapters.ProProfPackageAdaptor;
import com.indeves.chmplinapp.Models.MyPackageData;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class StuProfilePackageTabMyPackages extends android.support.v4.app.Fragment implements FirebaseEventsListener {
    //    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    ProProfPackageAdaptor userProfEventsAdaptor;
    private List<PackageModel> list;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_events_upcoming, container, false);
        recyclerView = rootView.findViewById(R.id.userProfile_event_recycler_view);
        ;
        list = new ArrayList<PackageModel>();

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userProfEventsAdaptor = new ProProfPackageAdaptor(list);
        return rootView;
    }

    @Override
    public void onStart() {
        ReadData readData = new ReadData(this);
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());

        super.onStart();
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

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            ProUserModel proUserModel = dataSnapshot.getValue(ProUserModel.class);
            if (proUserModel != null) {
                if (proUserModel.getPackages() != null) {
                    list.addAll(proUserModel.getPackages());
                    recyclerView.setAdapter(userProfEventsAdaptor);
                    userProfEventsAdaptor.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "You don't have any packages yet", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
