package com.guilhermekunz.gerenciadordetarefas.data.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.guilhermekunz.gerenciadordetarefas.data.database.TaskDatabase
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTask_should_insert_task() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        taskDao.insertTask(task)

        val retrievedTask = taskDao.getTaskById(1)
        assertEquals(task, retrievedTask)
    }

    @Test
    fun getAllTasks_should_return_all_tasks_in_descending_order_by_id() = runBlocking {
        val task1 = TaskEntity(1, "Title 1", "Description 1", false, false)
        val task2 = TaskEntity(2, "Title 2", "Description 2", true, false)
        taskDao.insertTask(task1)
        taskDao.insertTask(task2)

        val tasks = taskDao.getAllTasks().first()
        assertEquals(listOf(task2, task1), tasks)
    }

    @Test
    fun deleteTask_should_delete_a_task() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        taskDao.insertTask(task)
        taskDao.deleteTask(task)

        val retrievedTask = taskDao.getTaskById(1)
        assertNull(retrievedTask)
    }

    @Test
    fun update_should_update_a_task() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        taskDao.insertTask(task)

        val updatedTask = task.copy(title = "Updated Title")
        taskDao.update(updatedTask)

        val retrievedTask = taskDao.getTaskById(1)
        assertEquals(updatedTask, retrievedTask)
    }

    @Test
    fun getTaskById_should_return_the_correct_task() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        taskDao.insertTask(task)

        val retrievedTask = taskDao.getTaskById(1)
        assertEquals(task, retrievedTask)
    }

    @Test
    fun getUnsyncedTasks_should_return_unsynced_tasks() = runBlocking {
        val syncedTask = TaskEntity(1, "Synced", "Description", true, true, false)
        val unsyncedTask = TaskEntity(2, "Unsynced", "Description", false, false, false)
        taskDao.insertTask(syncedTask)
        taskDao.insertTask(unsyncedTask)

        val unsyncedTasks = taskDao.getUnsyncedTasks().first()
        assertEquals(listOf(unsyncedTask), unsyncedTasks)
    }

    @Test
    fun getDeletedTasks_should_return_deleted_tasks() = runBlocking {
        val deletedTask = TaskEntity(1, "Deleted", "Description", false, false, true)
        val nonDeletedTask = TaskEntity(2, "Non-Deleted", "Description", false, false, false)
        taskDao.insertTask(deletedTask)
        taskDao.insertTask(nonDeletedTask)

        val deletedTasks = taskDao.getDeletedTasks().first()
        assertEquals(listOf(deletedTask), deletedTasks)
    }
}