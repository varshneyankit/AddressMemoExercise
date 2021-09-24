package com.assignment.addressmemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private SharedPreferencesConfig preferencesConfig;
    private RelativeLayout progressBarLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton dashboardFab;
    private ConstraintLayout blank_dashboard_layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesConfig = new SharedPreferencesConfig(requireActivity().getApplicationContext());
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        addressListAdapter = new AddressListAdapter(addressList, this::onTaskClick);
        mainViewModel.getAllAddresses().observe(this, addresses -> {
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
                blank_dashboard_layout.setVisibility(View.GONE);
            } else {
                addressListAdapter.notifyDataSetChanged();
                blank_dashboard_layout.setVisibility(View.VISIBLE);
                dashboardFab.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        mainViewModel.isApiCalled.observe(this, it -> {
            if (it)
                hideProgressRL();
            else
                showProgressRL();
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        progressBarLayout = mView.findViewById(R.id.progress_bar_root_layout);
        recyclerView = mView.findViewById(R.id.dashboard_recycler_view);
        dashboardFab = mView.findViewById(R.id.dashboard_fab);
        blank_dashboard_layout = mView.findViewById(R.id.dashboard_blank);
        recyclerView.setAdapter(addressListAdapter);
        dashboardFab.setOnClickListener(v2 -> navigateToCreateAddress());
        mView.findViewById(R.id.dashboard_blank_fab).setOnClickListener(v2 -> navigateToCreateAddress());
        mView.findViewById(R.id.dashboard_toolbar_refresh_text).setOnClickListener(v2 -> onRefresh());
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