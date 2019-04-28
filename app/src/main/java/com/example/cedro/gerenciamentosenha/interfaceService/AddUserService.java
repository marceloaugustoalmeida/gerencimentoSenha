package com.example.cedro.gerenciamentosenha.interfaceService;


import com.example.cedro.gerenciamentosenha.model.RequestAddUser;
import com.example.cedro.gerenciamentosenha.model.ResponseAddUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AddUserService {

    public static final String BASE_URL = "https://dev.people.com.ai/mobile/api/v2/";

    @POST("register")
    Call<ResponseAddUser> addUser(@Body RequestAddUser requestAddUser);

}
