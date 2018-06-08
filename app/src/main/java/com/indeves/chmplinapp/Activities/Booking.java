package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Adapters.SpinnerCustomArrayAdapter;
import com.indeves.chmplinapp.Fragments.UserProfilePhotographersTabSearch;
import com.indeves.chmplinapp.Fragments.UserProfilePhotographersTabSearchSelect;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.indeves.chmplinapp.R.id.booking_time_layout;
import static com.indeves.chmplinapp.R.id.visible;

public class Booking extends StepProgressBar implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    static final int Did = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String[] options = {"Select location option", "Current Location", "Insert Addrees"};
    Spinner eTime, eType, photoShare, eLoction, ePackage;
    TextView proName, proDetails, eDate, timefrom, timeto;
    ImageView proPhoto;
    Button proedit, proceed, timeedit, dateedit;
    EditText note, addressLine;
    int dpyear, dpday, dpmonth;
    LinearLayout layout;
    String sDate;
    View view_bookig;
    LookUpModel sTime;
    LookUpModel sType;
    LookUpModel sSharingOption;
    List<PackageModel> ePackageSpinner = new ArrayList<>();
    PackageModel packageData;
    String bookerName;
    String sShareable;
    String sAddress;
    String sNote;
    Boolean boolDate = false, booltime = false, boolType = false, boolAddress = false, fromto = true, from = false, to = false, boolshareable = false, boolPackageType = false;
    int REQUST = 1;
    int PLACE_PICKER_REQUEST = 5;
    Geocoder geocoder;
    ProgressDialog progressDialog;
    Place place = null;
    List<LookUpModel> eTypeSpinner = new ArrayList<>();
    List<LookUpModel> eTimeSpinner = new ArrayList<>();
    List<LookUpModel> photoShareSpinner = new ArrayList<>();
    EventModel eventModel;
    ProUserModel pro;
    String type;
    Button button;
    private String mParam1;
    private String mParam2;
    private DatePickerDialog.OnDateSetListener dlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dpyear = i;
            dpmonth = i1 + 1;
            dpday = i2;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            boolDate = true;
            eDate.setText(String.valueOf(dpday) + "-" + months[dpmonth - 1] + "-" + String.valueOf(dpyear));

        }
    };
    private FirebaseEventsListener firebaseEventsListener = new FirebaseEventsListener() {
        @Override
        public void onWriteDataCompleted(boolean writeSuccessful) {
            progressDialog.dismiss();
            if (writeSuccessful) {
                Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                stateprogressbar.checkStateCompleted(true);
                Approval output = new Approval(eventModel);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Log.i("done", "done");
                transaction.replace(R.id.container_o, output).addToBackStack(null).commit();

            }
        }

        @Override
        public void onReadDataResponse(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                if (dataSnapshot.getValue(UserData.class).getName() != null && !(dataSnapshot.getValue(UserData.class).getName().equals(""))) {
                    bookerName = dataSnapshot.getValue(UserData.class).getName();
                } else bookerName = dataSnapshot.getValue(UserData.class).getEmail();
            } else {
                Toast.makeText(getContext(), "Error loading your data", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @SuppressLint("ValidFragment")
    public Booking(ProUserModel pro, String type) {
        this.pro = pro;
    }

    public Booking() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_booking, container, false);
        //button=(Button)rootview.findViewById(R.id.btnNext);
        //button.setOnClickListener(this);
        eDate = (TextView) rootview.findViewById(R.id.booking_textview_edate);
        eDate.setOnClickListener(this);
        timefrom = (TextView) rootview.findViewById(R.id.booking_textview_from);
        timefrom.setOnClickListener(this);
        timeto = (TextView) rootview.findViewById(R.id.booking_textview_to);
        timeto.setOnClickListener(this);
        proName = (TextView) rootview.findViewById(R.id.booking_textView_proname);
        proDetails = (TextView) rootview.findViewById(R.id.booking_textview_prodetails);
        proPhoto = (ImageView) rootview.findViewById(R.id.booking_imageView_prophoto);
        //  proedit = (Button) rootview.findViewById(R.id.booking_button_edit);
        proceed = (Button) rootview.findViewById(R.id.booking_proceed_button);
        proceed.setOnClickListener(this);
        //   proedit.setOnClickListener(this);
        eTime = (Spinner) rootview.findViewById(R.id.booking_spinner_etime);
        eType = (Spinner) rootview.findViewById(R.id.booking_spiner_etype);
        photoShare = (Spinner) rootview.findViewById(R.id.booking_spinner_photoshare);
        eLoction = (Spinner) rootview.findViewById(R.id.booking_spinner_eloction);
        note = (EditText) rootview.findViewById(R.id.booking_editText_note);
        final Calendar cal = Calendar.getInstance();
        dpyear = cal.get(Calendar.YEAR);
        dpmonth = cal.get(Calendar.MONTH);
        dpday = cal.get(Calendar.DAY_OF_MONTH);
        layout = (LinearLayout) rootview.findViewById(booking_time_layout);
        layout.setVisibility(View.GONE);
        timeto.setVisibility(View.GONE);
        timefrom.setVisibility(View.GONE);
        addressLine = (EditText) rootview.findViewById(R.id.booking_edittext_insertaddrres);
        addressLine = (EditText) rootview.findViewById(R.id.booking_edittext_insertaddrres);
        addressLine.setVisibility(View.GONE);
        ePackage = (Spinner) rootview.findViewById(R.id.booking_spiner_package);
        ePackage.setOnItemSelectedListener(this);
        view_bookig = (View) rootview.findViewById(R.id.booking_view);
        view_bookig.setVisibility(View.GONE);
        dateedit = (Button) rootview.findViewById(R.id.booking_button_dateedit);
        dateedit.setOnClickListener(this);


        //    eTimeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       /* eTimeSpinner.add("Full Day");
        eTimeSpinner.add("Half Day");
        eTimeSpinner.add("Per hour");*/
        photoShare.setOnItemSelectedListener(this);
        ePackage.setVisibility(View.GONE);




      /*  eTypeSpinner.add("Kids");
        eTypeSpinner.add("Wedding");
        eTypeSpinner.add("Photo Session");
        eTypeSpinner.add("Birthday");
        eTypeSpinner.add("Fan Day");*/
        eTime.setOnItemSelectedListener(this);
        timeedit = (Button) rootview.findViewById(R.id.booking_button_timeedit);
        timeedit.setOnClickListener(this);
        timeedit.setVisibility(View.GONE);
        eType.setOnItemSelectedListener(this);

        // photoShare.setSelection(photoShareSpinner.getCount());
        eLoction.setOnItemSelectedListener(this);
        // ArrayAdapter loctionSpinner = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> loctionSpinner = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, options);

        loctionSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  loctionSpinner.add("Insert Addrees");

       /* loctionSpinner.add("Select location option");
        loctionSpinner.add("Current Location");*/
        eLoction.setAdapter(loctionSpinner);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        proName.setText(pro.getName());
        proDetails.setText(pro.getCity() + "-" + pro.getCountry());
        Picasso.with(getContext()).load(pro.profilePicUrl).resize(45, 45).transform(new CircleTransform()).into(proPhoto);
        ReadData readData = new ReadData(firebaseEventsListener);
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eTypeSpinner.add(new LookUpModel(0, "Select Event Type"));

                eTypeSpinner.addAll(eventTypeLookups);

                ArrayAdapter<LookUpModel> eventTypeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eTypeSpinner);
                eventTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                eType.setAdapter(eventTypeArrayAdapter);
            }
        });

        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eTimeSpinner.add(new LookUpModel(0, "Select Event Time"));
                eTimeSpinner.addAll(eventTypeLookups);
                ArrayAdapter<LookUpModel> eventTimeArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, eTimeSpinner);
                eventTimeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                eTime.setAdapter(eventTimeArrayAdapter);


            }
        });
        readData.getLookupsByType("shringOptionLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                photoShareSpinner.add(new LookUpModel(0, "Select photo sharing option"));
                photoShareSpinner.addAll(eventTypeLookups);

                ArrayAdapter<LookUpModel> eventSharingOptionArrayAdapter = new ArrayAdapter<LookUpModel>(getContext(), android.R.layout.simple_spinner_item, photoShareSpinner);

                eventSharingOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                photoShare.setAdapter(eventSharingOptionArrayAdapter);


            }
        });
        readData.getUserInfoById(FirebaseAuth.getInstance().getCurrentUser().getUid());


        return rootview;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        stateprogressbar.setAllStatesCompleted(false);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (v == proedit) {
            UserProfilePhotographersTabSearchSelect output = new UserProfilePhotographersTabSearchSelect();
            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container_o, output).commit();

        }
        if (v == proceed) {
            sAddress = addressLine.getText().toString();
            if (sAddress.length() > 5) {
                boolAddress = true;
            }
            Log.i("14", String.valueOf(boolAddress));
            Log.i("1", String.valueOf(boolDate));
            Log.i("2", String.valueOf(booltime));
            Log.i("3", String.valueOf(boolType));
            Log.i("4", String.valueOf(boolAddress));
            Log.i("5", String.valueOf(fromto));
            Log.i("6", String.valueOf(from));
            Log.i("7", String.valueOf(to));
            Log.i("8", String.valueOf(boolshareable));
            Log.i("9", sAddress + "hi" + "\n" + note.getText().toString());
            Log.i("10", String.valueOf(sAddress.length()));
            if (proceedbtn(boolDate, booltime, boolType, boolAddress, fromto, from, to, boolshareable, boolPackageType)) {
                if (sTime.getId() == 0) {
                    Toast.makeText(getContext(), "Please Select Event Time ", Toast.LENGTH_LONG).show();
                    booltime = false;
                } else if (sType.getId() == 0) {
                    Toast.makeText(getContext(), "Please Select Event Type ", Toast.LENGTH_LONG).show();
                    boolType = false;
                } else if (sSharingOption.getId() == 0) {
                    Toast.makeText(getContext(), "Please Select Sharing Option ", Toast.LENGTH_LONG).show();
                    boolshareable = false;
                } else {

                    sDate = eDate.getText().toString();
                    sNote = note.getText().toString();
                    // if (fromto==true){sTime=timefrom.getText().toString()+"  to  "+timeto.getText().toString();}
//                    Toast.makeText(getContext(), sAddress + "\n" + sType + "\n" + sTime + "\n" + sNote + "\n" + sDate + "\n" + sShareable, Toast.LENGTH_LONG).show();
               /* Intent intent=new Intent(this,Approval.class);
                intent.putExtra("address",sAddress);
                intent.putExtra("date",sDate);
                intent.putExtra("time",sTime);
                intent.putExtra("note",sNote);
                intent.putExtra("share",sShareable);
                intent.putExtra("type",sType);
                stateprogressbar.checkStateCompleted(true);
                startActivity(intent);*/

                    // startActivity(new Intent(Booking.this,Approval.class));
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    if (timefrom.getText().toString().equals("From")) {
                        timefrom.setText("");
                    }
                    if (timeto.getText().toString().equals("To")) {
                        timeto.setText("");
                    }

                    long latitude = 0, longitude = 0;
                    if (place != null) {
                        latitude = (long) place.getLatLng().latitude;
                        longitude = (long) place.getLatLng().longitude;
                    }

                    eventModel = new EventModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), pro.getUid(), sDate, bookerName, pro.getName(), timefrom.getText().toString(), timeto.getText().toString(), sNote, sAddress, longitude, latitude, "pending", sType.getId(), sTime.getId(), sSharingOption.getId(), packageData);
                    WriteData writeData = new WriteData(firebaseEventsListener);
                    try {
                        writeData.bookNewEvent(eventModel);
                        Log.i("hi", eventModel.toString());
                    } catch (Exception e) {
                        //User is not authenticated
                        e.printStackTrace();
                    }


                }
            }
        }
        //else Toast.makeText(this, "Check Your data", Toast.LENGTH_SHORT).show();}
        if (v == dateedit) {
            DatePickerDialog c = new DatePickerDialog(getContext(), dlistener, dpyear, dpmonth, dpday);
            c.getDatePicker().setMinDate(new Date().getTime());
            c.show();

        }
        if (v == eDate) {

            DatePickerDialog c = new DatePickerDialog(getContext(), dlistener, dpyear, dpmonth, dpday);
            c.getDatePicker().setMinDate(new Date().getTime());
            c.show();
            boolDate = true;
            eDate.setClickable(false);

        }
        if (v == timeedit) {
            eTime.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            timeto.setVisibility(View.GONE);
            timefrom.setVisibility(View.GONE);
            timeedit.setVisibility(View.GONE);
            booltime = true;
            fromto = false;

        }
        if (v == timeto) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String am_pm = "";

                    Calendar datetime = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);

                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                        am_pm = "AM";
                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                        am_pm = "PM";

                    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                    timeto.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);
                    to = true;

                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
        if (v == timefrom) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String am_pm = "";

                    Calendar datetime = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);

                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                        am_pm = "AM";
                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                        am_pm = "PM";

                    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                    timefrom.setText(strHrsToShow + ":" + datetime.get(Calendar.MINUTE) + " " + am_pm);
                    from = true;

                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
    }

    @SuppressLint("ResourceAsColor")
    @Override

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (parent.getId()) {
            case R.id.booking_spinner_etime: {
                String eTimedata;
                if (position == 1 || position == 2) {
                    sTime = eTimeSpinner.get(position);

                    layout.setVisibility(View.GONE);
                    timeto.setVisibility(View.GONE);
                    timefrom.setVisibility(View.GONE);
                    timeedit.setVisibility(View.GONE);
                    booltime = true;

                } else if (position == 3) {
                    sTime = eTimeSpinner.get(position);

                    layout.setVisibility(View.VISIBLE);
                    timeto.setVisibility(View.VISIBLE);
                    timefrom.setVisibility(View.VISIBLE);
                    eTime.setVisibility(View.GONE);
                    timeedit.setVisibility(View.VISIBLE);
                    timefrom.setOnClickListener(this);
                    timeto.setOnClickListener(this);

                }


                break;
            }
            case R.id.booking_spiner_etype: {
                if (position != 0) {
                    sType = eTypeSpinner.get(position);
                    boolType = true;
                    view_bookig.setVisibility(View.VISIBLE);
                    ePackage.setVisibility(View.VISIBLE);
                    ePackageSpinner = new ArrayList<>();
                    if (pro.getPackages() != null) {

                        ePackageSpinner.add(new PackageModel("Please Select Package", "", 0, 0, 0));
                        for (int i = 0; i < pro.getPackages().size(); i++) {
                            if (pro.getPackages().get(i).getEventTypeId() == sType.getId() && pro.getPackages().get(i).getEventTimeId() == sTime.getId()) {
                                ePackageSpinner.add(pro.getPackages().get(i));
                            }
                        }

                        ArrayAdapter<PackageModel> typePackage = new ArrayAdapter<PackageModel>(getContext(), android.R.layout.simple_spinner_item, ePackageSpinner);

                        typePackage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ePackage.setAdapter(typePackage);

                        if (ePackageSpinner.size() == 1) {
                            ePackage.setVisibility(View.GONE);
                            view_bookig.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Sorry the Photographer doesn`t have packages ", Toast.LENGTH_LONG).show();
                        }

                    }

                }


                break;
            }
            case R.id.booking_spiner_package: {
                if (position > 0) {
                    packageData = ePackageSpinner.get(position);
                    boolPackageType = true;


                }
            }
            case R.id.booking_spinner_photoshare: {

                sSharingOption = photoShareSpinner.get(position);
                boolshareable = true;


                break;
            }
            case R.id.booking_spinner_eloction: {
                if (position == 2) {
                    addressLine.setVisibility(View.VISIBLE);
                    sAddress = addressLine.getText().toString();
                    if (addressLine.length() > 5) {
                        boolAddress = true;
                    }
                } else if (position == 1) {
                    addressLine.setVisibility(View.VISIBLE);
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected Dialog onCreateDialog(int id) {
        if (id == Did) {
            DatePickerDialog c = new DatePickerDialog(getContext(), dlistener, dpyear, dpmonth, dpday);
            c.getDatePicker().setMinDate(new Date().getTime());
            return c;
        } else return null;


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String result = data.getStringExtra("result");
                place = PlacePicker.getPlace(data, getContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String toastMsg = String.format("Place: %s", place.getName());
                if (place.getLatLng() != null) {
                    if (addresses != null) {

                        final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        final String city = addresses.get(0).getLocality();
                        Log.i("1", toastMsg);
                        Log.i("2", address + "\n" + city);
                        addressLine.setText(address);
                    } else addressLine.setText(toastMsg);
                    boolAddress = true;
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public boolean proceedbtn(Boolean boolDate, Boolean booltime, Boolean boolType, Boolean boolAddress, Boolean fromto, Boolean from, boolean to, Boolean boolshareable, Boolean x) {
        Boolean tfinal = false;
        if (booltime == false) {
            if (from == true && to == true) {
                tfinal = true;
            } else {
                tfinal = false;
                Toast.makeText(getContext(), "Please Select Event Time", Toast.LENGTH_LONG).show();
            }
        } else if (booltime == true) {
            tfinal = true;
        }
        if (boolAddress == false) {
            Toast.makeText(getContext(), "Please Select Event Loction", Toast.LENGTH_LONG).show();
        }
        if (boolDate == false) {
            Toast.makeText(getContext(), "Please Select Event Date", Toast.LENGTH_LONG).show();
        }
        if (boolshareable == false) {
            Toast.makeText(getContext(), "Please Select Event Share Option", Toast.LENGTH_LONG).show();
        }
        if (boolType == false) {
            Toast.makeText(getContext(), "Please Select Event Type", Toast.LENGTH_LONG).show();
        }
        if (x == false) {
            Toast.makeText(getContext(), "Please Select Package", Toast.LENGTH_LONG).show();
        }
        if (boolAddress == true && boolDate == true && boolshareable == true && boolType == true && tfinal == true && x == true) {
            return true;
        } else {
            return false;
        }

    }


}
