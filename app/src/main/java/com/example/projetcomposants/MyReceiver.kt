package com.example.projetcomposants

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.ParcelFileDescriptor
import android.widget.Toast
import java.io.FileDescriptor
import java.io.FileInputStream

class MyReceiver : BroadcastReceiver() {
    companion object{
        lateinit var fis:FileInputStream
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        /* trouver la reference detéléchargement */
        val reference: Long? = intent?.getLongExtra(
            DownloadManager.EXTRA_DOWNLOAD_ID, -1
        )

        /* vérifier si la référence se trouve sur la liste de nos téléchargement */
        if (reference == null)
            return

        //récupérer ParcelFileDescriptor de fichier téléchargé
        try {


            val parcelFileDescriptor: ParcelFileDescriptor =
                MainActivity.downloadManager.openDownloadedFile(reference)
            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
            fis = FileInputStream(fileDescriptor)


            Intent(context, MyService::class.java).also { intent ->
                context?.startService(intent)
            }
        }
        catch (e :Exception){
            Toast.makeText(context,"lien non valide !",Toast.LENGTH_LONG).show()
        }
    }



}