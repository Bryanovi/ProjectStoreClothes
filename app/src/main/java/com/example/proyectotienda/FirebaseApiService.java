package com.example.proyectotienda;

import com.example.proyectotienda.domain.ClothDomain;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.List;
import java.util.Map;

public interface FirebaseApiService {
    @POST("clothList.json")
    Call<Void> saveClothList(@Body List<ClothDomain> clothList);
    @GET("clothList.json")
    Call<Map<String, ClothDomain>> getClothList();
}

