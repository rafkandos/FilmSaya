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

class InsertFilmActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_film)
        save.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid
                val db = FirebaseDatabase.getInstance("https://filmsaya-256b7-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val nameVal: String = name.getText().toString()
                val yearVal: String = year.getText().toString()
                val directorVal: String = director.getText().toString()
                val ratingVal: String = ratingBar.rating.toString()

                val getReference: DatabaseReference
                getReference = db.reference

                if (isEmpty(nameVal) || isEmpty(yearVal) || isEmpty(directorVal) || isEmpty(ratingVal)) {
                    if (isEmpty(nameVal)) Toast.makeText(this@InsertFilmActivity, "Film Name is required!", Toast.LENGTH_SHORT).show()
                    if (isEmpty(yearVal)) Toast.makeText(this@InsertFilmActivity, "Realease Year is required!", Toast.LENGTH_SHORT).show()
                    if (isEmpty(directorVal)) Toast.makeText(this@InsertFilmActivity, "Director is required!", Toast.LENGTH_SHORT).show()
                    if (isEmpty(ratingVal)) Toast.makeText(this@InsertFilmActivity, "Rating is required!", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("Film").push()
                        .setValue(film_data(nameVal, yearVal, directorVal, ratingVal))
                        .addOnCompleteListener(this) {
                            name.setText("")
                            year.setText("")
                            director.setText("")
                            ratingBar.rating = 0.0f
                            Toast.makeText(this@InsertFilmActivity, "Data Already Saved", Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                }
            }
        }
    }
}