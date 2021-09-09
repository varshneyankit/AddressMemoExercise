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
    private Address currentAddress;

    public MainViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public MutableLiveData<List<Address>> getAllAddresses() {
        Call<List<Address>> call = apiInterface.getAddressList(API_TOKEN);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    dataList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return dataList;
    }

    public void insertAddress(Address address) {
        apiInterface.createAddress(API_TOKEN, address.getFirstName(), address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getPinCode(), address.getStateId(), address.getCountryId(), address.getPhone())
                .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        if (response.isSuccessful())
                            Toast.makeText(getApplication().getApplicationContext(), "Address was successfully added !", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplication().getApplicationContext(), "Please enter valid zipcode and try again !", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

    public void updateAddress(Address address) {
        apiInterface.updateAddress(address.getId(), API_TOKEN, address.getFirstName(), address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getPinCode())
                .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        Toast.makeText(getApplication().getApplicationContext(), "Address was successfully updated !", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody() + response.message() + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), "Something went wrong !" + t.toString(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

    public void deleteAddress(Address address) {
        apiInterface.deleteAddress(address.getId(), API_TOKEN)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                            Toast.makeText(getApplication().getApplicationContext(), "Address was successfully deleted !", Toast.LENGTH_SHORT).show();
                        else
                            Log.e("TAG", "onResponse: " + response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t.getMessage());
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

    public void setDefaultAddress(Address address) {
        Objects.requireNonNull(dataList.getValue()).forEach(it -> it.setDefault(it.getId() == address.getId()));
    }
}