package com.raffli.filmsaya

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter( private val filmList: ArrayList<film_data>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Name: TextView
        val Director: TextView
        val Rating: TextView
        val ListItem: CardView

        init {
            Name = itemView.findViewById(R.id.txtName)
            Director = itemView.findViewById(R.id.txtDirector)
            Rating = itemView.findViewById(R.id.txtRating)
            ListItem = itemView.findViewById(R.id.card_view)
        }
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
        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                return true
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