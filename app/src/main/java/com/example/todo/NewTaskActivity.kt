package com.example.todo

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.todo.room_database.ToDo
import com.example.todo.room_database.ToDoDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import android.content.pm.PackageManager
import com.google.android.gms.maps.GoogleMap
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.config.GservicesValue.isInitialized
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.libraries.places.api.Places.isInitialized

class NewTaskActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    EasyPermissions.PermissionCallbacks,OnMapReadyCallback{
    lateinit var editTextTitle:EditText
    lateinit var editTextDescription_t:EditText
    lateinit var editTextTime: EditText
    lateinit var editTextPlace: EditText

    lateinit var map : GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription_t = findViewById(R.id.editTextDescription_t)
        editTextTime = findViewById(R.id.editTextTime)
        editTextPlace = findViewById(R.id.editTextPlaces)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestLocationPermission()
        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(),"api key")
        }


    }
    companion object{
        private const val REQUEST_LOCATION_PERMISION=1
    }
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISION)
    fun requestLocationPermission(){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(this,"permiso airaaly graded", Toast.LENGTH_LONG).show()
        }else{
            EasyPermissions.requestPermissions(this,"Grant Location Permission",
            REQUEST_LOCATION_PERMISION,Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



    fun onSave(view: android.view.View) {
        var title: String = editTextTitle.text.toString()
        var description_t: String = editTextDescription_t.text.toString()
        var time : String = editTextTime.text.toString()
        var place: String = editTextPlace.text.toString()
        val db = ToDoDatabase.getDatabase(this)
        val todoDAO =db.todoDao()
        val dbFirebase = FirebaseFirestore.getInstance()
        val task = ToDo(0, title, description_t, time, place)
        runBlocking {
            launch {
                var result =  todoDAO.insertTask(task)
                if (result!=-1L){
                    dbFirebase.collection("ToDO").document(result.toString()).set(
                        hashMapOf(
                            "title" to title,
                            "description_t" to description_t,
                            "time" to time,
                            "place" to place
                        )
                    )
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    fun onFindPlace(view: android.view.View) {
        val fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields)
            .build(this)
        startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE)
    }


    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this,"Mylocation Buton click", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission()
        }
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationButtonClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==AUTOCOMPLETE_REQUEST_CODE){
            when(resultCode){
                Activity.RESULT_OK->{
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        editTextPlace.setText(place.name)
                        map.addMarker(
                            MarkerOptions()
                                .position(place.latLng)
                                .title(place.name))
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng,15f))
                    }
                }
                AutocompleteActivity.RESULT_ERROR->{
                    Toast.makeText(this,"Mylocation Buton click", Toast.LENGTH_LONG).show()
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}