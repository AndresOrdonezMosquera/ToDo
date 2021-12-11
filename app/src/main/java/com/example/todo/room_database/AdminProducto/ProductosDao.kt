package com.example.todo.room_database.AdminProducto

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductosDao {
    @Query("SELECT * FROM productos")
    fun getAll(): LiveData<List<Producto>>
    @Query("SELECT * FROM productos WHERE idProducto = :id")
    fun get(id: Int): LiveData<Producto>
    @Insert
    fun insrtAll(productos: Producto):Long
    @Update
    fun update(producto: Producto)
    @Delete
    fun delete(producto: Producto)
}