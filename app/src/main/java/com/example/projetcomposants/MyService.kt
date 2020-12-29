package com.example.projetcomposants

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.example.projetcomposants.BDD.Flux
import com.example.projetcomposants.BDD.Info
import com.example.projetcomposants.BDD.MyDao
import com.example.projetcomposants.BDD.MyDatabase
import com.example.projetcomposants.fragment.TelechargementFragment
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.thread


class MyService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private val handler = Handler()

    lateinit var dao: MyDao


    private fun runOnUiThread(runnable: Runnable) {
        handler.post(runnable)
    }

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        lateinit var fis: FileInputStream
        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            val bd = Room.databaseBuilder(
                applicationContext,
                MyDatabase::class.java, "BDD"
            ).build()
            dao = MyDatabase.getDatabase(applicationContext).myDao()

            parse(fis)
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }


    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread(
            "ServiceStartArguments",
            android.os.Process.THREAD_PRIORITY_BACKGROUND
        ).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "début du téléchargement", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->

            msg.arg1 = startId

            val fis = MyReceiver.fis

            serviceHandler?.fis = fis
            serviceHandler?.sendMessage(msg)

        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "fichier rss téléchargé", Toast.LENGTH_SHORT).show()
    }

    fun parse(fis: FileInputStream) {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(fis)
        doc.documentElement.normalize()
        Log.i("ujbi", "Root element :" + doc.getDocumentElement().getNodeName())
        val channel = doc.getElementsByTagName("channel").item(0) as Element
        val source = channel.getElementsByTagName("title").item(0).textContent
        val tag = channel.getElementsByTagName("description").item(0).textContent

        lateinit var flux : Flux
        Thread {
            flux = Flux(source, tag, MainActivity.linkFlux)
            try {
                dao.insertFlux(flux)

                runOnUiThread(Runnable {
                    TelechargementFragment.recyclerAdapter.getList().add(flux)
                    TelechargementFragment.recyclerAdapter.notifyDataSetChanged()
                })

            } catch (e: SQLiteConstraintException) {

                runOnUiThread(Runnable {
                    Toast.makeText(
                        applicationContext,
                        "cette source est déja connue",
                        Toast.LENGTH_LONG
                    ).show()


                })

            }
        }.start()



        println("source : ${source} tag : ${tag}")

        val itemList = doc.getElementsByTagName("item")




        for (i in 0..itemList.length - 1) {
            val it = itemList.item(i)
            if (it.nodeType == Node.ELEMENT_NODE) {
                val element = it as Element
                val title = element.getElementsByTagName("title").item(0).textContent
                val description = element.getElementsByTagName("description").item(0).textContent
                val link = element.getElementsByTagName("link").item(0).textContent
                val datetime = element.getElementsByTagName("pubDate").item(0).textContent
                Thread {
                    val info = Info(
                        title = title,
                        description = description,
                        link = link,
                        nouveau = true,
                        linkSource = MainActivity.linkFlux,
                        dateTime = datetime
                    )

                    try {
                        dao.insertInfo(
                            info
                        )
                    } catch (e: SQLiteConstraintException) {

                        println("cette info est deja connue")
                    }



                }.start()

            }
        }

        runOnUiThread(Runnable {
            showNotification(flux.source)
        })

        thread {
            println("${dao.getAllInfo().size}---------->${dao.getallFlux().size}")
        }


    }

    fun showNotification(name: String) {

        val intent = Intent(this, AfficherActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        intent.putExtra("filter", "new")

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)


        var builder = NotificationCompat.Builder(this, getString(R.string.channel_name))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Nouveau Dlux!")
            .setContentText(name)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }
}