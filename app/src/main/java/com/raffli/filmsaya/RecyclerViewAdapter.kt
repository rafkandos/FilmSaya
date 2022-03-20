package com.raffli.filmsaya

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdapter(private var filmList: ArrayList<film_data>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var context: Context
    private var auth: FirebaseAuth? = null
    val db = FirebaseDatabase.getInstance("https://filmsaya-256b7-default-rtdb.asia-southeast1.firebasedatabase.app/")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Name: TextView
        val Director: TextView
        val Rating: TextView
        val ListItem: CardView
        val DeleteFilm: ImageButton

        init {
            Name = itemView.findViewById(R.id.txtName)
            Director = itemView.findViewById(R.id.txtDirector)
            Rating = itemView.findViewById(R.id.txtRating)
            ListItem = itemView.findViewById(R.id.card_view)
            DeleteFilm = itemView.findViewById(R.id.btnDeleteCard)
        }
    }

    interface dataListener {
        fun onDeleteData(filmData: film_data?, position: Int)
    }

    var listener: dataListener? = null

    fun RecyclerViewAdapter(filmList: ArrayList<film_data>?, context: Context?) {
        this.filmList = filmList!!
        this.context = context!!
        listener = context as MainActivity?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.view_item, parent, false
        )
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name: String? = filmList.get(position).name
        val year: String? = filmList.get(position).year
        val director: String? = filmList.get(position).director
        val rating: String? = filmList.get(position).rating
        holder.Name.text = "$name ($year)"
        holder.Director.text = "$director"
        holder.Rating.text = "Rating: $rating"
        holder.ListItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val bundle = Bundle()
                bundle.putString("nameData", filmList[position].name)
                bundle.putString("yearData", filmList[position].year)
                bundle.putString("directorData", filmList[position].director)
                bundle.putString("ratingData", filmList[position].rating)
                bundle.putString("getPrimaryKey", filmList[position].key)
                val intent = Intent(context, UpdateFilmActivity::class.java)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        })
        holder.DeleteFilm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                auth = FirebaseAuth.getInstance()
                val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                val getReference = db.getReference()
                val getKey = filmList[position].key.toString()
                if(getReference != null){
                    getReference.child("Admin")
                        .child(getUserID)
                        .child("Film")
                        .child(getKey!!)
                        .removeValue()
                        .addOnSuccessListener {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                } else {
                    Toast.makeText(context, "Reference kosong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    init {
        this.context = context
    }
}