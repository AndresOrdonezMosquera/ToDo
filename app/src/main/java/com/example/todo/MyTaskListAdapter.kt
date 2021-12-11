package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MyTaskListAdpter (context : AppCompatActivity, val info: Bundle)
    : RecyclerView.Adapter<MyTaskListAdpter.MyViewHolder>(){
    class MyViewHolder(val layout : View): RecyclerView.ViewHolder(layout)
    private  var context : AppCompatActivity = context
    var  myTaskid : ArrayList<Int> = info.getIntegerArrayList("id") as ArrayList<Int>
    var  myTaskTitles : ArrayList<String> = info.getStringArrayList("titles") as ArrayList<String>
    //var  myTaskDescription_t : ArrayList<String> = info.getStringArrayList("description_t") as ArrayList<String>
    var  myTaskTimes : ArrayList<String> = info.getStringArrayList("times") as ArrayList<String>
    var  myTaskPlaces : ArrayList<String> = info.getStringArrayList("places") as ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var layout = LayoutInflater.from(parent.context).inflate(R.layout.tast_list_items,parent,false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var textViewTask= holder.layout.findViewById<TextView>(R.id.TextViewTask)
        textViewTask.text=myTaskTitles[position]
        var textViewTime= holder.layout.findViewById<TextView>(R.id.textViewTime)
        textViewTime.text=myTaskTimes[position]
        holder.layout.setOnClickListener{
            Toast.makeText(holder.itemView.context,textViewTask.text,Toast.LENGTH_LONG).show()
            val datos = Bundle()
            datos.putString("tarea",textViewTask.text as String)
            //datos.putString("descripcion_t",myTaskDescription_t[position])
            datos.putString("hora",textViewTime.text as String)
            datos.putString("lugar",myTaskPlaces[position])
            datos.putInt("id" ,myTaskid[position])
            context.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragment_container_view,DetailFragment::class.java,datos,"detail")
                ?.addToBackStack("")
                ?.commit()
        }
    }

    override fun getItemCount(): Int {
        return  myTaskTitles.size
    }


}