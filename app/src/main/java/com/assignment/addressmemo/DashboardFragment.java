package com.assignment.addressmemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.addressmemo.adapter.AddressListAdpater;
import com.assignment.addressmemo.pojos.Address;

import java.util.LinkedList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private final List<Address> addressList = new LinkedList<>();
    private MainViewModel mainViewModel;
    private AddressListAdpater addressListAdpater;
    private View mView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        RecyclerView recyclerView = mView.findViewById(R.id.dashboard_recycler_view);
        addressListAdpater = new AddressListAdpater(addressList, this::onTaskClick);

        mainViewModel.getAllAddresses().observe(requireActivity(), addresses -> {
            addressList.clear();
            if (!addresses.isEmpty()) {
                addressList.addAll(addresses);
                addressListAdpater.notifyDataSetChanged();
            } else
                mView = inflater.inflate(R.layout.fragment_dashboard_blank, container, false);
        });
        recyclerView.setAdapter(addressListAdpater);
        mView.findViewById(R.id.dashboard_fab).setOnClickListener(v2 -> navigateToCreateAddress());
        return mView;
    }

    private void onTaskClick(int position, View view) {
        PopupMenu popup = new PopupMenu(this.requireContext(), view);
        popup.inflate(R.menu.menu_item_options);
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_option_update) {
                mainViewModel.OnAddressClick(position);
                navigateToCreateAddress();
                return true;
            } else if (itemId == R.id.menu_option_delete) {
                mainViewModel.deleteAddress(addressList.get(position));
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void navigateToCreateAddress() {
        NavHostFragment.findNavController(DashboardFragment.this)
                .navigate(R.id.action_DashboardFragment_to_AddressFragment);
    }
}