package com.indeves.chmplinapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Adapters.StudioTeamMembersAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.StudioTeamMember;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AssignEventToProFragment extends Fragment implements View.OnClickListener, FirebaseEventsListener, StudioTeamMembersAdapter.ItemClickListener {
    private static final String ARG_PARAM1 = "selectedEvent";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    List<LookUpModel> eventTypes, eventTimes, sharingOptions;
    ReadData readData;
    ProgressDialog progressDialog;
    RecyclerView teamMembersListView;
    StudioTeamMembersAdapter studioTeamMembersAdapter;
    ProUserModel proUserModel;
    CardView assignedTeamMemberCardView;
    ImageView assignedMemberImageView;
    StudioTeamMember assignedStudioMemer;
    private Button accept, reject;
    private TextView name, time, day, month, share, location, eventType, assignedMemberName, assignedMemberDescription;
    private EventModel selectedEvent;
    private FirebaseEventsListener firebaseEventsListener = new FirebaseEventsListener() {
        @Override
        public void onWriteDataCompleted(boolean writeSuccessful) {
            if (writeSuccessful) {
                for (int i = 0; i < proUserModel.getStudioTeamMembers().size(); i++) {
                    if (proUserModel.getStudioTeamMembers().get(i).equals(assignedStudioMemer)) {
                        ArrayList<String> eventsIds;
                        if (proUserModel.getStudioTeamMembers().get(i).getEventsIds() == null) {
                            eventsIds = new ArrayList<>();
                        } else {
                            eventsIds = proUserModel.getStudioTeamMembers().get(i).getEventsIds();
                        }
                        eventsIds.add(selectedEvent.getEventId());
                        proUserModel.getStudioTeamMembers().get(i).setEventsIds(eventsIds);
                    }
                }
                ProUserModel proUserModel1 = new ProUserModel();
                proUserModel1.setStudioTeamMembers(proUserModel.getStudioTeamMembers());
                WriteData writeData = new WriteData(AssignEventToProFragment.this);
                try {
                    writeData.updateUserProfileData(proUserModel1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Unable to accept event", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onReadDataResponse(DataSnapshot dataSnapshot) {

        }
    };

    public AssignEventToProFragment() {
        // Required empty public constructor
    }

    public static AssignEventToProFragment newInstance(EventModel selectedEvent) {
        AssignEventToProFragment fragment = new AssignEventToProFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, selectedEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedEvent = (EventModel) getArguments().getSerializable(ARG_PARAM1);
        }
        eventTimes = new ArrayList<>();
        eventTypes = new ArrayList<>();
        sharingOptions = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait.. ");
        progressDialog.show();
        loadLookups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_assign_event_to, container, false);
        name = rootView.findViewById(R.id.userProfile_event_photographer_name);
        time = rootView.findViewById(R.id.userProfile_event_time);
        day = rootView.findViewById(R.id.userProfile_event_day);
        month = rootView.findViewById(R.id.userProfile_event_month);
        share = rootView.findViewById(R.id.userProfile_event_shareOption);
        location = rootView.findViewById(R.id.userProfile_event_location);
        eventType = rootView.findViewById(R.id.userProfile_event_type);
        assignedMemberImageView = rootView.findViewById(R.id.assigned_team_member_image);
        assignedMemberName = rootView.findViewById(R.id.assigned_team_member_name);
        assignedMemberDescription = rootView.findViewById(R.id.assigned_team_member_description);
        accept = rootView.findViewById(R.id.pro_Accept_button);
        accept.setOnClickListener(this);
        accept.setVisibility(View.GONE);
        reject = rootView.findViewById(R.id.pro_Reject_button);
        reject.setOnClickListener(this);
        assignedTeamMemberCardView = rootView.findViewById(R.id.assignedProItem);
        assignedTeamMemberCardView.setVisibility(View.GONE);

        //-------------------------

        teamMembersListView = rootView.findViewById(R.id.stu_profile_itemsList_listView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //------------------------

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ReadData readData = new ReadData(this);
            readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        progressDialog.show();
        if (v == accept) {
            WriteData writeData = new WriteData(firebaseEventsListener);
            try {
                writeData.respondToEvent("accepted", selectedEvent.getEventId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (v == reject) {
            WriteData writeData = new WriteData(this);
            try {
                writeData.respondToEvent("rejected", selectedEvent.getEventId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayEventData() {
        if (selectedEvent != null) {
            name.setText(selectedEvent.getBookerUserName());
            String[] eventDateParts = selectedEvent.getEventDate().split("-");
            day.setText(eventDateParts[0]);
//        month.setText(eventDateParts[1]);
            if (eventTypes != null) {
                for (LookUpModel eventTypeElement : eventTypes) {
                    if (selectedEvent.getTypeId() == eventTypeElement.getId()) {
                        eventType.setText(eventTypeElement.getEnglishName());

                    }
                }

            }
            if (sharingOptions != null) {
                for (LookUpModel sharingOptionElement : sharingOptions) {
                    if (selectedEvent.getSharingOptionId() == sharingOptionElement.getId()) {
                        share.setText(sharingOptionElement.getEnglishName());

                    }
                }

            }
            if (selectedEvent.getStartTime() != null && !selectedEvent.getStartTime().isEmpty() && selectedEvent.getEndTime() != null && !selectedEvent.getEndTime().isEmpty()) {
                String timeString = selectedEvent.getStartTime() + " - " + selectedEvent.getEndTime();
                time.setText(timeString);

            } else {
                if (eventTimes != null) {
                    for (LookUpModel eventTimesElement : eventTimes) {
                        if (eventTimesElement.getId() == selectedEvent.getTimeId()) {
                            time.setText(eventTimesElement.getEnglishName());

                        }
                    }
                }
            }
            location.setText(selectedEvent.getEventCity());
            month.setText(eventDateParts[1]);

        }
    }

    public void loadLookups() {
        readData = new ReadData();
        //using nested calls is to make sure that all lookups are loaded
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                if (eventTypeLookups != null) {
                    eventTypes.addAll(eventTypeLookups);
                    readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
                        @Override
                        public void onLookUpsResponse(List<LookUpModel> lookups) {
                            if (lookups != null) {
                                eventTimes.addAll(lookups);
                                readData.getLookupsByType("shringOptionLookups", new ReadData.LookUpsListener() {
                                    @Override
                                    public void onLookUpsResponse(List<LookUpModel> lookups) {
                                        progressDialog.dismiss();
                                        if (lookups != null) {
                                            sharingOptions.addAll(lookups);
                                            displayEventData();
                                        } else {
                                            Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to load event data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        progressDialog.dismiss();
        if (writeSuccessful) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            proUserModel = dataSnapshot.getValue(ProUserModel.class);
            if (proUserModel != null) {
                if (proUserModel.getStudioTeamMembers() != null) {
                    studioTeamMembersAdapter = new StudioTeamMembersAdapter(getContext(), proUserModel.getStudioTeamMembers(), "assign");
                    studioTeamMembersAdapter.setClickListener(AssignEventToProFragment.this);
                    teamMembersListView.setAdapter(studioTeamMembersAdapter);
                }
            }
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
    public void onItemClick(View view, int position) {
        displayAssignedPro(studioTeamMembersAdapter.getItem(position));
    }

    public void displayAssignedPro(StudioTeamMember member) {
        assignedStudioMemer = member;
        reject.setVisibility(View.GONE);
        accept.setVisibility(View.VISIBLE);
        teamMembersListView.setVisibility(View.GONE);
        assignedTeamMemberCardView.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(member.getImageUrl()).transform(new CircleTransform()).resize(80, 80).into(assignedMemberImageView);
        assignedMemberName.setText(member.getName());
        String description = member.getCity() + ", " + member.getGender();
        assignedMemberDescription.setText(description);
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
