package com.maisel.common

import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.maisel.R
import com.maisel.databinding.ViewCallToActionButtonBinding
import com.maisel.utils.dpToPx

//TODO: SAFE TO DELETE
class CallToActionButton(context: Context, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    private val binding = ViewCallToActionButtonBinding.bind(
        LayoutInflater.from(context)
            .inflate(R.layout.view_call_to_action_button, this, true)
    )

    private val fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    private val drawableBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.call_to_action_button_background)

    init {
        val typedValue = TypedValue()
        val styledAttribute = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val accentColor = styledAttribute.getColor(0, 0)
        styledAttribute.recycle()

        drawableBackground?.setColorFilter(accentColor, PorterDuff.Mode.SRC)
        background = drawableBackground

        elevation = context.dpToPx(4f).toFloat()
        stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.button_elevation)

        if (attrs != null) {
            val attributesArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CallToActionButton)
            val text = attributesArray.getString(R.styleable.CallToActionButton_android_text)

            if (text != null && text.isNotEmpty()) {
                binding.textView.text = text
            }

            val textColor = attributesArray.getColor(R.styleable.CallToActionButton_cab_text_color, accentColor)
            if (textColor != accentColor) {
                setButtonTextColor(textColor)
            }

            val textSize = attributesArray.getDimensionPixelSize(R.styleable.CallToActionButton_cab_text_size, 0).toFloat()
            if (textSize != 0f) {
                binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }

            if (attributesArray.hasValue(R.styleable.CallToActionButton_cab_spinner_color)) {
                val spinnerColor = attributesArray.getColor(R.styleable.CallToActionButton_cab_spinner_color, 0)
                binding.progressBar.indeterminateTintList = ColorStateList.valueOf(spinnerColor)
            }

            val bgColor = attributesArray.getColor(R.styleable.CallToActionButton_cab_background_color, accentColor)

            if (bgColor != accentColor) {
                setButtonBackgroundColor(bgColor)
            }

            attributesArray.recycle()
        }
    }

    fun setLoading() {
        binding.progressBar.animation = fadeIn
        binding.progressBar.visibility = View.VISIBLE
        binding.textView.visibility = View.INVISIBLE
    }

    fun setComplete() {
        binding.textView.animation = fadeIn
        binding.progressBar.visibility = View.GONE
        binding.textView.text = "Done"
        binding.textView.visibility = View.VISIBLE
    }

    fun setFailed() {
        binding.textView.animation = fadeIn
        binding.textView.text = "Failed"
        binding.progressBar.visibility = View.GONE
        binding.textView.visibility = View.VISIBLE
    }

    private fun setText(text: String?) {
        binding.textView.text = text
    }

    private fun setButtonTextColor(@ColorInt color: Int) {
        binding.textView.setTextColor(color)
    }

    private fun setButtonBackgroundColor(@ColorInt color: Int) {
        drawableBackground?.setColorFilter(color, PorterDuff.Mode.SRC)
        background = drawableBackground
    }
}
