package com.indeves.chmplinapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.API.ReadData;
import com.indeves.chmplinapp.Models.LookUpModel;
import com.indeves.chmplinapp.Models.MyPackageData;
import com.indeves.chmplinapp.Models.PackageModel;
import com.indeves.chmplinapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProProfPackageAdaptor extends RecyclerView.Adapter<ProProfPackageAdaptor.MyViewHolder> {
    private List<PackageModel> list;
    private List<LookUpModel> eventTypesList, eventTimesList;

    public ProProfPackageAdaptor(List<PackageModel> list) {
        this.list = list;
        eventTimesList = new ArrayList<>();
        eventTypesList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pro_profile_tab_packages_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PackageModel pData = list.get(position);
        holder.packageName.setText(pData.getPackageTitle());
        holder.packagePrice.setText(String.valueOf(pData.getPrice()));
        holder.packageDescription.setText(pData.getPackageDescription());
        ReadData readData = new ReadData();
        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
                eventTypesList.addAll(eventTypeLookups);
                if (eventTypesList != null) {
                    for (LookUpModel eventTypeElement : eventTypesList) {
                        if (pData.getEventTypeId() == eventTypeElement.getId()) {
                            holder.packageEventType.setText(eventTypeElement.getEnglishName());

                        }
                    }

                }
            }
        });
        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
            @Override
            public void onLookUpsResponse(List<LookUpModel> lookups) {
                eventTimesList.addAll(lookups);
                if (eventTimesList != null) {
                    for (LookUpModel eventTimesElement : eventTimesList) {
                        if (eventTimesElement.getId() == pData.getEventTimeId()) {
                            holder.packageTime.setText(eventTimesElement.getEnglishName());

                        }
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

//    public void loadLookups() {
//        ReadData readData = new ReadData();
//        readData.getLookupsByType("eventTypesLookups", new ReadData.LookUpsListener() {
//            @Override
//            public void onLookUpsResponse(List<LookUpModel> eventTypeLookups) {
//                Log.v("EventTypeLookupsArr", eventTypeLookups.toString());
//                eventTypesList.addAll(eventTypeLookups);
//            }
//        });
//        readData.getLookupsByType("eventTimesLookups", new ReadData.LookUpsListener() {
//            @Override
//            public void onLookUpsResponse(List<LookUpModel> lookups) {
//                eventTimesList.addAll(lookups);
//            }
//        });
//
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView packageName, packageTime, packageDescription, packagePrice, packageCurrency, packageEventType;

        public MyViewHolder(View view) {
            super(view);
            packageName = view.findViewById(R.id.proProfile_myPackage_packageName);
            packageTime = view.findViewById(R.id.proProfile_myPackage_eventTime);
            packageDescription = view.findViewById(R.id.proProfile_myPackage_desc);
            packageCurrency = view.findViewById(R.id.proProfile_myPackage_currency);
            packageEventType = view.findViewById(R.id.proProfile_myPackage_eventType);
            packagePrice = view.findViewById(R.id.proProfile_myPackage_price);


        }
    }
}
