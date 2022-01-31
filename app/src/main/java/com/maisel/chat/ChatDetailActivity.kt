package com.maisel.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.maisel.chat.composables.ChatDetailScreen
import com.maisel.common.BaseActivity
import com.maisel.compose.ui.theme.ChatTheme

@ExperimentalComposeUiApi
class ChatDetailActivity : BaseActivity() {

    //https://android--code.blogspot.com/2021/03/jetpack-compose-how-to-use-topappbar.html
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ChatTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Surface {
                        ChatDetailScreen()
                    }
                }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ChatDetailActivity::class.java)
        }
    }
}
