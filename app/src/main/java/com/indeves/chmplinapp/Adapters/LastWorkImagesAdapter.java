package com.indeves.chmplinapp.Adapters;

/**
 * Created by Khalid on 2/7/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indeves.chmplinapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LastWorkImagesAdapter extends RecyclerView.Adapter<LastWorkImagesAdapter.ViewHolder> {
    private ArrayList<String> imagesURLs;
    private Context context;

    public LastWorkImagesAdapter(Context context, ArrayList<String> imagesURLs) {
        this.imagesURLs = imagesURLs;
        this.context = context;
    }

    @Override
    public LastWorkImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.last_work_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LastWorkImagesAdapter.ViewHolder viewHolder, int i) {
        Picasso.with(context).load(imagesURLs.get(i)).placeholder(R.drawable.test_image).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return imagesURLs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_android;

        public ViewHolder(View view) {
            super(view);
            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}