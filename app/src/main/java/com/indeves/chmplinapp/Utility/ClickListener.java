package com.indeves.chmplinapp.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.indeves.chmplinapp.Activities.MainActivity;
import com.squareup.picasso.Transformation;

import static android.support.v7.widget.RecyclerView.*;




    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view,int position);
    }
