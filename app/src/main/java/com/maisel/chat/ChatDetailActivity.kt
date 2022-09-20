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
@Deprecated("TODO: This activity can be deleted")
class ChatDetailActivity : BaseActivity() {

}
