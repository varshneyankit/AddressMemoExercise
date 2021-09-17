package com.assignment.addressmemo.pojos;

import com.assignment.addressmemo.api.ApiClient;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class APIError {
    @SerializedName("error")
    String errorMessage;
    @SerializedName("errors")
    Errors possibleErrors;

    public APIError (){}

    public APIError(String errorMessage, Errors possibleErrors){
        this.errorMessage = errorMessage;
        this.possibleErrors = possibleErrors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Errors getPossibleErrors() {
        return possibleErrors;
    }

    public class Errors {
        private List<String> zipcode;

        public Errors(List<String> zipcode) {
            this.zipcode = zipcode;
        }

        public List<String> getZipcode() {
            return zipcode;
        }
    }
}

