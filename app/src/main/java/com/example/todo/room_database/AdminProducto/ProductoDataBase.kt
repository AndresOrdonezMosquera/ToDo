package com.example.todo.room_database.AdminProducto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.room_database.ToDoDatabase

@Database(entities = [Producto::class], version = 1)
abstract class ProductoDataBase : RoomDatabase(){
    abstract fun productos(): ProductosDao

    companion object{
        @Volatile
        private var INSTANCE : ProductoDataBase?=null
        fun getDatabase(context: Context): ProductoDataBase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDataBase::class.java,
                    "Producto_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}