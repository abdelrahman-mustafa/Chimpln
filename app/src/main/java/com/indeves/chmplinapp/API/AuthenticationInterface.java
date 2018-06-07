package com.indeves.chmplinapp.API;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * Created by khalid on 28/02/18.
 */

public class AuthenticationInterface {
    public interface LoginListener {
        void onUserLoginComplete(boolean loginSuccessful);
    }

    public interface SinUpListener {
        void onSignUpWithEmailAndPasswordComplete(boolean signUpSuccessful);
    }

    public interface SignInWithPhoneAuthCredentialListener {
        void onSignInWithPhoneAuthCredentialCompleted(boolean codeVerified);
    }
    public interface ForgetPassListener {
        void onForgetPassListener(boolean mailSent);
    }


    public interface PhoneVerificationListener {
        void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential);

        void onCodeSent(String VerficationID, PhoneAuthProvider.ForceResendingToken forceResendingToken);

        void onVerificationFailed(FirebaseException e);
    }

}
