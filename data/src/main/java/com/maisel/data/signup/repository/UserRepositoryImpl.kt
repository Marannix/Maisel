package com.maisel.data.signup.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.repository.UserRepository
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

//TODO: Rename package to @user
class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth,
                         private val database: DatabaseReference) : UserRepository {

    override fun createAccount(
        name: String,
        email: String,
        password: String
    ): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
            .map { authResults ->
                val user = SignUpUser(null, name, email, password, null, null)

                //TODO: Maybe throw an exception if current user is null?
                setUserInDatabase(user)
                authResults
            }
            .subscribeOn(Schedulers.io())
    }

    override fun signInWithEmailAndPassword(email: String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
            .subscribeOn(Schedulers.io())
    }

    override fun signInWithCredential(idToken: String, credential: AuthCredential) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithCredential(firebaseAuth, credential)
            .subscribeOn(Schedulers.io())
    }

    override fun setCurrentUser(firebaseUser: FirebaseUser) {
        val user = SignUpUser(firebaseAuth.uid, firebaseUser.displayName, null, null, firebaseUser.photoUrl.toString(), null)
        setUserInDatabase(user)
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun logoutUser() {
        return firebaseAuth.signOut()
    }

    private fun setUserInDatabase(user: SignUpUser) {
        //TODO: Maybe throw an exception if current user is null?
        val id = firebaseAuth.currentUser!!.uid
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
    }
}
