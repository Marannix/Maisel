package com.maisel.showcase.composables

import com.maisel.R

class ShowcaseItem(
    val title: String,
    val text: String,
    val image: Int,
) {
    companion object {
        fun get() : List<ShowcaseItem> {
            return listOf(
                ShowcaseItem("This project is for exploring new options and libraries", "Duis sit amet quam diam. Etiam luctus odio vel auctor molestie. In hac habitasse platea dictumst. ", R.drawable.ic_undraw_exploring),
                ShowcaseItem("I've implemented compose for onboarding journey", "Mauris vestibulum, sem elementum laoreet venenatis, tortor odio gravida orci, quis tincidunt est sem sit amet nulla. ", R.drawable.ic_undraw_mobile_testing ),
                ShowcaseItem("To Login page we go!", "Sed lobortis dapibus luctus. Sed tincidunt malesuada tincidunt. Ut elit tellus, tempor non odio sit amet, tempus finibus felis.", R.drawable.ic_undraw_mobile_login )
            )
        }
    }
}
