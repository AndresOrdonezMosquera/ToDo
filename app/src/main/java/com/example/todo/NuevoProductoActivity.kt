package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.room_database.AdminProducto.Producto
import com.example.todo.room_database.AdminProducto.ProductoDataBase
import kotlinx.android.synthetic.main.activity_nuevo_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_producto.*

class NuevoProductoActivity : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imagenUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)
        var idProducto: Int?=null
        if(intent.hasExtra("producto")){
            val producto=intent.extras?.getSerializable("producto") as Producto
            editTextNombre.setText(producto.nombre)
            editTextPrecio.setText(producto.precio.toString())
            editTextDetalle.setText(producto.descripcion)
            idProducto= producto.idProducto

            val imagenUri = ImagenController.getImagenUri(this, idProducto.toLong())
            imageView_Perfil.setImageURI(imagenUri)
        }
        val database = ProductoDataBase.getDatabase(this)
        val dbFirebase = FirebaseFirestore.getInstance()
        btn_nuevo_producto.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val precio = editTextPrecio.text.toString().toFloat()
            val desripcion = editTextDetalle.text.toString()
            val producto = Producto(nombre,precio,desripcion,R.drawable.ic_launcher_background)

            if(idProducto!=null){
                CoroutineScope(Dispatchers.IO).launch {
                    producto.idProducto = idProducto
                    database.productos().update(producto)
                    imagenUri?.let {
                        ImagenController.saveImagen(this@NuevoProductoActivity,idProducto.toLong(),it)
                    }
                    dbFirebase.collection("Productos").document(idProducto.toString()).set(
                        hashMapOf(
                            "nombre" to nombre,
                            "precio" to precio,
                            "descripcion" to desripcion
                        )
                    )
                    this@NuevoProductoActivity.finish()
                }

            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = database.productos().insrtAll(producto)
                    imagenUri?.let {
                        ImagenController.saveImagen(this@NuevoProductoActivity,result,it)
                    }
                    dbFirebase.collection("Productos").document(result.toString()).set(
                        hashMapOf(
                            "nombre" to nombre,
                            "precio" to precio,
                            "descripcion" to desripcion
                        )
                    )
                    this@NuevoProductoActivity.finish()
                }
            }
        }
        imagenselect.setOnClickListener{
            ImagenController.selectPhoneFromGallery(this,SELECT_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY && resultCode == Activity.RESULT_OK->{
                imagenUri = data!!.data
                imagenselect.setImageURI(imagenUri)
            }
        }
    }
}