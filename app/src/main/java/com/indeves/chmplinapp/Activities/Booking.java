package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.indeves.chmplinapp.Adapters.SpinnerCustomArrayAdapter;
import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.indeves.chmplinapp.R.id.booking_time_layout;

public class Booking extends StepProgressBar implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    Spinner eTime,eType,photoShare,eLoction;
    TextView proName,proDetails,eDate,timefrom,timeto;
    ImageView proPhoto;
    Button proedit,proceed,timeedit;
    EditText note,addressLine;
    int dpyear , dpday,dpmonth;
    static final int Did=0;
    LinearLayout layout;
    String sDate,sTime,sType,sShareable,sAddress,sNote;
    Boolean boolDate=false,booltime=false,boolType=false,boolAddress=false,fromto=true,from=false,to=false,boolshareable=false;
    SpinnerCustomArrayAdapter photoShareSpinner;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int REQUST=1;
    private String mParam1;
    private String mParam2;
    int PLACE_PICKER_REQUEST = 5;
    Geocoder geocoder;



    ProUserModel pro;
    String type;
    Button button;

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
        View rootview=inflater.inflate(R.layout.activity_booking, container, false);
        //button=(Button)rootview.findViewById(R.id.btnNext);
        //button.setOnClickListener(this);
        eDate=(TextView)rootview.findViewById(R.id.booking_textview_edate);
        eDate.setOnClickListener(this);
        timefrom=(TextView)rootview.findViewById(R.id.booking_textview_from);
        timefrom.setOnClickListener(this);
        timeto=(TextView)rootview.findViewById(R.id.booking_textview_to);
        timeto.setOnClickListener(this);
        proName=(TextView)rootview.findViewById(R.id.booking_textView_proname);
        proDetails=(TextView)rootview.findViewById(R.id.booking_textview_prodetails);
        proPhoto=(ImageView)rootview.findViewById(R.id.booking_imageView_prophoto);
        proedit =(Button)rootview.findViewById(R.id.booking_button_edit);
        proceed=(Button)rootview.findViewById(R.id.booking_proceed_button);
        proceed.setOnClickListener(this);
        proedit.setOnClickListener(this);
        eTime=(Spinner)rootview.findViewById(R.id.booking_spinner_etime);
        eType=(Spinner)rootview.findViewById(R.id.booking_spiner_etype);
        photoShare=(Spinner)rootview.findViewById(R.id.booking_spinner_photoshare);
        eLoction=(Spinner)rootview.findViewById(R.id.booking_spinner_eloction);
        note=(EditText)rootview.findViewById(R.id.booking_editText_note);
        final Calendar cal = Calendar.getInstance();
        dpyear=cal.get(Calendar.YEAR);
        dpmonth=cal.get(Calendar.MONTH);
        dpday=cal.get(Calendar.DAY_OF_MONTH);
        layout=(LinearLayout)rootview.findViewById(booking_time_layout);
        layout.setVisibility(View.GONE);
        timeto.setVisibility(View.GONE);
        timefrom.setVisibility(View.GONE);
        addressLine=(EditText)rootview.findViewById(R.id.booking_edittext_insertaddrres);
        addressLine=(EditText)rootview.findViewById(R.id.booking_edittext_insertaddrres) ;
        addressLine.setVisibility(View.GONE);
        SpinnerCustomArrayAdapter eTimeSpinner = new SpinnerCustomArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);

        eTimeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eTimeSpinner.add("Full Day");
        eTimeSpinner.add("Half Day");
        eTimeSpinner.add("Per hour");
        eTimeSpinner.add("Select event Time");

        eTime.setAdapter(eTimeSpinner);
        eTime.setSelection(eTimeSpinner.getCount());
        eTime.setOnItemSelectedListener(this);
        timeedit=(Button)rootview.findViewById(R.id.booking_button_timeedit);
        timeedit.setOnClickListener(this);
        timeedit.setVisibility(View.GONE);
        eType.setOnItemSelectedListener(this);
        SpinnerCustomArrayAdapter eTypeSpinner = new SpinnerCustomArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);

        eTypeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eTypeSpinner.add("Kids");
        eTypeSpinner.add("Wedding");
        eTypeSpinner.add("Photo Session");
        eTypeSpinner.add("Birthday");
        eTypeSpinner.add("Fan Day");
        eTypeSpinner.add("Select event Type");
        eType.setAdapter(eTypeSpinner);
        eType.setSelection(eTypeSpinner.getCount());
        photoShare.setOnItemSelectedListener(this);
        photoShareSpinner = new SpinnerCustomArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);

        photoShareSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoShareSpinner.add("Let pro share photos");
        photoShareSpinner.add("share after approval up to 10 pic`s ");
        photoShareSpinner.add("don`t share any");
        photoShareSpinner.add("Select photo sharing option");
        photoShare.setAdapter(photoShareSpinner);
        photoShare.setSelection(photoShareSpinner.getCount());
        eLoction.setOnItemSelectedListener(this);
        SpinnerCustomArrayAdapter loctionSpinner = new SpinnerCustomArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);

        loctionSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loctionSpinner.add("Insert Addrees");
        loctionSpinner.add("Current Location");
        loctionSpinner.add("Select location option");
        eLoction.setAdapter(loctionSpinner);
        eLoction.setSelection(loctionSpinner.getCount());
        geocoder = new Geocoder(getContext(), Locale.getDefault());













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
        if (v==proceed)
        { sAddress=addressLine.getText().toString();
            if (sAddress.length()>5){boolAddress=true;}
            Log.i("14",String.valueOf(boolAddress));
            Log.i("1",String.valueOf(boolDate));
            Log.i("2",String.valueOf(booltime));
            Log.i("3",String.valueOf(boolType));
            Log.i("4",String.valueOf(boolAddress));
            Log.i("5",String.valueOf(fromto));
            Log.i("6",String.valueOf(from));
            Log.i("7",String.valueOf(to));
            Log.i("8",String.valueOf(boolshareable));
            Log.i("9",sAddress+"hi"+"\n"+note.getText().toString());
            Log.i("10",String.valueOf(sAddress.length()));



            if(proceedbtn(  boolDate,  booltime,  boolType,  boolAddress,  fromto,  from,  to,  boolshareable))
            {


                sDate=eDate.getText().toString();
            sNote=note.getText().toString();
            if (fromto==true){sTime=timefrom.getText().toString()+"  to  "+timeto.getText().toString();}
            Toast.makeText(getContext(), sAddress+"\n" +sType+"\n" +sTime+"\n" +sNote+"\n" +sDate+"\n" +sShareable, Toast.LENGTH_LONG).show();
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
                stateprogressbar.checkStateCompleted(true);
                Approval output = new Approval();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container_o, output).commit();



            }}
        //else Toast.makeText(this, "Check Your data", Toast.LENGTH_SHORT).show();}
        if (v==eDate)
        {

            DatePickerDialog c = new DatePickerDialog( getContext(),dlistener ,dpyear , dpmonth, dpday);
            c.getDatePicker().setMinDate(new Date().getTime());
            c.show();
                boolDate=true;

        }
        if (v==timeedit)
            {
                eTime.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                timeto.setVisibility(View.GONE);
                timefrom.setVisibility(View.GONE);
                timeedit.setVisibility(View.GONE);
               booltime=true;
               fromto=false;

            }
        if (v==timeto){
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

                    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                    timeto.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
                    to=true;

                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
        if (v==timefrom){Calendar mcurrentTime = Calendar.getInstance();
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

                    String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                    timefrom.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
                    from=true;

                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
    }








    @SuppressLint("ResourceAsColor")
    @Override

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



              switch(parent.getId()) {
            case R.id.booking_spinner_etime:
            {
                String eTimedata;
               if (position==0||position==1){
                   sTime = parent.getItemAtPosition(position).toString();
                   layout.setVisibility(View.GONE);
                   timeto.setVisibility(View.GONE);
                   timefrom.setVisibility(View.GONE);
                   timeedit.setVisibility(View.GONE);
                   booltime=true;

               }
               else if (position==2)
               {
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
            case R.id.booking_spiner_etype:
                {
                    sType = parent.getItemAtPosition(position).toString();
                    boolType=true;

                break;
                }
            case R.id.booking_spinner_photoshare:
                {
                    sShareable = parent.getItemAtPosition(position).toString();
                    boolshareable=true;

                    break;
                }
            case R.id.booking_spinner_eloction:
                {
                    if (position==0){addressLine.setVisibility(View.VISIBLE);
                    sAddress=addressLine.getText().toString();
                    }
                    else if (position==1){
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

    protected Dialog onCreateDialog(int id){
        if (id ==Did ){DatePickerDialog c = new DatePickerDialog( getContext(),dlistener ,dpyear , dpmonth, dpday);
            c.getDatePicker().setMinDate(new Date().getTime());
            return c;}
        else return null;


    }
    private DatePickerDialog.OnDateSetListener dlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dpyear=i;
            dpmonth=i1+1;
            dpday=i2;
            String[] months= {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" };
            eDate.setText(String.valueOf(dpday)+"  "+months[dpmonth-1]+"   "+String.valueOf(dpyear));

        }
    };
    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Place place = PlacePicker.getPlace(data, getContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String toastMsg = String.format("Place: %s", place.getName());
                final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                final String city = addresses.get(0).getLocality();
                Log.i("1",toastMsg);
                Log.i("2",address+"\n"+city);
                addressLine.setText(address);
                boolAddress=true;

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public boolean proceedbtn(Boolean boolDate,Boolean booltime,Boolean boolType,Boolean boolAddress,Boolean fromto,Boolean from,boolean to,Boolean boolshareable){
       Boolean tfinal=false;
        if (booltime==false)
        {
            if (from==true&&to==true){tfinal=true;}
            else {tfinal=false;}
        }
        else if (booltime==true){tfinal=true;}

        if (boolAddress==true&&boolDate==true&&boolshareable==true&&boolType==true&&tfinal==true){return true;}
        else {return false;}

    }


}
