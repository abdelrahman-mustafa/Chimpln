package com.indeves.chmplinapp.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class PaymentFragment extends StepProgressBar implements View.OnClickListener, FirebaseEventsListener, AdapterView.OnItemSelectedListener {
    EditText num,name,date,cvv;
    Spinner payType,cardType;
    Button addnewCard,addcard;




    public PaymentFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_payment, container, false);
        num=(EditText)rootview.findViewById(R.id.pay_num_edittext);
        name=(EditText)rootview.findViewById(R.id.pay_name_editText);
        date=(EditText)rootview.findViewById(R.id.pay_date_edittext);
        cvv=(EditText)rootview.findViewById(R.id.pay_cvv_edittext);
        payType=(Spinner)rootview.findViewById(R.id.paytype_spinner);
        cardType=(Spinner)rootview.findViewById(R.id.pay_cardtype_spinner);
        addnewCard=(Button)rootview.findViewById(R.id.pay_addnewcard_button);
        addcard=(Button)rootview.findViewById(R.id.pay_addcard_button);
        addcard.setOnClickListener(this);
        addnewCard.setOnClickListener(this);
        payType.setOnItemSelectedListener(this);
        cardType.setOnItemSelectedListener(this);






        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        stateprogressbar.setAllStatesCompleted(true);


    }


    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}









