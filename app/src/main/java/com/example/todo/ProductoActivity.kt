package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import com.example.todo.room_database.AdminProducto.Producto
import com.example.todo.room_database.AdminProducto.ProductoDataBase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoActivity : AppCompatActivity() {
    private lateinit var dataBase: ProductoDataBase
    private lateinit var producto: Producto
    private lateinit var productoLiveData: LiveData<Producto>
    val dbFirebase = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
        dataBase = ProductoDataBase.getDatabase(this)
        val idProducto = intent.getIntExtra("id",0)

        val imagenUri = ImagenController.getImagenUri(this, idProducto.toLong())
        imageView_Perfil.setImageURI(imagenUri)

        productoLiveData=dataBase.productos().get(idProducto)
        productoLiveData.observe(this,{
            producto =it
            textView_Nombre.text = producto.nombre
            textView_Precio.text = "$${producto.precio}"
            textView_Detalle.text = producto.descripcion
            imageView_Perfil.setImageResource(producto.imagen)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.producto_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.edit_item->{
                val intent = Intent(this, NuevoProductoActivity::class.java)
                intent.putExtra("producto", producto)
                startActivity(intent)
            }
            R.id.delete_item->{
                productoLiveData.removeObservers(this)
                CoroutineScope(Dispatchers.IO).launch {
                    dataBase.productos().delete(producto)
                    dbFirebase.collection("Productos").document(producto.idProducto.toString()).delete()
                    this@ProductoActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}