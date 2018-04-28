package com.indeves.chmplinapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.indeves.chmplinapp.API.FirebaseEventsListener;
import com.indeves.chmplinapp.API.PaymentServicesAPI;
import com.indeves.chmplinapp.API.WriteData;
import com.indeves.chmplinapp.Activities.Contactpro;
import com.indeves.chmplinapp.Models.EventModel;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.StepProgressBar;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.paymob.acceptsdk.IntentConstants;
import com.paymob.acceptsdk.PayActivity;
import com.paymob.acceptsdk.PayActivityIntentKeys;
import com.paymob.acceptsdk.PayResponseKeys;
import com.paymob.acceptsdk.SaveCardResponseKeys;
import com.paymob.acceptsdk.ToastMaker;

import java.util.ArrayList;

public class PaymentFragment extends StepProgressBar implements View.OnClickListener, FirebaseEventsListener, AdapterView.OnItemSelectedListener, PaymentServicesAPI.PaymentServiceListener {

    static final int ACCEPT_PAYMENT_REQUEST = 10;
    Spinner paymentMethodSpinner;
    ArrayList<String> paymentMethods;
    String selectedProId;
    int selectedPackagePrice;
    ProgressDialog progressDialog;
    EventModel selectedEvent;
    double paymentAmount = 0;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PaymentFragment(String proId, EventModel selectedEvent) {
        this.selectedProId = proId;
        this.selectedEvent = selectedEvent;
        this.selectedPackagePrice = selectedEvent.getSelectedPackage().getPrice();
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
        paymentMethodSpinner = (Spinner) rootview.findViewById(R.id.paymentMethod_spinner);
        paymentMethodSpinner.setOnItemSelectedListener(this);
        paymentMethods = new ArrayList<String>();
        paymentMethods.add("Choose payment method");
        paymentMethods.add("Cash");
        paymentMethods.add("Credit card");
        ArrayAdapter<String> paymentMethodsArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, paymentMethods);
        paymentMethodsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(paymentMethodsArrayAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
//        stateprogressbar.setAllStatesCompleted(true);

    }


    @Override
    public void onWriteDataCompleted(boolean writeSuccessful) {

    }

    @Override
    public void onReadDataResponse(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (paymentMethods.get(position).equals("Cash")) {
            //K.A go to last step with cash payment description
            proceedAfterPayment("booked-cash");
        } else if (paymentMethods.get(position).equals("Credit card")) {
            try {
                progressDialog.show();
                PaymentServicesAPI paymentServicesAPI = new PaymentServicesAPI(this);
                //payment amount is 20 % of selected package price
                paymentAmount = ((0.2) * selectedPackagePrice) * 100; // K.A: amount is multiplied by 100 to be converted in cents as per paymob solutions request
                paymentServicesAPI.getPaymentKey(paymentAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRetrievePaymentKey(String paymentKey) {
        progressDialog.dismiss();
        if (paymentKey != null) {
            Log.v("PaymentKeyResponse", paymentKey);
            Intent pay_intent = new Intent(getContext(), PayActivity.class);

            putNormalExtras(pay_intent, paymentKey);
            pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, true);
            pay_intent.putExtra(PayActivityIntentKeys.SHOW_ALERTS, false);
            pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, true);
            pay_intent.putExtra(PayActivityIntentKeys.THEME_COLOR, 0x8033B5E5);

            startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST);
        } else {
            Toast.makeText(getContext(), "Error in payment process!", Toast.LENGTH_SHORT).show();
        }

    }

    private void putNormalExtras(Intent intent, String paymentKey) {
        // Pass the correct values for the billing data keys
        //TODO: pass real data after finishing testing
        intent.putExtra(PayActivityIntentKeys.FIRST_NAME, "first_name");
        intent.putExtra(PayActivityIntentKeys.LAST_NAME, "last_name");
        intent.putExtra(PayActivityIntentKeys.BUILDING, "1");
        intent.putExtra(PayActivityIntentKeys.FLOOR, "1");
        intent.putExtra(PayActivityIntentKeys.APARTMENT, "1");
        intent.putExtra(PayActivityIntentKeys.CITY, "cairo");
        intent.putExtra(PayActivityIntentKeys.STATE, "new_cairo");
        intent.putExtra(PayActivityIntentKeys.COUNTRY, "egypt");
        intent.putExtra(PayActivityIntentKeys.EMAIL, "email@gmail.com");
        intent.putExtra(PayActivityIntentKeys.PHONE_NUMBER, "2345678");
        intent.putExtra(PayActivityIntentKeys.POSTAL_CODE, "3456");

        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, paymentKey);

        intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        if (requestCode == ACCEPT_PAYMENT_REQUEST) {

            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(getActivity(), "User canceled!!");
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(getActivity(), "Missing Argument == " + extras.getString(IntentConstants.MISSING_ARGUMENT_VALUE));
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(getActivity(), "Reason == " + extras.getString(IntentConstants.TRANSACTION_ERROR_REASON));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // User attempted to pay but their transaction was rejected

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(getActivity(), extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(getActivity(), extras.getString(IntentConstants.RAW_PAY_RESPONSE));
                Log.e("TransactionIssue", extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {
                // User finished their payment successfully

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                Toast.makeText(getContext(), "Paid successfully!", Toast.LENGTH_LONG).show();
                proceedAfterPayment("booked-credit");
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading  the returned JSON.
                ToastMaker.displayShortToast(getActivity(), "TRANSACTION_SUCCESSFUL - Parsing Issue");

                // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want
//                ToastMaker.displayShortToast(getActivity(), "Token == " + extras.getString(SaveCardResponseKeys.TOKEN));
                Toast.makeText(getContext(), "Paid successfully!", Toast.LENGTH_LONG).show();
                proceedAfterPayment("booked-credit");
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayShortToast(getActivity(), "User canceled 3-d secure verification!!");

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(getActivity(), extras.getString(PayResponseKeys.PENDING));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(getActivity(), "User canceled 3-d secure verification - Parsing Issue!!");

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(getActivity(), extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            }
        }
    }

    void proceedAfterPayment(final String status) {
        progressDialog.show();
        WriteData writeData = new WriteData(new FirebaseEventsListener() {
            @Override
            public void onWriteDataCompleted(boolean writeSuccessful) {
                progressDialog.dismiss();
                if (writeSuccessful) {
                    Contactpro frag = new Contactpro(selectedProId, paymentAmount, status);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_o, frag).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getContext(), "Payment is done, but system failed to save its data, contact admin for details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReadDataResponse(DataSnapshot dataSnapshot) {

            }
        });
        try {
            writeData.updateEventStatusAfterPayment(status, selectedEvent.getEventId(), paymentAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}









