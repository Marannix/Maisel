package com.maisel.data.user.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maisel.data.coroutine.DispatcherProvider
import com.maisel.data.database.LocalPersistenceManager
import com.maisel.data.user.dao.UserDao
import com.maisel.data.user.mapper.toDomain
import com.maisel.data.user.mapper.toEntity
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

@ExperimentalCoroutinesApi
class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: DatabaseReference,
    private val localPersistenceManager: LocalPersistenceManager,
    private val userDao: UserDao
) : UserRepository {

    override fun createAccount(
        name: String,
        email: String,
        password: String
    ): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
            .map { authResults ->
                val user = User(firebaseAuth.currentUser!!.uid, name, email, password, null, null)

                //TODO: Maybe throw an exception if current user is null?
                setUserInDatabase(user)
                authResults
            }
            .subscribeOn(Schedulers.io())
    }

    override suspend fun makeLoginRequest(email: String, password: String): AuthResult? {
        Log.d("joshua", "log in request")

        return withContext(DispatcherProvider.IO) {
            try {
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .await().also {
                        Log.d("joshua", "trying to log in")
                    }
            } catch (e: Exception) {
                Log.d("joshua exception", e.toString())
                null
            }
        }
    }

    override suspend fun signInWithCredential(
        credential: AuthCredential
    ): AuthResult? {
        Log.d("joshua", "sign in with cred")

        return withContext(DispatcherProvider.Main) {
            try {
                firebaseAuth
                    .signInWithCredential(credential)
                    .await().also {
                        Log.d("joshua", "trying to log in")
                    }
            } catch (e: Exception) {
                Log.d("joshua exception", e.toString())
                null
            }
        }
    }

    override fun getLoggedInUser(): User? {
        if (firebaseAuth.currentUser == null) {
            localPersistenceManager.setLoggedInUser(null)
        }

        return localPersistenceManager.getLoggedInUser()
    }

    override fun listenToLoggedInUser() = callbackFlow<Result<User>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                localPersistenceManager.setLoggedInUser(user)
                Log.d("joshua repo: ", user.toString())
                this@callbackFlow.sendBlocking(Result.success(user ?: User()))
            }
        }
        
        Log.d("joshua", "current user: ${firebaseAuth.currentUser}")
        firebaseAuth.currentUser?.uid?.let { uid ->
            database.child("users").child(uid).addValueEventListener(postListener)

            awaitClose {
                database.child("users").child(uid).removeEventListener(postListener)
            }
        }
    }

    override fun logoutUser() = callbackFlow<Result<Unit>> {
        val listener = FirebaseAuth.AuthStateListener {
            localPersistenceManager.setLoggedInUser(null)
            if (it.currentUser != null) {
                it.signOut()
                this@callbackFlow.trySendBlocking(Result.success(Unit))
            } else {
                this@callbackFlow.trySendBlocking(Result.failure(Exception("Already logged out")))
            }
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override suspend fun getUsers(): Flow<List<User>> {
        return userDao.getUsers().distinctUntilChanged()
            .map { listOfUsers ->
                listOfUsers.map { user ->
                    user.toDomain()
                }
            }
    }

    override suspend fun insertUsers(users: List<User>) {
        userDao.insertUsers(users.filter { it.userId != null }.map { it.toEntity() })
    }

    // https://medium.com/swlh/how-to-use-firebase-realtime-database-with-kotlin-coroutine-flow-946fe4cf2cd9
    override fun fetchListOfUsers() = callbackFlow<Result<List<User>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<User>()
                snapshot.children.forEach { children ->
                    val users = children.getValue(User::class.java)
                    users?.userId = children.key

                    users?.let { user ->
                        list.add(user.copy(username = user.username?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }))
                    }
                }
                trySend(Result.success(list))
            }
        }

        //TODO: Rename "users" to "users"
        database.child("users").addValueEventListener(postListener)

        awaitClose {
            database.child("users").removeEventListener(postListener)
        }
    }

    private fun setUserInDatabase(user: User) {
        //TODO: Maybe throw an exception if current user is null?
        database.child("users").child(user.userId!!).setValue(user)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
            .addOnCompleteListener {

            }
    }

    override fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    override fun getRecipientUser(userId: String): User {
        return userDao.getUser(userId).toDomain()
    }
}
