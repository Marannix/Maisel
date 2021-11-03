package com.maisel.onboarding.composables.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class OnboardingProvider: PreviewParameterProvider<Unit> {
    override val values: Sequence<Unit>
        get() = sequenceOf(Unit)

    override val count: Int
        get() = values.count()
}