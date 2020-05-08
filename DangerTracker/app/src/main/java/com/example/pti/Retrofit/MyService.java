package com.example.pti.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("user") String user,
                                    @Field("email") String email,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("user") String user,
                                 @Field("password") String name);

    @POST("getContactos")
    @FormUrlEncoded
    Observable<String> getContactos(@Field("user") String user);

    @POST("addContacto")
    @FormUrlEncoded
    Observable<String> addContacto(@Field("user") String user,
                                   @Field("telf") String telf);
}