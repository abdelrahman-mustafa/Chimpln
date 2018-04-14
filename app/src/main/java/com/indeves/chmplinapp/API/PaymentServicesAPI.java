package com.indeves.chmplinapp.API;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

    public void getPaymentKey(double paymentAmount) {
        Map<String, Object> data = new HashMap<>();
        data.put("amount", paymentAmount);
        mFunctions.getHttpsCallable("pay").call(data)
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                        if (task.isSuccessful()) {
                            paymentServiceListener.onRetrievePaymentKey(task.getResult().getData().toString());
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.v("PaymentKeyException", details.toString());
                            }
                            paymentServiceListener.onRetrievePaymentKey(null);
                        }

                    }
                });
    }

    public interface PaymentServiceListener {
        void onRetrievePaymentKey(String paymentKey);
    }
}
