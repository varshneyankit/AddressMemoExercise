package com.assignment.addressmemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.assignment.addressmemo.api.ApiClient;
import com.assignment.addressmemo.api.ApiInterface;
import com.assignment.addressmemo.pojos.Address;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Address>> dataList = new MutableLiveData<>();
    private final ApiInterface apiInterface;
    private final String API_TOKEN = "52e04d83e87e509f07982e6ac851e2d2c67d1d0eabc4fe78";
    private final SharedPreferencesConfig preferencesConfig;
    private final String TAG = "MainViewModel";
    public MutableLiveData<Boolean> canNavigate = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Address currentAddress;

    public MainViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        preferencesConfig = new SharedPreferencesConfig(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Address>> getAllAddresses() {
        Call<List<Address>> call = apiInterface.getAddressList(API_TOKEN);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.body() != null && response.isSuccessful())
                    dataList.setValue(response.body());
                else
                    errorMessage.setValue("Possible API error : " + response.message());
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                errorMessage.setValue("Something went wrong.\nPossible error : " + t.getMessage());
                Log.e(TAG, "onFailure: getAllAddress " + t.getMessage());
            }
        });
        return dataList;
    }

    public void insertAddress(Address address, boolean defaultStatus) {
        apiInterface.createAddress(API_TOKEN, address.getFirstName(), address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getPinCode(), address.getStateId(), address.getCountryId(), address.getPhone())
                .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication().getApplicationContext(), "Address was successfully added !", Toast.LENGTH_SHORT).show();
                            if (response.body() != null)
                                if (defaultStatus)
                                    preferencesConfig.writeDefaultAddress(response.body().getId());
                            canNavigate.setValue(true);
                            getAllAddresses();
                        } else {
                            errorMessage.setValue("Possible API error : " + response.message());
                            canNavigate.setValue(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        errorMessage.setValue("Something went wrong.\nPossible error : " + t.getMessage());
                        Log.e(TAG, "onFailure: insertAddress " + t.getMessage());
                        canNavigate.setValue(false);
                    }
                });
    }

    public void updateAddress(Address address, boolean defaultStatus) {
        apiInterface.updateAddress(address.getId(), API_TOKEN, address.getFirstName(), address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getPinCode())
                .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null)
                                if (defaultStatus)
                                    preferencesConfig.writeDefaultAddress(response.body().getId());
                                else if (preferencesConfig.readDefaultAddress() == address.getId())
                                    preferencesConfig.writeDefaultAddress(0);
                            Toast.makeText(getApplication().getApplicationContext(), "Address Updated Successfully! ", Toast.LENGTH_SHORT).show();
                            canNavigate.setValue(true);
                            getAllAddresses();

                        } else {
                            errorMessage.setValue("Possible API error : " + response.message());
                            canNavigate.setValue(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        errorMessage.setValue("Something went wrong.\nPossible error : " + t.getMessage());
                        Log.e(TAG, "onFailure: updateAddress " + t.getMessage());
                        canNavigate.setValue(false);
                    }
                });
    }

    public void deleteAddress(Address address) {
        apiInterface.deleteAddress(address.getId(), API_TOKEN)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication().getApplicationContext(), "Address was successfully deleted !", Toast.LENGTH_SHORT).show();
                            getAllAddresses();
                        } else {
                            Log.e(TAG, "onDeleteAddress: " + response.message());
                            errorMessage.setValue("Possible API error : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        errorMessage.setValue("Something went wrong.\nPossible error : " + t.getMessage());
                        Log.e(TAG, "onDeleteAddressFailure: " + t.getMessage());
                    }
                });
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddressToNull() {
        currentAddress = null;
    }

    public void OnAddressClick(int position) {
        currentAddress = Objects.requireNonNull(dataList.getValue()).get(position);
    }
}