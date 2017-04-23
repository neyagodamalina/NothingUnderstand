package com.neyagodamalina.nibnim.json;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Класс ответа от Яндекс.Переводчик.
 */
public class JSONResponse{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("text")
    @Expose
    private List<String> text = null;


    @SerializedName("message")
    @Expose
    private String message;


    public List<String> getText() {
        return text;
    }


    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}