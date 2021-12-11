package com.example.todo.room_database.AdminProducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.todo.ImagenController
import com.example.todo.R
import kotlinx.android.synthetic.main.item_producto.view.*

class ProductoAdacter (private val mContext: Context,private val listaProductos : List<Producto>):
ArrayAdapter<Producto>(mContext,0,listaProductos){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_producto,parent,false)
        val producto = listaProductos[position]
        layout.textViewNombre.text = producto.nombre
        layout.textViewPrecio.text = "$${producto.precio}"
        //layout.imageViewProducto.setImageResource(producto.imagen)
        val imageUri= ImagenController.getImagenUri(context,producto.idProducto.toLong())
        layout.imageViewProducto.setImageURI(imageUri)
        return layout
    }
}
