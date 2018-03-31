package com.indeves.chmplinapp.Fragments;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Adapters.ProProfPackageAdaptor;
import com.indeves.chmplinapp.Models.MyPackageData;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.SwipeController;
import com.indeves.chmplinapp.Utility.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;


public class ProProfilePackageTabMyPackages extends android.support.v4.app.Fragment implements FirebaseEventsListener {
    //    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final float buttonWidth = 300;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    ProProfPackageAdaptor userProfEventsAdaptor;
    SwipeController swipeController = null;
    ProUserModel proUserModel;
    Boolean swipeBack = false;
    private List<PackageModel> list;
    private RecyclerView recyclerView;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private Paint p = new Paint();
    private PackageModel deletedPackage;
    private StuProfilePackageTabMyPackages.ButtonsState buttonShowedState = StuProfilePackageTabMyPackages.ButtonsState.GONE;

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
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<PackageModel>();

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                // A.M : Here you add the function of delete
                deletedPackage = list.get(position);
                list.remove(position);
                ProUserModel proUserModel = new ProUserModel();
                proUserModel.setPackages(list);
                WriteData writeData = new WriteData(ProProfilePackageTabMyPackages.this);
                try {
                    writeData.updateUserProfileData(proUserModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLeftClicked(int position) {
                // A.M : Here you add the function of edit
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                EditPackageDetails editPackageDetails = EditPackageDetails.newInstance(list.get(position), proUserModel);
                fragmentTransaction.replace(R.id.main_container, editPackageDetails).addToBackStack(null).commit();

            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        userProfEventsAdaptor = new ProProfPackageAdaptor(list);
        ReadData readData = new ReadData(this);
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        if (writeSuccessful) {
            Toast.makeText(getContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            userProfEventsAdaptor.notifyDataSetChanged();
            if (deletedPackage != null) {
                if (proUserModel.getPackages().contains(deletedPackage)) {
                    proUserModel.getPackages().remove(deletedPackage);
                }
            }
        } else {
            Toast.makeText(getContext(), "Failed to delete package", Toast.LENGTH_SHORT).show();
            if (deletedPackage != null) {
                list.add(deletedPackage);
                userProfEventsAdaptor.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            proUserModel = dataSnapshot.getValue(ProUserModel.class);
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

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
