package com.example.cedro.gerenciamentosenha.interfaceService;


import com.example.cedro.gerenciamentosenha.model.RequestLogin;
import com.example.cedro.gerenciamentosenha.model.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface LoginService {

    public static final String BASE_URL = "https://dev.people.com.ai/mobile/api/v2/";

    //@Headers({"content-type: application/json"})
    @POST("login")
    Call<ResponseLogin> login(@Body RequestLogin requestBody);

}
