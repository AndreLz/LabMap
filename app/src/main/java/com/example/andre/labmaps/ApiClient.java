package com.example.andre.labmaps;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiClient {
    private static CountryInterface CountryService;

    public static CountryInterface getCountryClient() {
        if (CountryService == null) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl("https://restcountries.eu")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            CountryService = restAdapter.create(CountryInterface.class);
        }

        return CountryService;
    }

    public interface CountryInterface {
        @GET("/rest/v1/all")
        Call<List<Country>> getCountries();
    }
}