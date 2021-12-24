package com.maisel.signin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.accompanist.pager.ExperimentalPagerApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class SignInActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignInActivity>()

    @Test
    fun loginIsLoaded() {
        val email = composeTestRule.onNodeWithText("Email")
        email.assertIsDisplayed()
    }
}
