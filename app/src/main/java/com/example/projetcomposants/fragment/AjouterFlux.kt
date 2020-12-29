package com.example.projetcomposants.fragment

import android.Manifest
import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.projetcomposants.MainActivity
import com.example.projetcomposants.MyViewModel
import com.example.projetcomposants.R
import com.example.projetcomposants.fragment.TelechargementFragment.Companion.listFlux
import com.google.android.material.textfield.TextInputLayout


class AjouterFlux : DialogFragment() {

    val myViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.activity_popup_ajouter_flux, container, false)

        val textInputLayoutLien =
            view.findViewById<TextInputLayout>(R.id.activity_flux_textInputLayout)
        val btnFlux = view.findViewById<Button>(R.id.activity_flux_boutton)


//        myViewModel.lvLink.observe(activity as LifecycleOwner, Observer {
//        })


        btnFlux.isFocusable = true
        btnFlux.isClickable = true

        btnFlux.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            MainActivity.linkFlux = /*"https://" +*/ textInputLayoutLien.editText?.text.toString()

            textInputLayoutLien.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    // TODO Auto-generated method stub
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // TODO Auto-generated method stub
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    myViewModel.lvLink.value = s.toString()
                }
            })

            textInputLayoutLien.editText?.setText(myViewModel.lvLink.value)


            var flag = true
            for (flux in listFlux) {
                if (MainActivity.linkFlux == flux.link) {
                    flag = false
                    break
                }
            }
            if (flag) {
                donwloadLink(MainActivity.linkFlux)
                dismiss()
            } else
                Toast.makeText(activity, "ce flux est déja dans la liste", Toast.LENGTH_LONG).show()
        }
        
        return  view
    }
    
    /*fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout., container, false)
     
        return v
    }*/

    companion object {
      
        fun newInstance(): AjouterFlux {
            val f = AjouterFlux()

            val args = Bundle()
            f.setArguments(args)
            return f
        }
    }

    fun donwloadLink(url: String) {
        var uri: Uri = Uri.parse(url)
        var request: DownloadManager.Request = DownloadManager.Request(uri)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext")
        request.setTitle("request.xml")
        MainActivity.downloadManager.enqueue(request)


    }
}