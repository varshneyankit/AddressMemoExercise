package com.assignment.addressmemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.addressmemo.adapter.AddressListAdapter;
import com.assignment.addressmemo.pojos.Address;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private final List<Address> addressList = new LinkedList<>();
    private MainViewModel mainViewModel;
    private AddressListAdapter addressListAdapter;
    private View mView;
    private SharedPreferencesConfig preferencesConfig;
    private RelativeLayout progressBarLayout;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        progressBarLayout = mView.findViewById(R.id.progress_bar_root_layout);
        preferencesConfig = new SharedPreferencesConfig(requireActivity().getApplicationContext());
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        RecyclerView recyclerView = mView.findViewById(R.id.dashboard_recycler_view);
        addressListAdapter = new AddressListAdapter(addressList, this::onTaskClick);
        FloatingActionButton dashboardFab = mView.findViewById(R.id.dashboard_fab);
        mainViewModel.getAllAddresses().observe(requireActivity(), addresses -> {
            addressList.clear();
            if (!addresses.isEmpty()) {
                int defaultId = preferencesConfig.readDefaultAddress();
                if (defaultId != 0)
                    addresses.forEach(it -> it.setDefault(it.getId() == defaultId));
                else
                    addresses.forEach(it -> it.setDefault(false));
                addressList.addAll(addresses);
                addressListAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                dashboardFab.setVisibility(View.VISIBLE);
                mView.findViewById(R.id.dashboard_blank).setVisibility(View.GONE);
            } else {
                mView.findViewById(R.id.dashboard_blank).setVisibility(View.VISIBLE);
                dashboardFab.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(addressListAdapter);
        mView.findViewById(R.id.dashboard_fab).setOnClickListener(v2 -> navigateToCreateAddress());
        mView.findViewById(R.id.dashboard_blank_fab).setOnClickListener(v2 -> navigateToCreateAddress());
        mView.findViewById(R.id.dashboard_toolbar_refresh_text).setOnClickListener(v2 -> onRefresh());
        mainViewModel.isApiCalled.observe(requireActivity(), it -> {
            if (it)
                hideProgressRL();
            else
                showProgressRL();
        });
        return mView;
    }

    private void onRefresh() {
        Toast.makeText(getContext(), "Refreshing..", Toast.LENGTH_SHORT).show();
        mainViewModel.getAllAddresses();
    }

    private void onTaskClick(int position, View view) {
        PopupMenu popup = new PopupMenu(this.requireContext(), view);
        popup.inflate(R.menu.menu_item_options);
        if (!addressList.isEmpty()) {
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
        }
        popup.show();
    }

    private void navigateToCreateAddress() {
        NavHostFragment.findNavController(DashboardFragment.this)
                .navigate(R.id.action_DashboardFragment_to_AddressFragment);
    }
    public void showProgressRL() {
        if (progressBarLayout.getVisibility() != View.VISIBLE) {
            progressBarLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressRL() {
        if (progressBarLayout.getVisibility() != View.GONE) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}