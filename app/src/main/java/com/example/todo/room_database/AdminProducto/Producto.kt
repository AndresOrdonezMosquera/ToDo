package com.example.todo.room_database.AdminProducto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "productos")
class Producto (
    val nombre: String,
    val precio: Float,
    val descripcion: String,
    val imagen: Int,
    @PrimaryKey(autoGenerate = true)
    var idProducto: Int = 0
        ) : Serializable