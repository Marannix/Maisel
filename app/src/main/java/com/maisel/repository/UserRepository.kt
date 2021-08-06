package com.maisel.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.maisel.model.SignUpUserEntity
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers


class UserRepositoryImpl(private val firebaseAuth: FirebaseAuth) : UserRepository {

    //private val firebaseAuth = FirebaseAuth.getInstance()

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
        //return Observable.fromCallable {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
            .map { authResults ->
                        val user = SignUpUserEntity(name, email, password)

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
                        //        ?: return@map//TODO: Throw null pointer
                }
            .subscribeOn(Schedulers.io())
//        return Observable.just(
//            firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val user = SignUpUserEntity(name, email, password)
//                        val id =
//                            task.result?.user?.uid
//                                ?: return@addOnCompleteListener //TODO: Throw null pointer
//
//                        database.child("Users").child(id).setValue(user)
//                            .addOnSuccessListener {
//                                Log.d("Joshua123", "database created successfully")
//                            }
//                            .addOnFailureListener {
//                                Log.d("Joshua123", "database failed successfully")
//
//                            }
//                            .addOnCompleteListener {
//                                Log.d("Joshua123", "database completed successfully")
//                            }
//
//                        Log.d("Joshua123", "User created successfully")
//                    } else {
//                        Log.d("Joshua123", task.exception.toString())
//                    }
//                }).subscribeOn(Schedulers.io())
    }
}

//
//        return Completable.fromCallable {
//            try {
//                firebaseAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("Joshua123", "database created successfully")
//                        } else {
//                            Log.d("Joshua123", "Not successful")
//                            Log.d("Joshua123", task.exception.toString())
//                        }
//                    }
//            } catch (e: Exception){
//                Log.d("Joshua123", e.message.toString())
//
//            }
//
//        }.subscribeOn(Schedulers.io())
//        return Observable.just(firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    return@addOnCompleteListener
//                }
//                // return@addOnCompleteListener Observable.just(OuchMeOuch.Success)
//            }
//            .addOnFailureListener {
//
//            })
//            .map<OuchMeOuch> {
//                return@map OuchMeOuch.Success
//            }
//            .doOnError { Log.d("error", it.message.toString()) }
    //.startWith(OuchMeOuch.Loading)
    // .subscribeOn(Schedulers.io())


//        return Observable.just(firebaseAuth.createUserWithEmailAndPassword(email, password))
//            .map { task ->
//                task.addOnCompleteListener {
//                    if (task.isSuccessful) {
//                        val user = SignUpUser(name, email, password)
////                        val id =
////                            task.result?.user?.uid
////                                ?: return@addOnCompleteListener//TODO: Throw null pointer
////
//                                 val id = task.result?.user?.uid ?: " "//TODO: Throw null pointer
//
//                        database.child("Users").child(id).setValue(user)
//                            .addOnSuccessListener {
//                                Log.d("Joshua123", "database created successfully")
//                            }
//                            .addOnFailureListener {
//                                Log.d("Joshua123", "database failed successfully")
//                                OuchMeOuch.Error
//
//                            }.addOnCompleteListener {
//                                Log.d("Joshua123", "database completed successfully")
//                            }
//
//                        //    Completable.complete()
//                        Log.d("Joshua123", "User created successfully")
//                        OuchMeOuch.Success
//
//                    } else {
//                        // Completable.error()
//                        Log.d("Joshua123", "Not successful")
//                        Log.d("Joshua123", task.exception.toString())
//                        OuchMeOuch.Error
//                    }
//                }.addOnFailureListener {
//                    OuchMeOuch.Error
//                }
//            }
//            .doOnError { Log.d("error", it.message.toString()) }
//            //.startWith(OuchMeOuch.Loading)
//            .subscribeOn(Schedulers.io())


//        return Completable.fromCallable {
//            firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val user = SignUpUser(name, email, password)
//                        val id =
//                            task.result?.user?.uid
//                                ?: return@addOnCompleteListener //TODO: Throw null pointer
//
//                        database.child("Users").child(id).setValue(user)
//                            .addOnSuccessListener {
//                                Log.d("Joshua123", "database created successfully")
//                            }
//                            .addOnFailureListener {
//                                Log.d("Joshua123", "database failed successfully")
//
//                            }.addOnCompleteListener {
//                                Log.d("Joshua123", "database completed successfully")
//                            }
//
//                        //    Completable.complete()
//                        Log.d("Joshua123", "User created successfully")
//                    } else {
//                        // Completable.error()
//                        Log.d("Joshua123", "Not successful")
//                        Log.d("Joshua123", task.exception.toString())
//                    }
//                }
//        }.doOnError {
//            Log.d("Joshua123", "Completable failed successfully")
//            Log.d("Joshua123", it.message.toString())
//        }.subscribeOn(Schedulers.io())

//
//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val user = SignUpUser(name, email, password)
//                val id =
//                    task.result?.user?.uid ?: return@addOnCompleteListener //TODO: Throw null pointer
//
//                database.child("Users").child(id).setValue(user)
//                    .addOnSuccessListener {
//                        Log.d("Joshua123", "database created successfully")
//                    }
//                    .addOnFailureListener {
//                        Log.d("Joshua123", "database failed successfully")
//
//                    }.addOnCompleteListener {
//                        Log.d("Joshua123", "database completed successfully")
//                    }
//
//                Log.d("Joshua123", "User created successfully")
//            } else {
//                Log.d("Joshua123", task.exception.toString())
//            }
//        }
//}


sealed class OuchMeOuch {
    object Loading : OuchMeOuch()
    object Success : OuchMeOuch()
    object Error : OuchMeOuch()
    object Error2 : OuchMeOuch()
    object Error3 : OuchMeOuch()

}