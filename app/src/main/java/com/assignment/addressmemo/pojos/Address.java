package com.assignment.addressmemo.pojos;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("id")
    private int id;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    @SerializedName("address1")
    private String address1;
    @SerializedName("address2")
    private String address2;
    @SerializedName("city")
    private String city;
    @SerializedName("zipcode")
    private String pinCode;
    @SerializedName("phone")
    private String phone;
    @SerializedName("state_name")
    private String state;
    @SerializedName("alternative_phone")
    private String alternativePhone;
    @SerializedName("company")
    private String company;
    @SerializedName("state_id")
    private int stateId;
    @SerializedName("country_id")
    private int countryId;

    private boolean isDefault;

    public Address(int id, String firstName, String lastName, String address1, String address2, String city, String pinCode, String phone, String state, String alternativePhone, String company, int stateId, int countryId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.pinCode = pinCode;
        this.phone = phone;
        this.state = state;
        this.alternativePhone = alternativePhone;
        this.company = company;
        this.stateId = stateId;
        this.countryId = countryId;
    }

    public Address(String firstName, String address1, String address2, String city, String pinCode, String state, int stateId, int countryId, String phone) {
        this.firstName = firstName;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.pinCode = pinCode;
        this.state = state;
        this.stateId = stateId;
        this.countryId = countryId;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public String getCompany() {
        return company;
    }

    public int getStateId() {
        return stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder item = new StringBuilder();
        item.append(address1).append(address2).append("\n")
                .append(city).append(", ");
        if (state != null)
            item.append(state).append(",");
        item.append("\n").append(pinCode);
        return item.toString();
    }
}