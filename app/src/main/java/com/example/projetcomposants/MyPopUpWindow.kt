package com.example.projetcomposants

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.projetcomposants.BDD.Flux
import com.google.android.material.textfield.TextInputLayout


class MyPopUpWindow(val activity: Activity, val listFlux: MutableList<Flux>) :
    PopupWindow(activity) {

    val myViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(MyViewModel::class.java)
    }

    fun displayPopupWindow() {
        val inflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        val view = inflater.inflate(R.layout.activity_popup_ajouter_flux, null)
        this.contentView = view

        val textInputLayoutLien =
            view.findViewById<TextInputLayout>(R.id.activity_flux_textInputLayout)
        val btnFlux = view.findViewById<Button>(R.id.activity_flux_boutton)


//        myViewModel.lvLink.observe(activity as LifecycleOwner, Observer {
//        })


        btnFlux.isFocusable = true
        btnFlux.isClickable = true

        btnFlux.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            AfficherActivity.linkFlux = /*"https://" +*/ textInputLayoutLien.editText?.text.toString()

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

            myViewModel.lvPopupWindowIsDisplayed.observe(activity as LifecycleOwner, Observer {
                if (it)
                    textInputLayoutLien.editText?.setText(myViewModel.lvLink.value)

            })

            var flag = true
            for (flux in listFlux) {
                if (AfficherActivity.linkFlux == flux.link) {
                    flag = false
                    break
                }
            }
            if (flag) {
                donwloadLink(AfficherActivity.linkFlux)
                dismiss()
            } else
                Toast.makeText(activity, "ce flux est déja dans la liste", Toast.LENGTH_LONG).show()
        }
    }

    fun donwloadLink(url: String) {
        var uri: Uri = Uri.parse(url)
        var request: DownloadManager.Request = DownloadManager.Request(uri)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext")
        request.setTitle("request.xml")
        AfficherActivity.downloadManager.enqueue(request)


    }



}