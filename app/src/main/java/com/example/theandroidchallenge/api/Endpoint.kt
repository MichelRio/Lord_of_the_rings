package com.example.theandroidchallenge.api

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface Endpoint {

    @GET("book")
    fun getBooksResponse(
    ): Call<ResponseBody>

    @GET("movie")
    fun getMoviesResponse(
        @Header("Authorization") auth: String
    ): Call<ResponseBody>

    @GET("character?limit=5")
    fun getCharacters(
        @Header("Authorization") auth: String
    ): Call<ResponseBody>

    @GET("character/{id}")
    fun getCharacterDetails(
        @Header("Authorization") auth: String,
        @Path("id") id:String
    ): Call<ResponseBody>

    @GET("character/{id}/quote")
    fun getCharactersQuotes(
        @Header("Authorization") auth: String,
        @Path("id") id:String
    ): Call<ResponseBody>

}