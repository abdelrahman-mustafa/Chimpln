package com.indeves.chmplinapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Adapters.StudioTeamMembersAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class StuProProfile extends Fragment implements FirebaseEventsListener, View.OnClickListener {
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    Context attachedActivityContext;
    TextView proName, prosCount, eventsCount, subInfoRow, addPro;
    RatingBar rate;
    ImageView profileImage;
    RecyclerView teamMembersListView;
    ProUserModel proUserModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("TestTest", "In on Create view");
        View rootView = inflater.inflate(R.layout.stu_pro_profile, container, false);
        proName = rootView.findViewById(R.id.stu_profile_name);
        prosCount = rootView.findViewById(R.id.stu_profile_prosCount);
        eventsCount = rootView.findViewById(R.id.pro_profile_events);
        subInfoRow = rootView.findViewById(R.id.stu_profile_about);
        rate = rootView.findViewById(R.id.stu_profile_rating);
        teamMembersListView = rootView.findViewById(R.id.stu_profile_itemsList_listView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        addPro = rootView.findViewById(R.id.stu_profile_addPro_textView);
        addPro.setOnClickListener(this);
        profileImage = rootView.findViewById(R.id.pro_profile_pic);
        setHasOptionsMenu(true);
        if (attachedActivityContext != null && ((StuLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_my_profile));
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ReadData readData = new ReadData(this);
            readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        ReadData readData2 = new ReadData(new FirebaseEventsListener() {
            @Override
            public void onWriteDataCompleted(boolean writeSuccessful) {

            }

            @Override
            public void onReadDataResponse(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ArrayList<EventModel> eventModels = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        EventModel eventModel = dataSnapshot1.getValue(EventModel.class);
                        if (eventModel != null && eventModel.getPhotographerId() != null && eventModel.getPhotographerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && eventModel.getEventStatus() != null && eventModel.getEventStatus().equals("finished")) {
                            eventModels.add(eventModel);
                        }
                    }
                    eventsCount.setText(String.valueOf(eventModels.size()));

                }
            }
        });
        readData2.getAllEvents();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pro_profile_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pro_profile_menu_edit) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_container, new StuEditProfileFragment());
            ft.addToBackStack(null);
            ft.commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (teamMembersListView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) teamMembersListView.getLayoutManager())
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

        teamMembersListView.setLayoutManager(mLayoutManager);
        teamMembersListView.scrollToPosition(scrollPosition);
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

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            proUserModel = dataSnapshot.getValue(ProUserModel.class);
            displayUserInfo(proUserModel);
        }

    }

    void displayUserInfo(ProUserModel proUserModel) {
        if (proUserModel != null) {
            if (proUserModel.getName() != null) {
                proName.setText(proUserModel.getName());
            }
            if (proUserModel.getArea() != null) {
                subInfoRow.setText(proUserModel.getArea());
            }
            if (proUserModel.getProfilePicUrl() != null) {
                Picasso.with(getContext()).load(proUserModel.getProfilePicUrl()).resize(300, 300).placeholder(R.drawable.user).transform(new CircleTransform()).error(R.drawable.user).into(profileImage);
            }
            if (proUserModel.getStudioTeamMembers() != null) {
                prosCount.setText(String.valueOf(proUserModel.getStudioTeamMembers().size()));
            }
            if (proUserModel.getStudioTeamMembers() != null) {
                StudioTeamMembersAdapter studioTeamMembersAdapter = new StudioTeamMembersAdapter(getContext(), proUserModel.getStudioTeamMembers(), false);
                teamMembersListView.setAdapter(studioTeamMembersAdapter);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v == addPro) {
            if (proUserModel != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, AddProToStudioFragment.newInstance(proUserModel));
                ft.addToBackStack(null);
                ft.commit();
            }
        }

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
