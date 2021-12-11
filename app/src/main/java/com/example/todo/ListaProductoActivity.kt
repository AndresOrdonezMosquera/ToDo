package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.todo.room_database.AdminProducto.Producto
import com.example.todo.room_database.AdminProducto.ProductoAdacter
import com.example.todo.room_database.AdminProducto.ProductoDataBase
import kotlinx.android.synthetic.main.activity_lista_producto.*

class ListaProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_producto)
        /*val producto = Producto("tv",1500000.0F,"nuevo",R.drawable.ic_baseline_tv_24)
        val producto2 = Producto("camara",800000.0F,"nuevo",R.drawable.ic_baseline_camera_alt_24)
        val listaProducto = listOf(producto,producto2)
        val adacter = ProductoAdacter(this, listaProducto)
        lista.adapter = adacter*/
        var listaProductos = emptyList<Producto>()
        val database = ProductoDataBase.getDatabase(this)
        database.productos().getAll().observe(
            this, Observer { listaProductos= it
            val adacter = ProductoAdacter(this, listaProductos)
            lista.adapter= adacter}
        )



        lista.setOnItemClickListener(){paret,view,position,id->
            val intent = Intent(this,ProductoActivity::class.java)
            //intent.putExtra("producto", listaProductos[position])
            intent.putExtra("id", listaProductos[position].idProducto)
            startActivity(intent)
        }
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, NuevoProductoActivity::class.java)
            startActivity(intent)
        }
    }
}