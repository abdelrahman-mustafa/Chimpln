package com.indeves.chmplinapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import com.indeves.chmplinapp.Adapters.SpinnerCustomArrayAdapter;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.Calendar;
import java.util.Date;

import static com.indeves.chmplinapp.R.id.booking_time_layout;
import static com.indeves.chmplinapp.R.id.start;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Booking");
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        stateprogressbar.setAllStatesCompleted(false);
        eDate=(TextView)findViewById(R.id.booking_textview_edate);
        eDate.setOnClickListener(this);
        timefrom=(TextView)findViewById(R.id.booking_textview_from);
        timefrom.setOnClickListener(this);
        timeto=(TextView)findViewById(R.id.booking_textview_to);
        timeto.setOnClickListener(this);
        proName=(TextView)findViewById(R.id.booking_textView_proname);
        proDetails=(TextView)findViewById(R.id.booking_textview_prodetails);
        proPhoto=(ImageView)findViewById(R.id.booking_imageView_prophoto);
        proedit =(Button)findViewById(R.id.booking_button_edit);
        proceed=(Button)findViewById(R.id.booking_proceed_button);
        proceed.setOnClickListener(this);
        proedit.setOnClickListener(this);
        eTime=(Spinner)findViewById(R.id.booking_spinner_etime);
        eType=(Spinner)findViewById(R.id.booking_spiner_etype);
        photoShare=(Spinner)findViewById(R.id.booking_spinner_photoshare);
        eLoction=(Spinner)findViewById(R.id.booking_spinner_eloction);
        note=(EditText)findViewById(R.id.booking_editText_note);
        final Calendar cal = Calendar.getInstance();
        dpyear=cal.get(Calendar.YEAR);
        dpmonth=cal.get(Calendar.MONTH);
        dpday=cal.get(Calendar.DAY_OF_MONTH);
        layout=(LinearLayout)findViewById(booking_time_layout);
        layout.setVisibility(View.GONE);
        timeto.setVisibility(View.GONE);
        timefrom.setVisibility(View.GONE);
        addressLine=(EditText)findViewById(R.id.booking_edittext_insertaddrres);
        addressLine=(EditText)findViewById(R.id.booking_edittext_insertaddrres) ;
        addressLine.setVisibility(View.GONE);
        SpinnerCustomArrayAdapter eTimeSpinner = new SpinnerCustomArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);

        eTimeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eTimeSpinner.add("Full Day");
        eTimeSpinner.add("Half Day");
        eTimeSpinner.add("Per hour");
        eTimeSpinner.add("Select event Time");

        eTime.setAdapter(eTimeSpinner);
        eTime.setSelection(eTimeSpinner.getCount());
        eTime.setOnItemSelectedListener(this);
        timeedit=(Button)findViewById(R.id.booking_button_timeedit);
        timeedit.setOnClickListener(this);
        timeedit.setVisibility(View.GONE);
        eType.setOnItemSelectedListener(this);
        SpinnerCustomArrayAdapter eTypeSpinner = new SpinnerCustomArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);

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
        SpinnerCustomArrayAdapter photoShareSpinner = new SpinnerCustomArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);

        photoShareSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        photoShareSpinner.add("Let pro share photos");
        photoShareSpinner.add("share after approval up to 10 pic`s ");
        photoShareSpinner.add("don`t share any");
        photoShareSpinner.add("Select photo sharing option");
        photoShare.setAdapter(photoShareSpinner);
        photoShare.setSelection(photoShareSpinner.getCount());
        eLoction.setOnItemSelectedListener(this);
        SpinnerCustomArrayAdapter loctionSpinner = new SpinnerCustomArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);

        loctionSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loctionSpinner.add("Insert Addrees");
        loctionSpinner.add("Current Location");
        loctionSpinner.add("Select location option");
        eLoction.setAdapter(loctionSpinner);
        eLoction.setSelection(loctionSpinner.getCount());






    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (v==proceed)
        { /*sAddress=addressLine.getText().toString();
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
            Toast.makeText(this, sAddress+"\n" +sType+"\n" +sTime+"\n" +sNote+"\n" +sDate+"\n" +sShareable, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,Approval.class);
                intent.putExtra("address",sAddress);
                intent.putExtra("date",sDate);
                intent.putExtra("time",sTime);
                intent.putExtra("note",sNote);
                intent.putExtra("share",sShareable);
                intent.putExtra("type",sType);
                stateprogressbar.checkStateCompleted(true);
                startActivity(intent);
*/
            startActivity(new Intent(Booking.this,Approval.class));


        }
        //else Toast.makeText(this, "Check Your data", Toast.LENGTH_SHORT).show();}
        if (v==eDate)
        {

                showDialog(Did);boolDate=true;

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
            TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivityForResult(intent, 1);
                    }
                    break;
                }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected Dialog onCreateDialog(int id){
        if (id ==Did ){DatePickerDialog c = new DatePickerDialog( this,dlistener ,dpyear , dpmonth, dpday);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                addressLine.setText(result);
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
