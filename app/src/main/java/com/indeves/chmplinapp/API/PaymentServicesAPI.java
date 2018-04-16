package com.indeves.chmplinapp.API;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class PaymentServicesAPI {
    private FirebaseFunctions mFunctions;
    private PaymentServiceListener paymentServiceListener;

    public PaymentServicesAPI(PaymentServiceListener paymentServiceListener) throws Exception {
        if (paymentServiceListener != null) {
            this.paymentServiceListener = paymentServiceListener;
            mFunctions = FirebaseFunctions.getInstance();
        } else throw new Exception("Must implement paymentServiceListener");
    }

    public void getPaymentKey(final double paymentAmountInCents) {
        Map<String, Object> data = new HashMap<>();
        data.put("amount", paymentAmountInCents);
        mFunctions.getHttpsCallable("pay").call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
            @Override
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                paymentServiceListener.onRetrievePaymentKey(httpsCallableResult.getData().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("GetPaymentTokenExc", e.toString());
                paymentServiceListener.onRetrievePaymentKey(null);

            }
        });
    }

    public interface PaymentServiceListener {
        void onRetrievePaymentKey(String paymentKey);
    }
}
