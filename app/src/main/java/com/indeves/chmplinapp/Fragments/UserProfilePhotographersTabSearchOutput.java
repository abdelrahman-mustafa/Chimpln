package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Adapters.UserAccPhotographersAdaptor;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserAccPhotographerData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.ClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

public class UserProfilePhotographersTabSearchOutput extends android.support.v4.app.Fragment {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    TextView date, location, numEvents;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    //    private List<UserAccPhotographerData> list;
    private RecyclerView recyclerView;
    private UserAccPhotographersAdaptor userAccPhotographersAdaptor;
    private FragmentManager fragmentManager;
    ArrayList<ProUserModel> pros;
    @SuppressLint("ValidFragment")
    public UserProfilePhotographersTabSearchOutput(ArrayList<ProUserModel> list) {
        this.pros = list;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_profile_tab_photographers_search_output, container, false);

        recyclerView = rootView.findViewById(R.id.userProfile_pho_recycler_view);
        linearLayout2 = rootView.findViewById(R.id.sortedby);
        location = rootView.findViewById(R.id.userProfile_phot_search_location);
        date = rootView.findViewById(R.id.userProfile_phot_search_date);
        numEvents = rootView.findViewById(R.id.userProfile_phot_search_number_events);
        linearLayout = rootView.findViewById(R.id.view);
        linearLayout.setVisibility(View.VISIBLE);

//        UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
//
//        list = new ArrayList<UserAccPhotographerData>();
//        list.add(data);
//        list.add(data);
//        list.add(data);
//        list.add(data);
//        list.add(data);
//        list.add(data);
//        list.add(data);
      /*  mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
                list = new ArrayList<UserAccPhotographerData>();
                list.add(data);

                numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                location.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                location.setTextColor(getResources().getColor(R.color.colorWhite));
                numEvents.setTextColor(getResources().getColor(R.color.colorProfile));
                date.setTextColor(getResources().getColor(R.color.colorProfile));
                date.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
                recyclerView.setAdapter(userAccPhotographersAdaptor);
            }
        });
        numEvents.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
                list = new ArrayList<UserAccPhotographerData>();
                list.add(data);
                list.add(data);
    ؤا
            }
        });
        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccPhotographerData data = new UserAccPhotographerData("Ahmed Mohamed", "Cairo, Egypt", "EGP", "2000", "Male");
                list = new ArrayList<UserAccPhotographerData>();


                numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                location.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                date.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                numEvents.setTextColor(getResources().getColor(R.color.colorProfile));
                location.setTextColor(getResources().getColor(R.color.colorProfile));
                date.setTextColor(getResources().getColor(R.color.colorWhite));

                list.add(data);
                list.add(data);
                list.add(data);
                list.add(data);
                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
                recyclerView.setAdapter(userAccPhotographersAdaptor);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener((ReadData.AllProsListener) this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                linearLayout.setVisibility(GONE);
                fragmentManager = getActivity().getSupportFragmentManager();
                 UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_o, frag).commit();

            }
            @Override
            public void onLongClick(View view, int position) {
                linearLayout.setVisibility(GONE);
                UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, frag).commit();
            }
        }));

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(list);
        recyclerView.setAdapter(userAccPhotographersAdaptor);*/
  /*      ReadData readData = new ReadData();
        readData.getAllPros(new ReadData.AllProsListener() {
            @Override
            public void onProsResponse(final ArrayList<ProUserModel> pros) {
                //deal with pros
                Log.v("ALlPros", pros.toString());
                mLayoutManager = new LinearLayoutManager(getActivity());

                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

                if (savedInstanceState != null) {
                    // Restore saved layout manager type.
                    mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                            .getSerializable(KEY_LAYOUT_MANAGER);
                }
                location.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        location.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        location.setTextColor(getResources().getColor(R.color.colorWhite));
                        numEvents.setTextColor(getResources().getColor(R.color.colorProfile));
                        date.setTextColor(getResources().getColor(R.color.colorProfile));
                        date.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(pros);
                        recyclerView.setAdapter(userAccPhotographersAdaptor);
                    }
                });
                numEvents.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numEvents.setTextColor(getResources().getColor(R.color.colorWhite));
                        date.setTextColor(getResources().getColor(R.color.colorProfile));
                        location.setTextColor(getResources().getColor(R.color.colorProfile));
                        numEvents.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        location.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        date.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(pros);
                        recyclerView.setAdapter(userAccPhotographersAdaptor);
                    }
                });
                date.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numEvents.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        location.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        date.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        numEvents.setTextColor(getResources().getColor(R.color.colorProfile));
                        location.setTextColor(getResources().getColor(R.color.colorProfile));
                        date.setTextColor(getResources().getColor(R.color.colorWhite));

                        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(pros);
                        recyclerView.setAdapter(userAccPhotographersAdaptor);
                    }
                });
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                        recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, final int position) {
                        //Values are passing to activity & to fragment as well

                        linearLayout.setVisibility(GONE);
                        fragmentManager = getActivity().getSupportFragmentManager();
                        UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer(pros.get(position));
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container_o, frag).commit();

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        linearLayout.setVisibility(GONE);
                        UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer(pros.get(position));
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container_o, frag).commit();
                    }
                }));

                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(pros);
                recyclerView.setAdapter(userAccPhotographersAdaptor);

            }
        });*/
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }


        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        userAccPhotographersAdaptor = new UserAccPhotographersAdaptor(pros);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well

                linearLayout.setVisibility(GONE);
                fragmentManager = getActivity().getSupportFragmentManager();
                UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer(pros.get(position));
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_o, frag).commit();

            }

            @Override
            public void onLongClick(View view, int position) {
                linearLayout.setVisibility(GONE);
                UserProfilePhotographersTabSearchOutputSelectPhotographer frag = new UserProfilePhotographersTabSearchOutputSelectPhotographer(pros.get(position));
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_o, frag).commit();
            }
        }));
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


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
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
