package com.example.projekt

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val logoutButton : Button = findViewById(R.id.logoutButton)
        auth = Firebase.auth
        val userId = Firebase.auth.currentUser
        val db = Firebase.firestore
        val userIDTextView: TextView = findViewById(R.id.userIDTextView)
        val userUsernameTextView: TextView = findViewById(R.id.usernameTextView)
        val userEmailTextView: TextView = findViewById(R.id.emailTextView)


        db.collection("users")
            .whereEqualTo("uid", userId!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userIDTextView.text = document.id
                    Log.d("UserInfo", "${document.id} => ${document.data}")
                }
            }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

