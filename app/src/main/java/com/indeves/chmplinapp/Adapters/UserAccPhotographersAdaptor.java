package com.indeves.chmplinapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indeves.chmplinapp.Models.ProUserModel;
import com.indeves.chmplinapp.Models.UserAccPhotographerData;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAccPhotographersAdaptor extends RecyclerView.Adapter<UserAccPhotographersAdaptor.MyViewHolder> {
    List<ProUserModel> list;

    Context context;
    public UserAccPhotographersAdaptor(Context context, List<ProUserModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_profile_tab_photographers_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProUserModel pData = list.get(position);
        holder.Name.setText(pData.getName());
        holder.Location.setText(pData.getCity());
        holder.Price.setVisibility(View.INVISIBLE);
        holder.Price.setText(" ");
        holder.Gender.setText(pData.getGender());
        holder.Currency.setText("");
        holder.Currency.setVisibility(View.INVISIBLE);
        Picasso.with(context).load(pData.getProfilePicUrl()).resize(170,200).transform(new CircleTransform()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView Name, Location, Gender, Price, Currency;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            Name = view.findViewById(R.id.userProfile_phot_search_name);
            Gender = view.findViewById(R.id.userProfile_phot_search_gender);
            Location = view.findViewById(R.id.userProfile_phot_search_location);
            Currency = view.findViewById(R.id.userProfile_phot_search_currency);
            Price = view.findViewById(R.id.userProfile_phot_search_price);
            imageView = view.findViewById(R.id.userProfile_phot_search_pic);

        }
    }
}
