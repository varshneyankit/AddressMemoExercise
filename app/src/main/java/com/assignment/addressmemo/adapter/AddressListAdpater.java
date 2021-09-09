package com.assignment.addressmemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.addressmemo.R;
import com.assignment.addressmemo.interfaces.OnAddressClickListener;
import com.assignment.addressmemo.pojos.Address;

import java.util.List;

public class AddressListAdpater extends RecyclerView.Adapter<AddressListAdpater.ViewHolder> {
    private final List<Address> addressList;
    private final OnAddressClickListener addressClickListener;

    public AddressListAdpater(List<Address> itemsList, OnAddressClickListener addressClickListener) {
        this.addressList = itemsList;
        this.addressClickListener = addressClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = addressList.get(position);
        StringBuilder item = new StringBuilder();
        item.append(address.getAddress1()).append(address.getAddress2()).append("\n")
                .append(address.getCity()).append(", ").append(address.getState()).append(",")
                .append("\n").append(address.getPinCode());

        holder.addressTitle.setText(item);
        if (address.isDefault())
            holder.addressDefaultStatus.setVisibility(View.VISIBLE);
        else
            holder.addressDefaultStatus.setVisibility(View.GONE);
        holder.addressOptionMenu.setOnClickListener(view -> addressClickListener.onTaskClick(position, holder.addressOptionMenu));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressTitle;
        ImageView addressOptionMenu, addressDefaultStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTitle = itemView.findViewById(R.id.item_title);
            addressOptionMenu = itemView.findViewById(R.id.item_option_menu);
            addressDefaultStatus = itemView.findViewById(R.id.item_default_status);
        }
    }
}