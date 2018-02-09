package com.indeves.chmplinapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indeves.chmplinapp.Controllers.MyPackageData;
import com.indeves.chmplinapp.Controllers.PhotographerData;
import com.indeves.chmplinapp.R;

import java.util.List;

public class ProProfPackageAdaptor extends RecyclerView.Adapter<ProProfPackageAdaptor.MyViewHolder> {
    List<MyPackageData> list;

    public ProProfPackageAdaptor(List<MyPackageData> list) {
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       public TextView packageName, packageType,packageCondition,packagePrice,packageCurrency,packageSpecs;

        public MyViewHolder(View view) {
            super(view);
            packageName = view.findViewById(R.id.proProfile_myPackage_packageName);
            packageType = view.findViewById(R.id.proProfile_myPackage_type);
            packageCondition = view.findViewById(R.id.proProfile_myPackage_condition);
            packageCurrency = view.findViewById(R.id.proProfile_myPackage_currency);
            packageSpecs = view.findViewById(R.id.proProfile_myPackage_specs);
            packagePrice = view.findViewById(R.id.proProfile_myPackage_price);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pro_profile_tab_packages_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyPackageData pData = list.get(position);
        holder.packageName.setText(pData.getPackageName());
        holder.packageType.setText(pData.getPackageType());
        holder.packageCondition.setText(pData.getPackageCondition());
        holder.packageSpecs.setText(pData.getPackageSpecs());
        holder.packageCurrency.setText(pData.getPackageCurrency());
        holder.packagePrice.setText(pData.getPackagePrice());
    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}
