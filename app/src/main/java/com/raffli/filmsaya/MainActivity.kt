package com.raffli.filmsaya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.dataListener {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    val db = FirebaseDatabase.getInstance("https://filmsaya-256b7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private var filmData = ArrayList<film_data>()
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.listFilm)
        supportActionBar!!.title = "Film Data"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()

        fabAddFilm.setOnClickListener() {
            intent = Intent(applicationContext, InsertFilmActivity::class.java)
            startActivity(intent)
        }

        fabLogout.setOnClickListener() {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
        }
    }
    private fun GetData() {
        Toast.makeText(applicationContext, "Please wait for a while", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        getReference.child("Admin").child(getUserID).child("Film")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(film_data::class.java)
                            item?.key = snapshot.key
                            filmData.add(item!!)
                        }
                        adapter = RecyclerViewAdapter(filmData, this@MainActivity)
                        recyclerView?.adapter = adapter

                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext,"Data successfully loaded", Toast.LENGTH_LONG).show()
                    }
                    else {
                        tv_no_data.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data failed to load", Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
    }

    override fun onDeleteData(filmData: film_data?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        val getKey = filmData?.key.toString()
        if(getReference != null){
            getReference.child("Admin")
                .child(getUserID)
                .child("Film")
                .child(getKey!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
        else {
            Toast.makeText(this@MainActivity, "Reference Kosong", Toast.LENGTH_SHORT).show();
        }
    }
}