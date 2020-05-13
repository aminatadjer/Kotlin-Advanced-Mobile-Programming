package com.example.restapp


import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("todos")
    fun getAllTodos(): Call<List<Todo>>


    @POST("todos")
    fun createTodo(@Body todo: Todo): Call<Todo>

    @GET("todos?completed=true")
    fun getCompleted(): Call<List<Todo>>

    @GET("todos?completed=false")
    fun getNonCompleted(): Call<List<Todo>>


    @DELETE("todos/{id}")
    fun delete(@Path("id") id: String): Call<Todo>

    @PUT("todos/{id}")
    fun update(@Path("id") id: String,@Body todo: Todo): Call<Todo>

}