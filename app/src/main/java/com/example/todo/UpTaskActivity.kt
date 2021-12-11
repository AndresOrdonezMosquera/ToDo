package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.todo.room_database.ToDo
import com.example.todo.room_database.ToDoDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpTaskActivity : AppCompatActivity() {
    lateinit var UpTextTitle : EditText
    lateinit var UpTextDescription_t : EditText
    lateinit var UpTextTime : EditText
    lateinit var UpTextPlace : EditText
    lateinit var UpTextId : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_task)
        UpTextTitle = findViewById(R.id.uptextTitle)
        UpTextDescription_t = findViewById(R.id.uptextDescription_t)
        UpTextTime = findViewById(R.id.uptextTime)
        UpTextPlace = findViewById(R.id.uptextPlace)
        UpTextId = findViewById(R.id.textViewId)
        UpTextTitle.setText(this.intent.getStringExtra("tarea"))
        UpTextDescription_t.setText(this.intent.getStringExtra("descripcion_t"))
        UpTextTime.setText(this.intent.getStringExtra("hora"))
        UpTextPlace.setText(this.intent.getStringExtra("lugar"))
        UpTextId.setText(this.intent.getStringExtra("id"))
    }

    fun onUp(view: View) {
        var title : String = UpTextTitle.text.toString()
        var description_t : String = UpTextDescription_t.text.toString()
        var time : String = UpTextTime.text.toString()
        var place : String = UpTextPlace.text.toString()
        var id : String = UpTextId.text.toString()
        val db = ToDoDatabase.getDatabase(this)
        val toDoDAO = db.todoDao()
        val dbFirebase = FirebaseFirestore.getInstance()
        val task = ToDo(id.toInt(), title, description_t, time, place)
        runBlocking {
            launch {
                var result = toDoDAO.updateTask(task)
                dbFirebase.collection("ToDo").document(id).set(
                    hashMapOf("title" to title,
                              "description" to description_t,
                              "time" to time,
                              "place" to place)
                )
                setResult(RESULT_OK)
                finish()
            }
        }
        val principal = Intent(this, MainActivity::class.java)
        startActivity(principal)
    }

    fun onDelete(view: View) {
        var title : String = UpTextTitle.text.toString()
        var description_t : String = UpTextDescription_t.text.toString()
        var time : String = UpTextTime.text.toString()
        var place : String = UpTextPlace.text.toString()
        var id : String = UpTextId.text.toString()
        val db = ToDoDatabase.getDatabase(this)
        val toDoDAO = db.todoDao()
        val dbFirebase = FirebaseFirestore.getInstance()
        val task = ToDo(
            id.toInt(),
            title,
            description_t,
            time,
            place
        )
        runBlocking {
            launch {
                var result = toDoDAO.deleteTask(task)
                dbFirebase.collection("ToDo").document(id).delete()
                finish()
            }
        }
        val principal = Intent(this, MainActivity::class.java)
        startActivity(principal)
    }
}