package com.maisel.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.maisel.chat.composables.ChatDetailScreen
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.message.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class ChatDetailFragment : Fragment() {

    // private val receiverUser: User by lazy { requireNotNull(intent.getParcelableExtra(RECEIVER_USER_KEY)) }

//    private val viewModel: ChatDetailViewModel by lazy {
//        ViewModelProvider(this)[ChatDetailViewModel::class.java]
//    }

    private val viewModel: ChatDetailViewModel by lazy { ViewModelProvider(requireActivity())[ChatDetailViewModel::class.java] }

    private val messageViewModel: MessageViewModel by lazy { ViewModelProvider(requireActivity())[MessageViewModel::class.java] }

    private val args: ChatDetailFragmentArgs by navArgs()

    //https://android--code.blogspot.com/2021/03/jetpack-compose-how-to-use-topappbar.html
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WindowCompat.setDecorFitsSystemWindows(window, false)

        setup()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  receiverUserId =
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    ProvideWindowInsets(
                        windowInsetsAnimationsEnabled = true,
                        consumeWindowInsets = false
                    ) {
                        Surface {
                            viewModel.setUser(args.id)
                            ChatDetailScreen(viewModel, messageViewModel, null)
                        }
                    }
                }
            }
        }
    }

    private fun setup() {
//        if (receiverUser.userId == null) {
//            f() //TODO: Is this possible?
//        }

        val receiverId = args.id
        val senderId = viewModel.viewState.value?.senderUid!!

        messageViewModel.setSenderUid(senderId)
        messageViewModel.setReceiverId(receiverId)

        viewModel.getMessageItem(senderId, receiverId)
        viewModel.listToChatMessage(senderId, receiverId)
    }
}
