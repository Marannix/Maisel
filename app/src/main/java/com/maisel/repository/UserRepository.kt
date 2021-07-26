package com.maisel.repository

import android.R
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.maisel.MainActivity
import com.maisel.model.SignUpUser


class UserRepository : UserRepositoryImpl {

    private val firebaseAuth = FirebaseAuth.getInstance()
    //TODO: Extract path to constant
    //TODO: Next time don't change region from the default
    private val database: DatabaseReference = Firebase.database("https://maisel-32ad9-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun createAccount(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = SignUpUser(name, email, password)
                val id =
                    task.result?.user?.uid ?: return@addOnCompleteListener // Throw null pointer
                
                database.child("Users").child(id).setValue(user)
                    .addOnSuccessListener {
                        Log.d("Joshua123", "database created successfully")
                    }
                    .addOnFailureListener {
                        Log.d("Joshua123", "database failed successfully")

                    }.addOnCompleteListener {
                        Log.d("Joshua123", "database completed successfully")
                    }

                Log.d("Joshua123", "User created successfully")
            } else {
                Log.d("Joshua123", task.exception.toString())
            }
        }
    }
}