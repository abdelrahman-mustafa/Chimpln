package com.indeves.chmplinapp.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.CloudStorageAPI;
import com.indeves.chmplinapp.API.CloudStorageListener;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.ProRegActivity;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.StudioTeamMember;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddProToStudioFragment extends Fragment implements AdapterView.OnItemSelectedListener, FirebaseEventsListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "studioObject";
    private static int RESULT_LOAD_IMAGE = 1;
    EditText name, city;
    Spinner genderSpinner;
    ImageView imageView;
    ArrayList<String> genderCategories;
    String selectedGender;
    Bitmap selectedImage;
    ProgressDialog progressDialog;
    private ProUserModel studioObject;

    public AddProToStudioFragment() {
        // Required empty public constructor
    }

    public static AddProToStudioFragment newInstance(ProUserModel param1) {
        AddProToStudioFragment fragment = new AddProToStudioFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studioObject = (ProUserModel) getArguments().getSerializable(ARG_PARAM1);
        }
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait.. ");
        progressDialog.setCanceledOnTouchOutside(false);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pro_to_studio, container, false);
        name = view.findViewById(R.id.add_pro_first_name);
        city = view.findViewById(R.id.add_pro_city_editText);
        genderSpinner = view.findViewById(R.id.add_pro_spinner_gender);
        imageView = view.findViewById(R.id.add_pro_pic);
        imageView.setOnClickListener(this);
        genderCategories = new ArrayList<>();
        genderCategories.add(getResources().getString(R.string.gender));
        genderCategories.add("Male");
        genderCategories.add("Female");
        // Creating adapter for spinner
        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_large_text, genderCategories);
        // Drop down layout style - list view with radio button
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderArrayAdapter);
        genderSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_pro, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.studio_add_pro) {
            if (validateForm()) {
                progressDialog.show();
                CloudStorageAPI cloudStorageAPI = new CloudStorageAPI();
                cloudStorageAPI.UploadImage(selectedImage, false, new CloudStorageListener.UploadUserImageListener() {
                    @Override
                    public void onImageUpload(String downloadUrl) {
                        if (downloadUrl != null) {
                            ProUserModel proUserModel = new ProUserModel();
                            ArrayList<StudioTeamMember> studioTeamMembers;
                            if (studioObject.getStudioTeamMembers() == null) {
                                studioTeamMembers = new ArrayList<>();
                            } else {
                                studioTeamMembers = studioObject.getStudioTeamMembers();
                            }
                            StudioTeamMember studioTeamMember = new StudioTeamMember(downloadUrl, name.getText().toString(), city.getText().toString(), selectedGender);
                            studioTeamMembers.add(studioTeamMember);
                            proUserModel.setStudioTeamMembers(studioTeamMembers);
                            WriteData writeData = new WriteData(AddProToStudioFragment.this);
                            try {
                                writeData.updateUserProfileData(proUserModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed to save your data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedGender = genderCategories.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean validateForm() {
        boolean isvalid = true;
        if (name.getText().toString().isEmpty()) {
            isvalid = false;
            name.setError(getResources().getString(R.string.error_field_missing));
        } else if (city.getText().toString().isEmpty()) {
            isvalid = false;
            city.setError(getResources().getString(R.string.error_field_missing));
        } else if (selectedGender.equals(getResources().getString(R.string.gender))) {
            isvalid = false;
            Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
        } else if (selectedImage == null) {
            isvalid = false;
            Toast.makeText(getContext(), "Please set image", Toast.LENGTH_SHORT).show();
        }

        return isvalid;
    }

    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {
        progressDialog.dismiss();
        if (writeSuccessful) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }

        } else {
            Toast.makeText(getContext(), "Failed to save your data", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

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
                Picasso.with(getContext()).load(imageUri).resize(300, 300).transform(new CircleTransform()).into(imageView);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageView) {
            getImage();
        }
    }
}
