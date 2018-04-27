package com.indeves.chmplinapp.Models;

import android.widget.ImageView;

/**
 * Created by boda on 4/25/18.
 */

public class EventType {
    String type;
    ImageView pic;

    public EventType(String type) {
        this.type = type;
   //     this.pic = pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageView getPic() {
        return pic;
    }

    public void setPic(ImageView pic) {
        this.pic = pic;
    }
}
