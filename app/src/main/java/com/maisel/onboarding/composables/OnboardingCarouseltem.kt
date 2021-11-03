package com.maisel.onboarding.composables

import com.maisel.R

class OnboardingCarouseltem(
    val title: String,
    val text: String,
    val image: Int,
) {
    companion object {
        fun get() : List<OnboardingCarouseltem> {
            return listOf(
                OnboardingCarouseltem("Title 1", "Text 1", R.drawable.ic_dough ),
                OnboardingCarouseltem("Title 2", "Text 2", R.drawable.ic_dough ),
                OnboardingCarouseltem("Title 3", "Text 3", R.drawable.ic_dough )
            )
        }
    }
}