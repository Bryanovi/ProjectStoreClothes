package com.example.proyectotienda;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static final String BASE_URL = "https://tiendaropa-7839b-default-rtdb.firebaseio.com/";
    private static FirebaseApiService apiService;

    public static FirebaseApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(FirebaseApiService.class);
        }
        return apiService;
    }
}
