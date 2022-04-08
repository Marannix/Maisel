package com.maisel.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.chat.composables.ChatDetailScreen
import com.maisel.common.BaseActivity
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.domain.user.entity.User
import com.maisel.message.MessageViewModel

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
class ChatDetailActivity : BaseActivity() {

    private val user: User by lazy { requireNotNull(intent.getParcelableExtra(USER)) }
    private lateinit var senderId: String
    private lateinit var receiverId: String

    private val viewModel: ChatDetailViewModel by lazy {
        ViewModelProvider(this)[ChatDetailViewModel::class.java]
    }

    private val messageViewModel: MessageViewModel by lazy {
        ViewModelProvider(this)[MessageViewModel::class.java]
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
                        ChatDetailScreen(viewModel, messageViewModel, ::onBackPressed)
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

        viewModel.getSenderUid()?.let { senderId = it }
        user.userId?.let { receiverId = it  }

        messageViewModel.setSenderUid(senderId)
        messageViewModel.setReceiverId(receiverId)

        viewModel.getMessageItems(senderId, receiverId)
    }

    companion object {
        private const val USER = "USER"
        fun createIntent(context: Context, user: User): Intent {
            return Intent(context, ChatDetailActivity::class.java).apply {
                putExtra(USER, user)
            }
        }
    }
}
