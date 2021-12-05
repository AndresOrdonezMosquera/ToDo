package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_detail, container, false)
        var id = requireArguments().getInt("id")
        var descripcion = requireArguments().getString("descripcion")
        var tarea = requireArguments().getString("tarea")
        var hora = requireArguments().getString("hora")
        var lugar = requireArguments().getString("lugar")
        var textViewTarea : TextView = fragmento.findViewById(R.id.textViewtarea)
        var textViewHora : TextView = fragmento.findViewById(R.id.textViewHora)
        var textViewLugar : TextView = fragmento.findViewById(R.id.textViewLugar)
        val btnEditar : Button = fragmento.findViewById(R.id.btnEdit)
        textViewTarea.text = tarea
        textViewDescripcion.text = descripcion
        textViewHora.text = hora
        textViewLugar.text = lugar
        btnEditar.setOnClickListener{
            val principal = Intent(inflater.context,UpTaskActivity::class.java)
            principal.putExtra("tarea", textViewTarea.text as String)
            principal.putExtra("descripcion", textViewDescripcion.text as String)
            principal.putExtra("hora", textViewHora.text as String)
            principal.putExtra("lugar", textViewLugar.text as String)
            principal.putExtra("id", id.toString())
            startActivity(principal)
        }

        return fragmento
    }
}