package com.example.todo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.room_database.ToDo
import com.example.todo.room_database.ToDoDatabase
import com.example.todo.room_database.ToDoRepository.ToDoRepository
import com.example.todo.room_database.viewmodel.ToDoViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ToDoFragment : Fragment() {

    var myTaskid: ArrayList<Int> = ArrayList()
    var myTaskTitles: ArrayList<String> = ArrayList()
    //var myTaskDescriptions_t: ArrayList<String> = ArrayList()
    var myTaskTimes: ArrayList<String> = ArrayList()
    var myTaskPlaces: ArrayList<String> = ArrayList()
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var myAdapter : RecyclerView.Adapter<MyTaskListAdpter.MyViewHolder>
    var info : Bundle = Bundle()
    private lateinit var toDoViewModel : ToDoViewModel
    private lateinit var toDoRepository: ToDoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_to_do,container,false)
        /*val detail1 : Button = fragmento.findViewById(R.id.btn_detail_1)
        val detail2 : Button = fragmento.findViewById(R.id.btn_detail_2)
        val detail3 : Button = fragmento.findViewById(R.id.btn_detail_3)
        detail1.setOnClickListener(View.OnClickListener{
            val datos =Bundle()
            datos.putString("tarea", resources.getString(R.string.txt_tarea_1))
            datos.putString("hora","12:00")
            datos.putString("lugar","taller")
            activity?.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fragment_container_view,DetailFragment::class.java,datos,"detail")
                ?.addToBackStack("")
                ?.commit()

        })*/
        return fragmento
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*var myTaskTitles : ArrayList<String> = ArrayList()
        myTaskTitles.add(resources.getString(R.string.txt_tarea_1))
        myTaskTitles.add(resources.getString(R.string.txt_tarea_2))
        myTaskTitles.add(resources.getString(R.string.txt_tarea_3))
        var myTaskTimes : ArrayList<String> = ArrayList()
        myTaskTimes.add("9:00")
        myTaskTimes.add("9:00")
        myTaskTimes.add("9:00")
        var myTaskPlaces : ArrayList<String> = ArrayList()
        myTaskPlaces.add("super")
        myTaskPlaces.add("drogeria")
        myTaskPlaces.add("tienda")
        var info : Bundle = Bundle()
        info.putStringArrayList("titles", myTaskTitles)
        info.putStringArrayList("times", myTaskTimes)
        info.putStringArrayList("places", myTaskPlaces)
        listRecyclerView = requireView().findViewById(R.id.recyclerTodoList)
        myAdapter= MyTaskListAdpter(activity as AppCompatActivity,info)
        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter= myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))*/
        val fab : View = requireActivity().findViewById(R.id.fab)
        fab.setOnClickListener{view->
            val intent = Intent(activity, NewTaskActivity::class.java)
            var recursiveScope = 0
            startActivityForResult(intent, recursiveScope)
        }
        //var info : Bundle = Bundle()
        info.putIntegerArrayList("id", myTaskid)
        info.putStringArrayList("titles", myTaskTitles)
        //info.putStringArrayList("descriptions_t", myTaskDescriptions_t)
        info.putStringArrayList("times", myTaskTimes)
        info.putStringArrayList("places", myTaskPlaces)
        listRecyclerView = requireView().findViewById(R.id.recyclerTodoList)
        myAdapter= MyTaskListAdpter(activity as AppCompatActivity,info)
        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter= myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        updateList()
    }



    private fun updateList() {
        val db = ToDoDatabase.getDatabase(requireActivity())
        val dbFirebase = FirebaseFirestore.getInstance()
        val toDoDAO= db.todoDao()
        /*runBlocking {
            launch {
                var result = toDoDAO.getAllTasks()
                var i = 0
                myTaskTitles.clear()
                myTaskTimes.clear()
                myTaskPlaces.clear()
                while (i<result.size){
                    myTaskTitles.add(result[i].title.toString())
                    myTaskTimes.add(result[i].time.toString())
                    myTaskPlaces.add(result[i].place.toString())
                    i++
                }
                myAdapter.notifyDataSetChanged()
            }
        }*/
        toDoRepository = ToDoRepository(toDoDAO)
        toDoViewModel = ToDoViewModel(toDoRepository)
        var result = toDoViewModel.getAllTasks()
        result.invokeOnCompletion {
            var theTasks = toDoViewModel.getTheTasks()
            if (theTasks!!.size!=0) {
                var i = 0
                myTaskTitles.clear()
                //myTaskDescriptions_t.clear()
                myTaskTimes.clear()
                myTaskPlaces.clear()
                myTaskid.clear()
                while (i < theTasks.size) {
                    myTaskTitles.add(theTasks[i].title.toString())
                    //myTaskDescriptions_t.add(theTasks[i].title.toString())
                    myTaskTimes.add(theTasks[i].time.toString())
                    myTaskPlaces.add(theTasks[i].place.toString())
                    myTaskid.add(theTasks[i].id)
                    i++
                }
                myAdapter.notifyDataSetChanged()
            }else {

                var tasks = mutableListOf<ToDo>()
                dbFirebase.collection("ToDo").get().addOnSuccessListener {
                    var docs = it.documents
                    if (docs.size != 0) {
                        var i = 1
                        while (i <docs.size) {
                            myTaskTitles.add(docs[i].get("title") as String)
                            //myTaskDescriptions_t.add(docs[i].get("description_t") as String)
                            myTaskTimes.add(docs[i].get("time") as String)
                            myTaskPlaces.add(docs[i].get("place") as String)
                            tasks.add(ToDo(0, myTaskTitles[i], myTaskTimes[i], myTaskPlaces[i]))
                            i++
                        }
                        toDoViewModel.insertTasks(tasks)
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0){
            if(resultCode==Activity.RESULT_OK){
                updateList()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}