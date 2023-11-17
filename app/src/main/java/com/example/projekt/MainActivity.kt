package com.example.projekt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username: EditText = findViewById(R.id.Username)
        val userEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val userPassword: EditText = findViewById(R.id.editTextTextPassword)
        val loginSwitch: TextView = findViewById(R.id.switchToLogin)
        val register: Button = findViewById(R.id.registerButton)

        val db = Firebase.firestore
        auth = Firebase.auth
        val currentUser = auth.currentUser

        loginSwitch.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        register.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                userEmail.text.toString(),
                userPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Created user")

                        Log.w("TAG", "Failed to create user", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Hello:" + currentUser!!.email.toString(),
                            Toast.LENGTH_SHORT,
                        ).show()

                        val newUser = hashMapOf(
                            "name" to currentUser.email,
                            "username" to username.text.toString(),
                            "id" to currentUser.uid
                        )

                        db.collection("users").document(currentUser.uid)
                            .set(newUser)
                            .addOnSuccessListener {
                                Log.d("TAG", "DocumentSnapshot successfully written!")
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

    }

    }