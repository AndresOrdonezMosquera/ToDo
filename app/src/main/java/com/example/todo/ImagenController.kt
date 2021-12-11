package com.example.todo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File

object ImagenController {
    fun selectPhoneFromGallery(activity: Activity, code:Int){
        val intent = Intent(Intent(Intent.ACTION_PICK))
        intent.type="image/*"
        activity.startActivityForResult(intent,code)
    }
    fun saveImagen(context: Context, id: Long, uri: Uri){
        val file = File(context.filesDir,id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
    }
    fun getImagenUri(context: Context, id: Long): Uri{
        val file = File(context.filesDir,id.toString())
        return if(file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.example.todo/drawable/laceholder")
    }
}