package com.example.projetcomposants

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.projetcomposants.BDD.Flux
import com.example.projetcomposants.BDD.Info
import com.example.projetcomposants.BDD.MyDao
import com.example.projetcomposants.BDD.MyDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread


class AfficherActivity : AppCompatActivity() {


    //private val receiver = MyReceiver()

    private lateinit var listFlux: MutableList<Flux>




    val myViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    companion object {
        lateinit var downloadManager: DownloadManager
        lateinit var linkFlux: String
        lateinit var recyclerAdapter: RecyclerAdapter
        var listInfo = mutableListOf<Info>()
        lateinit var recyclerview: RecyclerView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_afficher)


        recyclerview = findViewById(R.id.activity_main_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)


        val bd = Room.databaseBuilder(
            this,
            MyDatabase::class.java, "BDD"
        ).build()


        val dao: MyDao = MyDatabase.getDatabase(this).myDao()
        Thread {

            val filter = intent.getStringExtra("filter")
            if (filter.equals("new")) {
                listInfo = dao.getNewInfo()
                setTitle("Nouveaux flux")
            } else if (filter.equals("all")) {
                setTitle("Tous les flux")
                listInfo = dao.getAllInfo()
            }

            dao.updateNouveau()

            runOnUiThread(Runnable {
                recyclerAdapter = RecyclerAdapter(this, listInfo)
                recyclerview.adapter = recyclerAdapter
            })
        }.start()



        downloadManager =
            this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        /* preparer un filtre pour BroadcastReceiver */


        thread {
            println("${dao.getAllInfo().size}---------->${dao.getallFlux().size}")
            listFlux = dao.getallFlux()
        }
    }

    /*override fun onResume() {
        super.onResume()
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        this.registerReceiver(receiver, filter)

    }

    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(receiver)
    }*/


}