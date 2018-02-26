package com.indeves.chmplinapp.Utility;

/**
 * Created by boda on 2/26/18.
 */

public class CheckError {

    public Boolean checkPassMatch(String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            return true;
        } else {
            return false;
        }

    }

    public Boolean checkEmpty(String phone, String mail, String pass, String confirm) {
        if (phone.isEmpty()) {
            return false;
        } else if (mail.isEmpty()) {
            return false;
        } else if (pass.isEmpty()) {
            return false;
        } else if (confirm.isEmpty()) {
            return false;
        }else {
            return true;
        }
    }

}
