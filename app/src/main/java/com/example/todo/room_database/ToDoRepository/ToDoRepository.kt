package com.example.todo.room_database.ToDoRepository

import com.example.todo.room_database.ToDo
import com.example.todo.room_database.ToDoDAO

class ToDoRepository (private val toDoDao : ToDoDAO){
    suspend fun getAllTasks():List<ToDo>{
        return toDoDao.getAllTasks()
    }
    suspend fun insertTask(toDo: ToDo): Long{
        return toDoDao.insertTask(toDo)
    }
    suspend fun insertTasks(toDo: List<ToDo>?):List<Long>{
        return toDoDao.inserttasks(toDo)
    }

}