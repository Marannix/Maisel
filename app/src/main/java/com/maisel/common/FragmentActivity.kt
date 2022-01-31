package com.maisel.common

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragmentActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.valueOf("DEBUG"));
    }
}
