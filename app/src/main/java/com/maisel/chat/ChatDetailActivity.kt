package com.maisel.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.chat.composables.ChatDetailScreen
import com.maisel.common.BaseActivity
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.domain.user.entity.SignUpUser

@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
class ChatDetailActivity : BaseActivity() {

    private val user : SignUpUser by lazy { requireNotNull(intent.getParcelableExtra(USER)) }
    private lateinit var senderRoom : String
    private lateinit var receiverRoom: String

    private val viewModel: ChatDetailViewModel by lazy {
        ViewModelProvider(this)[ChatDetailViewModel::class.java]
    }

    //https://android--code.blogspot.com/2021/03/jetpack-compose-how-to-use-topappbar.html
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ChatTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Surface {
                        viewModel.setUser(user)
                        ChatDetailScreen(viewModel, ::onBackPressed)
                    }
                }
            }
        }

        setup()
    }

    private fun setup() {
        if (user.userId == null || viewModel.getSenderUid() == null) {
            finish() //TODO: Is this possible?
        }

        senderRoom = user.userId + viewModel.getSenderUid()
        receiverRoom =  viewModel.getSenderUid() + user.userId
    }

    override fun onResume() {
        super.onResume()
        viewModel.startListeningToMessages(senderRoom)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListeningToMessages(senderRoom)
    }

    companion object {
        private const val USER = "USER"
        fun createIntent(context: Context, user: SignUpUser): Intent {
            return Intent(context, ChatDetailActivity::class.java).apply {
                putExtra(USER, user)
            }
        }
    }
}
