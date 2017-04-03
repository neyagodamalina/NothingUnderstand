package com.neyagodamalina.nibnim.request;

import com.neyagodamalina.nibnim.json.JSONResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * POST запрос
 */

public interface Translate {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<JSONResponse> getData(@Field("key") String action, @Field("text") String login, @Field("lang") String password);
}
