package com.raffli.filmsaya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert_film.*
import kotlinx.android.synthetic.main.activity_update_film.*
import kotlinx.android.synthetic.main.view_item.*

class UpdateFilmActivity : AppCompatActivity() {
    private var db: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var nameCheck: String? = null
    private var yearCheck: String? = null
    private var directorCheck: String? = null
    private var ratingCheck: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_film)
        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance("https://filmsaya-256b7-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        data
        update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                nameCheck = nameUpdate.getText().toString()
                yearCheck = yearUpdate.getText().toString()
                directorCheck = directorUpdate.getText().toString()
                ratingCheck = ratingBarUpdate.rating.toString()
                if (isEmpty(nameCheck!!) || isEmpty(yearCheck!!) || isEmpty(ratingCheck!!)) {
                    Toast.makeText(this@UpdateFilmActivity,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setFilm = film_data()

                    setFilm.name = nameUpdate.getText().toString()
                    setFilm.year = yearUpdate.getText().toString()
                    setFilm.director = directorUpdate.getText().toString()
                    setFilm.rating = ratingBarUpdate.rating.toString()
                    updateFilm(setFilm)
                }
            }
        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
        private get() {
            val getName = intent.extras!!.getString("nameData")
            val getYear = intent.extras!!.getString("yearData")
            val getDirector = intent.extras!!.getString("directorData")
            val getRating = intent.extras!!.getString("ratingData")
            nameUpdate!!.setText(getName)
            yearUpdate!!.setText(getYear)
            directorUpdate!!.setText(getDirector)
            if (getRating != null) {
                ratingBarUpdate.rating = getRating.toFloat()
            }
        }

    private fun updateFilm(filmData: film_data) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        db!!.child("Admin")
            .child(userID!!)
            .child("Film")
            .child(getKey!!)
            .setValue(filmData)
            .addOnSuccessListener {
                nameUpdate.setText("")
                yearUpdate.setText("")
                directorUpdate.setText("")
                ratingBarUpdate.rating = 0.0f
                Toast.makeText(this@UpdateFilmActivity, "Data Already Updated", Toast.LENGTH_SHORT).show()
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
    }
}