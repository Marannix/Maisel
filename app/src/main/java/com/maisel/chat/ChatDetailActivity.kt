package com.maisel.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import com.maisel.chat.composables.ChatDetailScreen
import com.maisel.common.BaseActivity
import com.maisel.ui.MainTheme

@ExperimentalComposeUiApi
class ChatDetailActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ChatDetailActivity::class.java)
        }
    }

    //https://android--code.blogspot.com/2021/03/jetpack-compose-how-to-use-topappbar.html
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ChatDetailScreen()
                }
            }
        }
    }
}
