package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Adapters.EventTypeAdapter;
import com.indeves.chmplinapp.Adapters.ProEventHistoryAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.EventType;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

@SuppressLint("ValidFragment")
public class Usere_photographer_Events extends android.support.v4.app.Fragment {


    ArrayList<EventType> eventsList = new ArrayList<>();
    FragmentManager fragmentManager;
    ;
    EventTypeAdapter userProfEventsAdaptor;
    String Uid;
    RecyclerView recyclerView;

    @SuppressLint("ValidFragment")
    public Usere_photographer_Events(String uid) {
        Uid = uid;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_select_pro_events, container, false);
         recyclerView = rootView.findViewById(R.id.event_type_list);
        userProfEventsAdaptor = new EventTypeAdapter(eventsList);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setAdapter(userProfEventsAdaptor);
        EventType eventType = new EventType("Wedding");
        EventType eventType1 = new EventType("Birthday");
        EventType eventType2 = new EventType("Business Event");
        EventType eventType3 = new EventType("Casual Event");
        EventType eventType4 = new EventType("Graduation");
        EventType eventType5 = new EventType("Celebrities");
        EventType eventType6 = new EventType("Products");


        eventsList.add(eventType);
        eventsList.add(eventType1);
        eventsList.add(eventType2);
        eventsList.add(eventType3);
        eventsList.add(eventType4);
        eventsList.add(eventType5);
        eventsList.add(eventType6);
        userProfEventsAdaptor.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                recyclerView.setVisibility(GONE);
                fragmentManager = getActivity().getSupportFragmentManager();
                UserProfSelectEventType frag = new UserProfSelectEventType(Uid,eventsList.get(position).getType());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.container_events, frag).commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootView;
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
