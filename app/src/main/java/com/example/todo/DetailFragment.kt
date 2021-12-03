package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

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
        var tarea = requireArguments().getString("tarea")
        var hora = requireArguments().getString("hora")
        var lugar = requireArguments().getString("lugar")
        var textViewTarea : TextView = fragmento.findViewById(R.id.textViewtarea)
        var textViewHora : TextView = fragmento.findViewById(R.id.textViewHora)
        var textViewLugar : TextView = fragmento.findViewById(R.id.textViewLugar)
        textViewTarea.text = tarea
        textViewHora.text = hora
        textViewLugar.text = lugar

        return fragmento
    }
}