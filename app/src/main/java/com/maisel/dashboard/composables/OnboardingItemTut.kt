package com.maisel.dashboard.composables

import com.maisel.R

class OnboardingItemTut(
    val title: String,
    val text: String,
    val image: Int,
) {
    companion object {
        fun get() : List<OnboardingItemTut> {
            return listOf(
                OnboardingItemTut("Title 1", "Text 1", R.drawable.ic_dough ),
                OnboardingItemTut("Title 2", "Text 2", R.drawable.ic_dough ),
                OnboardingItemTut("Title 3", "Text 3", R.drawable.ic_dough )
            )
        }
    }
}