package com.example.clientesappf

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/api/person")
    fun getAllPersons(): Call<List<Person>>

    @GET("/api/person/{id}")
    fun getPersonById(@Path("id") id: Int): Call<List<Person>>

    @POST("/api/create/person")
    fun createPerson(@Body person: Person): Call<Person>

    @PUT("/api/person/update/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Void>

    @DELETE("/api/person/delete/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Void>
}

