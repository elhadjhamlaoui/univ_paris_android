package com.example.projetcomposants.fragment

import android.app.DownloadManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetcomposants.BDD.Flux
import com.example.projetcomposants.BDD.MyDao
import com.example.projetcomposants.BDD.MyDatabase
import com.example.projetcomposants.FluxAdapter
import com.example.projetcomposants.MyPopUpWindow
import com.example.projetcomposants.MyViewModel
import com.example.projetcomposants.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TelechargementFragment : Fragment() {


    lateinit var dao: MyDao
    lateinit var myPopUpWindow: MyPopUpWindow
    private lateinit var btnAdd: FloatingActionButton

    val myViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    companion object {
        lateinit var downloadManager: DownloadManager
        var checkedFlux : MutableList<Flux> =  mutableListOf<Flux>()
        var listFlux : MutableList<Flux> =  mutableListOf<Flux>()
        var uncheckedFlux : MutableList<Flux> =  mutableListOf<Flux>()
        lateinit var recyclerAdapter : FluxAdapter
        lateinit var recyclerview : RecyclerView
        lateinit var btnSave : Button
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_telechargement, container, false)

        dao = MyDatabase.getDatabase(requireActivity()).myDao()

        btnAdd = root.findViewById(R.id.activity_main_add_button)

        recyclerview = root.findViewById(R.id.recyclerView)
        btnSave = root.findViewById(R.id.save)
        btnSave.isClickable = false
        btnSave.setBackgroundColor(Color.GRAY)

        recyclerview.layoutManager = LinearLayoutManager(requireActivity())


        btnSave.setOnClickListener {
            btnSave.setBackgroundColor(Color.GRAY)
            btnSave.isClickable = false
            Thread {
                for (flux in checkedFlux) {
                    dao.switchActivateInfo(flux.link, true)
                    dao.disactivateFlux(flux.link, true)
                }
                for (flux in uncheckedFlux) {
                    dao.switchActivateInfo(flux.link)
                    dao.disactivateFlux(flux.link)
                }
            }.start()


        }

        Thread {
            listFlux = dao.getallFlux()

            checkedFlux.clear()
            uncheckedFlux.clear()
            for (flux in listFlux) {
                if (flux.active) {
                    checkedFlux.add(flux)
                } else
                    uncheckedFlux.add(flux)
            }

            requireActivity().runOnUiThread(Runnable {
                recyclerAdapter = FluxAdapter(
                    requireActivity(),
                    listFlux,
                    checkedFlux,
                    uncheckedFlux
                )
                recyclerview.adapter = recyclerAdapter
            })
        }.start()




        /*myViewModel.lvPopupWindowIsDisplayed.observe((requireActivity()), Observer {
            if (it)
                createPopUpWindow()

        })*/


        /* enregistrer BroadcastReceiver */
        btnAdd.setOnClickListener {

            showDialog()
        }

        return  root;
    }


    fun showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val prev = requireActivity().supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment: DialogFragment = AjouterFlux.newInstance()
        newFragment.show(ft, "dialog")
    }

   /* fun createPopUpWindow() {
        myPopUpWindow = MyPopUpWindow(requireActivity(), listFlux)
        myPopUpWindow.displayPopupWindow()

        myPopUpWindow.showAtLocation(btnAdd, Gravity.CENTER, 0, 0)
        myPopUpWindow.dimBehind()

    }*/

    fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.6f
        wm.updateViewLayout(container, p)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        myViewModel.lvPopupWindowIsDisplayed.value = myPopUpWindow.isShowing
        myPopUpWindow.dismiss()
    }

}