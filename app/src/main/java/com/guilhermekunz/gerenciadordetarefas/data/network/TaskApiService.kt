package com.guilhermekunz.gerenciadordetarefas.data.network

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApiService {
    @GET("/todos")
    suspend fun getTasks(): List<TaskEntity>

    @POST("/todos")
    suspend fun createTask(@Body task: TaskEntity): Response<TaskEntity>

    @PUT("/todos/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: TaskEntity): Response<TaskEntity>

    @DELETE("/todos/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Unit>
}