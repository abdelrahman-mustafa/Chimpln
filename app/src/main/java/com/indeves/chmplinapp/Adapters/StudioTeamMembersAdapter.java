package com.indeves.chmplinapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.indeves.chmplinapp.Models.StudioTeamMember;
import com.indeves.chmplinapp.R;
import com.indeves.chmplinapp.Utility.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khalid on 26/03/18.
 */

public class StudioTeamMembersAdapter extends RecyclerView.Adapter<StudioTeamMembersAdapter.ViewHolder> {

    private List<StudioTeamMember> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private AddImagesArrayAdapter.ItemClickListener mClickListener;
    private boolean editMode;
    private Context context;

    // data is passed into the constructor
    public StudioTeamMembersAdapter(Context context, ArrayList<StudioTeamMember> data, boolean editMode) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.editMode = editMode;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public StudioTeamMembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.studio_team_member, parent, false);
        return new StudioTeamMembersAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull StudioTeamMembersAdapter.ViewHolder holder, int position) {
        StudioTeamMember member = mData.get(position);
        if (editMode) {
            holder.deleteMember.setVisibility(View.VISIBLE);
            holder.eventsCount.setVisibility(View.GONE);
            holder.eventCountHint.setVisibility(View.GONE);
        } else {
            holder.deleteMember.setVisibility(View.GONE);
            holder.eventsCount.setVisibility(View.VISIBLE);
            holder.eventCountHint.setVisibility(View.VISIBLE);
        }
        holder.name.setText(member.getName());
        String description = member.getCity() + ", " + member.getGender();
        holder.description.setText(description);
        if (member.getEventsIds() != null) {
            holder.eventsCount.setText(String.valueOf(member.getEventsIds().size()));
        }
        Picasso.with(context).load(member.getImageUrl()).transform(new CircleTransform()).resize(90, 90).into(holder.imageView);


    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public StudioTeamMember getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(AddImagesArrayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name, description, eventsCount, eventCountHint;
        Button deleteMember;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.team_member_image);
            name = itemView.findViewById(R.id.team_member_name);
            description = itemView.findViewById(R.id.team_member_description);
            eventsCount = itemView.findViewById(R.id.team_member_events_count);
            eventCountHint = itemView.findViewById(R.id.eventCountHint);
            deleteMember = itemView.findViewById(R.id.team_member_delete);
            deleteMember.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
