package com.example.projetcomposants

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.projetcomposants.BDD.Flux
import com.example.projetcomposants.BDD.Info
import com.example.projetcomposants.BDD.MyDatabase
import com.example.projetcomposants.fragment.TelechargementFragment
import ru.rambler.libs.swipe_layout.SwipeLayout

class FluxAdapter(
    val context: Context,
    val maListe: MutableList<Flux>,
    val checkedFlux: MutableList<Flux>,
    val uncheckedFlux: MutableList<Flux>

) :
    RecyclerView.Adapter<FluxAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        val maListe: MutableList<Flux>,
        val checkedFlux: MutableList<Flux>

    ) : RecyclerView.ViewHolder(view) {
        val source: TextView
        val tag: TextView
        val checkBox: CheckBox

        init {
            source = view.findViewById(R.id.source)
            tag = view.findViewById(R.id.tag)
            checkBox = view.findViewById(R.id.checkBox)


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flux, parent, false)

        return ViewHolder(view, maListe, checkedFlux)
    }

    override fun getItemCount(): Int {
        return maListe.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.source.setText(maListe[position].source)
        holder.tag.setText(maListe[position].tag)

        if (position != -1) {
            holder.checkBox.isChecked = maListe.get(position).active
            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                TelechargementFragment.btnSave.isClickable = true
                TelechargementFragment.btnSave.setBackgroundColor(Color.BLUE)

                if (!isChecked) {
                    uncheckedFlux
                        .add(maListe.get(position))
                    checkedFlux
                        .remove(maListe.get(position))

                } else {
                    uncheckedFlux
                        .remove(maListe.get(position))
                    checkedFlux
                        .add(maListe.get(position))

                }

            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getList(): MutableList<Flux> = this.maListe


}