package com.example.clientesappf

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //EMPRESA
    private const val BASE_URL = "http://192.168.3.205:5000"
    //Casa
    //private const val BASE_URL = "http://192.168.1.5:5000"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
