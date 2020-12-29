package com.example.projetcomposants

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.projetcomposants.BDD.Info
import com.example.projetcomposants.BDD.MyDatabase
import ru.rambler.libs.swipe_layout.SwipeLayout

class RecyclerAdapter(val context: Context, val maListe: MutableList<Info>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val root: View
        val swipeLayout: SwipeLayout
        val rightView: FrameLayout
        val textViewTitre: TextView
        val textviewDescription: TextView

        init {
            root = view.findViewById(R.id.root)
            swipeLayout = view.findViewById(R.id.swipeLayout)
            textViewTitre = view.findViewById(R.id.txtTitle)
            textviewDescription = view.findViewById(R.id.txtDescription)
            rightView = view.findViewById(R.id.right_view)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return maListe.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitre.setText(maListe[position].title)
        holder.textviewDescription.setText(maListe[position].description)
        holder.root.setOnClickListener {
            val intent = Intent(context, ViewLink::class.java)
            intent.putExtra("link", maListe.get(position).link)
            intent.putExtra("name", maListe.get(position).title)
            context.startActivity(intent)
        }
        holder.rightView.setOnClickListener {
            deleteItem(position)
            holder.swipeLayout.animateReset()

        }
    }

    fun deleteItem(position: Int) {

        val bd = Room.databaseBuilder(
            context,
            MyDatabase::class.java, "BDD"
        ).build()
        val dao = MyDatabase.getDatabase(context).myDao()
        println(maListe.get(position).title)

        Thread {
            println(maListe.get(position).title)

            dao.deleteInfo(maListe.removeAt(position))


        }.start()
        println(maListe.get(position).title)


        notifyItemRemoved(position)
        //notifyDataSetChanged()

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getList(): MutableList<Info> = this.maListe

// hadi marahich tekhdem correctement


//    fun sortItemsByDate() {
//        Collections.sort(this.maListe,
//            Comparator<Info>() { o1, o2 ->
//                if (o1.getDate() == null || o2.getDate() == null) 0 else o1.getDate()
//                    .compareTo(o2.getDate())
//            })
//    }
}