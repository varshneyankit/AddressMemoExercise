package com.assignment.addressmemo.api;


import com.assignment.addressmemo.pojos.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("addresses")
    Call<List<Address>> getAddressList(@Query("token") String token);
/*
&address[firstname]=qwerty&address[address1]=ABC street&address[city]=Delhi&address[country_id]=105&address[state_id]=1400&address[zipcode]=111111&address[phone]=1111111111
*/
/*
    @FormUrlEncoded
    @POST("addresses")
    Call<Address> createAddress(@Query("token") String token,
                                @Field("address[firstname]") String name,
                                @Field("address[address1]") String address1,
                                @Field("address[address2]") String address2,
                                @Field("address[city]") String city,
                                @Field("address[state_name]") String state_name,
                                @Field("address[zipcode]") String pinCode,
                                @Field("address[state_id]") int state_id,
                                @Field("address[country_id]") int country_id,
                                @Field("address[phone]") String phone);*/

    @POST("addresses")
    Call<Address> createAddress(@Query("token") String token,
                                @Query("address[firstname]") String name,
                                @Query("address[address1]") String address1,
                                @Query("address[address2]") String address2,
                                @Query("address[city]") String city,
                                @Query("address[state_name]") String state_name,
                                @Query("address[zipcode]") String pinCode,
                                @Query("address[state_id]") int state_id,
                                @Query("address[country_id]") int country_id,
                                @Query("address[phone]") String phone);

    @FormUrlEncoded
    @PUT("addresses/13")
    Call<Address> updateAddress(@Query("token") String token,
                                @Field("address[firstname]") String name,
                                @Field("address[address1]") String address1,
                                @Field("address[address1]") String address2,
                                @Field("address[city]") String city,
                                @Field("address[state_name]") String state_name,
                                @Field("address[zipcode]") String pinCode);

    @FormUrlEncoded
    @DELETE("addresses/14")
    Call<Address> deleteAddress(@Query("token") String token,
                                @Field("address[id]") int id);
}