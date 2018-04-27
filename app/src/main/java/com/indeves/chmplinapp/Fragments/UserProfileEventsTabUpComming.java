package com.indeves.chmplinapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Activities.Approval;
import com.indeves.chmplinapp.Activities.Contactpro;
import com.indeves.chmplinapp.Activities.Rejected;
import com.indeves.chmplinapp.Adapters.ProEventsArrayAdapter;
import com.indeves.chmplinapp.Adapters.UserProfEventsAdaptor;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.PhotographerData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class UserProfileEventsTabUpComming extends android.support.v4.app.Fragment implements FirebaseEventsListener {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    ProEventsArrayAdapter userProfEventsAdaptor;
    FragmentManager fragmentManager;
    private List<EventModel> list;
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
//        photographerData = new PhotographerData("Ahmed", "Wedding", "10:00am  3:00pm", "6", "Feb", "no");
//        list.add(photographerData);
//        list.add(photographerData);
//        list.add(photographerData);
//        list.add(photographerData);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        list = new ArrayList<EventModel>();
        userProfEventsAdaptor = new ProEventsArrayAdapter(list, "user");
        ReadData readData = new ReadData(this);
        readData.getAllEvents();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well

                getActivity().findViewById(R.id.userProfile_LinearLayout).setVisibility(View.GONE);
                fragmentManager = getActivity().getSupportFragmentManager();
                if (list.get(position).getEventStatus().equals("pending")) {
                    Approval frag = new Approval(list.get(position));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                } else if (list.get(position).getEventStatus().equals("accepted")) {
                    Contactpro frag = new Contactpro(list.get(position).getPhotographerId());
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                } else if (list.get(position).getEventStatus().equals("rejected")) {
                    Rejected frag = new Rejected(list.get(position));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                getActivity().findViewById(R.id.userProfile_LinearLayout).setVisibility(View.GONE);
                fragmentManager = getActivity().getSupportFragmentManager();
                if (list.get(position).getEventStatus().equals("pending")) {
                    Approval frag = new Approval(list.get(position));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                } else if (list.get(position).getEventStatus().equals("accepted")) {
                    Contactpro frag = new Contactpro(list.get(position).getPhotographerId());
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                } else if (list.get(position).getEventStatus().equals("rejected")) {
                    Rejected frag = new Rejected(list.get(position));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                }

            }
        }));

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

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {

            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                EventModel eventModel = dataSnapshot1.getValue(EventModel.class);

                if (eventModel != null && eventModel.getBookerUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    list.add(eventModel);

                }
            }
            recyclerView.setAdapter(userProfEventsAdaptor);
            userProfEventsAdaptor.notifyDataSetChanged();

        }
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
