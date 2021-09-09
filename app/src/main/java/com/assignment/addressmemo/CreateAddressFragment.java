package com.assignment.addressmemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.assignment.addressmemo.pojos.Address;

import java.util.LinkedList;
import java.util.List;

public class CreateAddressFragment extends Fragment {

    private MainViewModel mainViewModel;
    private Address currentAddress;
    private EditText nameEditText, addressLine1EditText, addressLine2EditText, landmarkEditText, cityEditText, stateEditText, pinCodeEditText;
    private CheckBox defaultCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_address, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        nameEditText = view.findViewById(R.id.create_address_name_edit_text);
        addressLine1EditText = view.findViewById(R.id.create_address_line1_edit_text);
        addressLine2EditText = view.findViewById(R.id.create_address_line2_edit_text);
        landmarkEditText = view.findViewById(R.id.create_address_landmark_edit_text);
        cityEditText = view.findViewById(R.id.create_address_city_edit_text);
        stateEditText = view.findViewById(R.id.create_address_state_edit_text);
        pinCodeEditText = view.findViewById(R.id.create_address_pin_edit_text);
        defaultCheckBox = view.findViewById(R.id.create_address_default_checkbox);
        view.findViewById(R.id.create_address_submit_button).setOnClickListener(v2 -> saveAddress());
        view.findViewById(R.id.create_address_back_image).setOnClickListener(v4 -> {
            mainViewModel.setCurrentAddressToNull();
            navigateToDashboard();
        });
        TextView title = view.findViewById(R.id.create_address_title_text);
        if (mainViewModel.getCurrentAddress() != null) {
            title.setText("Update Address");
            currentAddress = mainViewModel.getCurrentAddress();
            nameEditText.setText(currentAddress.getFirstName());
            addressLine1EditText.setText(currentAddress.getAddress1());
            addressLine2EditText.setText(currentAddress.getAddress2());
            cityEditText.setText(currentAddress.getCity());
            stateEditText.setText(currentAddress.getState());
            pinCodeEditText.setText(currentAddress.getPinCode());
            if (currentAddress.isDefault())
                defaultCheckBox.setChecked(true);
        }
        onBackPressed();
        return view;
    }

    public void onBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainViewModel.setCurrentAddressToNull();
                navigateToDashboard();
            }
        });
    }

    private void saveAddress() {
        String name = nameEditText.getText().toString().trim();
        String addressLine1 = addressLine1EditText.getText().toString().trim();
        String addressLine2 = addressLine2EditText.getText().toString().trim();
        String landmark = landmarkEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String pinCode = pinCodeEditText.getText().toString().trim();
        String[] stringFields = {name, addressLine1, addressLine2, landmark, city, state, pinCode};
        EditText[] editTexts = {nameEditText, addressLine1EditText, addressLine2EditText, landmarkEditText, cityEditText, stateEditText, pinCodeEditText};

        for (int i = 0; i < editTexts.length; i++) {
            if (TextUtils.isEmpty(stringFields[i])) {
                editTexts[i].setError("This field can't be blank");
                return;
            }
        }
        if (currentAddress != null) {
            currentAddress.setFirstName(name);
            currentAddress.setAddress1(addressLine1);
            currentAddress.setAddress2(addressLine2);
            currentAddress.setCity(city);
            currentAddress.setState(state);
            currentAddress.setPinCode(pinCode);
            mainViewModel.updateAddress(currentAddress);
            if (defaultCheckBox.isChecked())
                mainViewModel.setDefaultAddress(currentAddress.getId());
        } else {
            Address address = new Address(name, addressLine1, addressLine2, city, pinCode, state, 1400, 105, "1010101010");
            mainViewModel.insertAddress(address);
            if (defaultCheckBox.isChecked())
                mainViewModel.setDefaultAddress(address.getId());
        }
        mainViewModel.setCurrentAddressToNull();
        navigateToDashboard();
    }

    private void navigateToDashboard() {
        NavHostFragment.findNavController(CreateAddressFragment.this)
                .navigate(R.id.action_AddressFragment_to_DashboardFragment);
    }
}