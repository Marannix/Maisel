package com.maisel.data.signup.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.data.coroutine.DispatcherProvider
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.repository.UserRepository
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

//TODO: Rename package to @user
@ExperimentalCoroutinesApi
class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference) : UserRepository {

    private var listOfUsers = BehaviorSubject.create<List<SignUpUser>>()
    private var userListeners: ValueEventListener? = null

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

    override suspend fun makeLoginRequest(email: String, password: String): AuthResult? {
        return withContext(DispatcherProvider.IO) {
            try {
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .await()
            } catch (e: Exception) {
                Log.d("joshua exception", e.toString())
                null
            }
        }
    }

    override suspend fun signInWithCredential(
        credential: AuthCredential
    ): AuthResult? {
        return withContext(DispatcherProvider.IO) {
            try {
                firebaseAuth
                    .signInWithCredential(credential)
                    .await()
            } catch (e: Exception) {
                Log.d("joshua exception", e.toString())
                null
            }
        }
    }

    override fun setCurrentUser(firebaseUser: FirebaseUser) {
        val user = SignUpUser(
            firebaseUser.uid,
            firebaseUser.displayName,
            null,
            null,
            firebaseUser.photoUrl.toString(),
            null
        )
        //  setUserInDatabase(user)
    }

    override fun getFirebaseCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun getCurrentUser() = callbackFlow<Result<SignUpUser>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var user = SignUpUser()
                snapshot.children.forEach { children ->
                    val users = children.getValue(SignUpUser::class.java)
                    if (firebaseAuth.currentUser != null && users != null) {
                        if (firebaseAuth.currentUser!!.uid == users.userId) {
                            user = users
                        }
                    }
                }
                this@callbackFlow.sendBlocking(Result.success(user))
            }
        }

        //TODO: Rename "Users" to "users"
        database.child("Users").addValueEventListener(postListener)

        awaitClose {
            database.child("Users").removeEventListener(postListener)
        }
    }

    override fun logoutUser() {
        return firebaseAuth.signOut()
    }

    override fun observeListOfUsers(): Observable<List<SignUpUser>> = listOfUsers

    // https://medium.com/swlh/how-to-use-firebase-realtime-database-with-kotlin-coroutine-flow-946fe4cf2cd9
    override fun fetchListOfUsers() = callbackFlow<Result<List<SignUpUser>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<SignUpUser>()
                snapshot.children.forEach { children ->
                    val users = children.getValue(SignUpUser::class.java)
                    users?.userId = children.key

                    users?.let { user ->
                        list.add(user.copy(username = user.username?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }))
                    }
                }
                this@callbackFlow.sendBlocking(Result.success(list))
            }
        }

        //TODO: Rename "Users" to "users"
        database.child("Users").addValueEventListener(postListener)

        awaitClose {
            database.child("Users").removeEventListener(postListener)
        }
    }

    override fun getSenderUid(): String? {
        return firebaseAuth.uid
    }

    private fun setUserInDatabase(user: SignUpUser) {
        //TODO: Maybe throw an exception if current user is null?
        val id = firebaseAuth.currentUser!!.uid
        val userWithId = user.copy(userId = id)
        database.child("Users").child(id).setValue(userWithId)
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
