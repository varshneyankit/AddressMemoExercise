package com.assignment.addressmemo;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.assignment.addressmemo.pojos.Address;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAddressFragment extends Fragment {

    private MainViewModel mainViewModel;
    private Address currentAddress;
    private EditText nameEditText, addressLine1EditText, addressLine2EditText, landmarkEditText, cityEditText, stateEditText, pinCodeEditText;
    private TextInputLayout nameTil, addressLine1Til, addressLine2Til, cityTil, pinCodeTil;
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
        nameTil = view.findViewById(R.id.create_address_name_til);
        addressLine1EditText = view.findViewById(R.id.create_address_line1_edit_text);
        addressLine1Til = view.findViewById(R.id.create_address_line1_til);
        addressLine2EditText = view.findViewById(R.id.create_address_line2_edit_text);
        addressLine2Til = view.findViewById(R.id.create_address_line2_til);
        landmarkEditText = view.findViewById(R.id.create_address_landmark_edit_text);
        cityEditText = view.findViewById(R.id.create_address_city_edit_text);
        cityTil = view.findViewById(R.id.create_address_city_til);
        stateEditText = view.findViewById(R.id.create_address_state_edit_text);
        pinCodeEditText = view.findViewById(R.id.create_address_pin_edit_text);
        pinCodeTil = view.findViewById(R.id.create_address_pin_til);
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
        String[] stringFields = {name, addressLine1, addressLine2, city, pinCode};
        TextInputLayout[] textInputLayouts = {nameTil, addressLine1Til, addressLine2Til, cityTil, pinCodeTil};

        for (int i = 0; i < stringFields.length; i++) {
            if (TextUtils.isEmpty(stringFields[i])) {
                switch (i){
                    case 0:textInputLayouts[0].setError("Enter your full name");
                    break;
                    case 1:textInputLayouts[1].setError("Enter your Address Line 1");
                    break;
                    case 2:textInputLayouts[2].setError("Enter your Address Line 2");
                    break;
                    case 3:textInputLayouts[3].setError("Enter your City");
                    break;
                    case 4:textInputLayouts[4].setError("Enter your Pin Code");
                    break;
                }
                return;
            }else {
                switch (i){
                    case 0:textInputLayouts[0].setErrorEnabled(false);
                        break;
                    case 1:textInputLayouts[1].setErrorEnabled(false);
                        break;
                    case 2:textInputLayouts[2].setErrorEnabled(false);
                        break;
                    case 3:textInputLayouts[3].setErrorEnabled(false);
                        break;
                    case 4:textInputLayouts[4].setErrorEnabled(false);
                        break;
                }
            }
        }
        if (currentAddress != null) {
            currentAddress.setFirstName(name);
            currentAddress.setAddress1(addressLine1);
            currentAddress.setAddress2(addressLine2);
            currentAddress.setCity(city);
            currentAddress.setState(state);
            currentAddress.setPinCode(pinCode);
            mainViewModel.updateAddress(currentAddress, defaultCheckBox.isChecked());
        } else {
            Address address = new Address(name, addressLine1, addressLine2, city, pinCode, state, 1400, 105, "1010101010");
            mainViewModel.insertAddress(address, defaultCheckBox.isChecked());
        }
        mainViewModel.setCurrentAddressToNull();
        new Handler().postDelayed(() -> {
                    if (mainViewModel.canNavigate.getValue() != null && mainViewModel.canNavigate.getValue())
                        navigateToDashboard();
                }
                , 2000);
    }

    private void navigateToDashboard() {
        NavHostFragment.findNavController(CreateAddressFragment.this)
                .navigate(R.id.action_AddressFragment_to_DashboardFragment);
    }
}