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
                OnboardingCarouseltem("This project is for exploring new options and libraries", "Duis sit amet quam diam. Etiam luctus odio vel auctor molestie. In hac habitasse platea dictumst. ", R.drawable.ic_undraw_exploring),
                OnboardingCarouseltem("I've implemented compose for onboarding journey", "Mauris vestibulum, sem elementum laoreet venenatis, tortor odio gravida orci, quis tincidunt est sem sit amet nulla. ", R.drawable.ic_undraw_mobile_testing ),
                OnboardingCarouseltem("To Login page we go!", "Sed lobortis dapibus luctus. Sed tincidunt malesuada tincidunt. Ut elit tellus, tempor non odio sit amet, tempus finibus felis.", R.drawable.ic_undraw_mobile_login )
            )
        }
    }
}