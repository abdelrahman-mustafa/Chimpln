package com.indeves.chmplinapp.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.CloudStorageListener;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.ProLandingPage;
import com.indeves.chmplinapp.Activities.StuLandingPage;
import com.indeves.chmplinapp.Adapters.AddImagesArrayAdapter;
import com.indeves.chmplinapp.Adapters.StudioTeamMembersAdapter;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class StuEditProfileFragment extends Fragment implements FirebaseEventsListener, View.OnClickListener, StudioTeamMembersAdapter.ItemClickListener {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final String ARG_PARAM1 = "studioObject";
    private static final String ARG_PARAM2 = "eventsCount";
    private static int RESULT_LOAD_IMAGE = 1;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    Context attachedActivityContext;
    TextView prosCount, eventsCountTextView, addPro;
    EditText proName, subInfoRow;
    RatingBar rate;
    ImageView profileImage;
    RecyclerView teamMembersListView;
    ProUserModel proUserModel;
    StudioTeamMembersAdapter studioTeamMembersAdapter;
    ProgressDialog progressDialog;
    Bitmap selectedImage;
    private int eventsCount;
    private CloudStorageListener.UploadUserImageListener cloudStorageListener = new CloudStorageListener.UploadUserImageListener() {
        @Override
        public void onImageUpload(String downloadUrl) {
            if (downloadUrl != null) {
                ProUserModel proUserModel1 = new ProUserModel();
                proUserModel1.setName(proName.getText().toString());
                proUserModel1.setStudioTeamMembers(proUserModel.getStudioTeamMembers());
                proUserModel1.setArea(subInfoRow.getText().toString());
                proUserModel1.setProfilePicUrl(downloadUrl);
                WriteData writeData = new WriteData(StuEditProfileFragment.this);
                try {
                    writeData.updateUserProfileData(proUserModel1);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "You are not authenticated", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getContext(), "Failed to update your data", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public StuEditProfileFragment() {
        // Required empty public constructor
    }

    public static StuEditProfileFragment newInstance(ProUserModel param1, int eventsCount) {
        StuEditProfileFragment fragment = new StuEditProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, eventsCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proUserModel = (ProUserModel) getArguments().getSerializable(ARG_PARAM1);
            eventsCount = getArguments().getInt(ARG_PARAM2);
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait.. ");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stu_edit_profile, container, false);
        proName = rootView.findViewById(R.id.stu_profile_name);
        prosCount = rootView.findViewById(R.id.stu_profile_prosCount);
        eventsCountTextView = rootView.findViewById(R.id.pro_profile_events);
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
        profileImage.setOnClickListener(this);
        setHasOptionsMenu(true);
        if (attachedActivityContext != null && ((StuLandingPage) attachedActivityContext).getSupportActionBar() != null) {
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((StuLandingPage) attachedActivityContext).getSupportActionBar().setTitle(getResources().getString(R.string.fragment_title_edit_profile));

        }
        displayUserInfo(proUserModel);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pro_edit_profile_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pro_profile_menu_save) {
            progressDialog.show();
            if (selectedImage == null) {
                ProUserModel proUserModel1 = new ProUserModel();
                proUserModel1.setName(proName.getText().toString());
                proUserModel1.setStudioTeamMembers(proUserModel.getStudioTeamMembers());
                proUserModel1.setArea(subInfoRow.getText().toString());
                WriteData writeData = new WriteData(StuEditProfileFragment.this);
                try {
                    writeData.updateUserProfileData(proUserModel1);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "You are not authenticated", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else {
                CloudStorageAPI cloudStorageAPI = new CloudStorageAPI();
                cloudStorageAPI.UploadImage(selectedImage, true, cloudStorageListener);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

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
        progressDialog.dismiss();
        if (writeSuccessful) {
            Toast.makeText(attachedActivityContext, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }

        } else {
            Toast.makeText(attachedActivityContext, "Profile is not updated ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

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
                studioTeamMembersAdapter = new StudioTeamMembersAdapter(getContext(), proUserModel.getStudioTeamMembers(), "edit");
                studioTeamMembersAdapter.setClickListener(StuEditProfileFragment.this);
                teamMembersListView.setAdapter(studioTeamMembersAdapter);
            }
            eventsCountTextView.setText(String.valueOf(eventsCount));

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
        if (profileImage == v) {
            getImage();
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
        if (proUserModel.getStudioTeamMembers() != null) {
            proUserModel.getStudioTeamMembers().remove(position);
            studioTeamMembersAdapter.notifyDataSetChanged();
        }
    }

    private void getImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMAGE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 300, 300, false));
                Picasso.with(getContext()).load(imageUri).resize(300, 300).transform(new CircleTransform()).into(profileImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
