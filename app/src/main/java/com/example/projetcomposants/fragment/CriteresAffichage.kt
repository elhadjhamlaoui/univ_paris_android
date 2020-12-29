package com.example.projetcomposants.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetcomposants.AfficherActivity
import com.example.projetcomposants.BDD.Flux
import com.example.projetcomposants.BDD.MyDao
import com.example.projetcomposants.BDD.MyDatabase
import com.example.projetcomposants.FluxAdapter
import com.example.projetcomposants.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CriteresAffichage : Fragment() {
    private lateinit var btnAll: Button
    private lateinit var btnNew: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_criteres, container, false)
        btnAll = root.findViewById(R.id.but_all)
        btnNew = root.findViewById(R.id.but_new)


        btnAll.setOnClickListener {
            val intent = Intent(requireActivity(), AfficherActivity::class.java)
            intent.putExtra("filter", "all")
            startActivity(intent)
        }

        btnNew.setOnClickListener {
            val intent = Intent(requireActivity(), AfficherActivity::class.java)
            intent.putExtra("filter", "new")
            startActivity(intent)
        }

        return  root;
    }

}