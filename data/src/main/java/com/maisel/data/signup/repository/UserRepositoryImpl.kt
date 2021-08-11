package com.maisel.data.signup.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.repository.UserRepository
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

//TODO: Rename package to @user
class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth) : UserRepository {

    //TODO: Extract path to constant
    //TODO: Next time don't change region from the default
    // https://stackoverflow.com/questions/44606621/firebase-database-not-working-and-no-response
    private val database: DatabaseReference =
        Firebase.database("https://maisel-32ad9-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun createAccount(
        name: String,
        email: String,
        password: String
    ): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
            .map { authResults ->
                val user = SignUpUser(name, email, password)

                val id = authResults.user!!.uid

                database.child("Users").child(id).setValue(user)
                    .addOnSuccessListener {
                        Log.d("Joshua123", "database created successfully")
                    }
                    .addOnFailureListener {
                        Log.d("Joshua123", "database failed successfully")

                    }
                    .addOnCompleteListener {
                        Log.d("Joshua123", "database completed successfully")
                    }

                authResults
            }
            .subscribeOn(Schedulers.io())
    }

    override fun signInWithEmailAndPassword(email: String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
            .subscribeOn(Schedulers.io())
    }
}
