package com.assignment.addressmemo;

import android.util.Log;

import com.assignment.addressmemo.api.ApiClient;
import com.assignment.addressmemo.pojos.APIError;
import com.assignment.addressmemo.pojos.Address;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                ApiClient.getApiClient()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            assert response.errorBody() != null;
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            Log.e("ErrorUtils", "parseError: "+e.getMessage() );
            error = new APIError();
        }
        return error;
    }
}
