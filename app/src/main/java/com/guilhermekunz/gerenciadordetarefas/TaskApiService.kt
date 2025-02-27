package com.guilhermekunz.gerenciadordetarefas

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @POST("tasks")
    suspend fun addTask(@Body task: Task)

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: Task)

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}